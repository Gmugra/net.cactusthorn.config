/*
 * Copyright (C) 2021, Alexei Khatskevich
 *
 * Licensed under the BSD 3-Clause license.
 * You may obtain a copy of the License at
 *
 * https://github.com/Gmugra/net.cactusthorn.config/blob/main/LICENSE
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.cactusthorn.config.compiler.methodvalidator;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_INTERFACE;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_INTERFACES;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_INTERFACE_ARG_EMPTY;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_INTERFACE_ARG_WILDCARD;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_INTERFACE_ARG_INTERFACE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.lang.reflect.Type;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;
import net.cactusthorn.config.compiler.methodinfo.MethodInfo;

public class InterfaceOrCollectionTypeValidator extends MethodValidatorAncestor {

    private static final class InterfaceType {
        private Type interfaceType;
        private List<? extends TypeMirror> arguments;

        private InterfaceType(Type interfaceType, List<? extends TypeMirror> arguments) {
            this.interfaceType = interfaceType;
            this.arguments = arguments;
        }

        public Type interfaceType() {
            return interfaceType;
        }

        public List<? extends TypeMirror> arguments() {
            return arguments;
        }
    }

    public static final List<Class<?>> COLLECTIONS = List.of(List.class, Set.class, SortedSet.class, Map.class, SortedMap.class);
    private final Map<TypeMirror, Type> collections = new HashMap<>();

    private final MethodValidator valueValidator;
    private final MethodValidator keyValidator;

    public InterfaceOrCollectionTypeValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        COLLECTIONS.forEach(c -> collections.put(processingEnv.getElementUtils().getTypeElement(c.getName()).asType(), c));

        // @formatter:off
        valueValidator = MethodValidatorChain.builder(processingEnv, AbstractTypeValidator.class)
                .next(ConverterValidator.class)
                .next(DefaultConverterValidator.class)
                .next(StringTypeValidator.class)
                .build();

        keyValidator = MethodValidatorChain.builder(processingEnv, AbstractTypeValidator.class)
                .next(DefaultConverterValidator.class)
                .next(StringTypeValidator.class)
                .build();
        // @formatter:on
    }

    @Override public MethodInfo.Builder validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        var declaredType = (DeclaredType) typeMirror;
        var element = declaredType.asElement();
        if (element.getKind() != ElementKind.INTERFACE) {
            return next(methodElement, typeMirror);
        }
        if (isDefaultConvertor(element)) {
            return next(methodElement, typeMirror);
        }
        if (!isSupportedCollection(element) && existConverterAnnotation(methodElement)) {
            return next(methodElement, typeMirror);
        }
        if (!isSupportedCollection(element)) {
            throw new ProcessorException(msg(RETURN_INTERFACE, element), methodElement);
        }
        InterfaceType interfaceType = getSupportedInterfaceType(methodElement, declaredType, element);
        if (isMap(interfaceType)) {
            return validateMap(methodElement, interfaceType);
        } else {
            return validateArgument(methodElement, interfaceType, 0, valueValidator, true);
        }
    }

    private boolean isSupportedCollection(Element element) {
        return collections.entrySet().stream().anyMatch(e -> processingEnv().getTypeUtils().isSameType(element.asType(), e.getKey()));
    }

    private InterfaceType getSupportedInterfaceType(ExecutableElement methodElement, DeclaredType declaredType, Element element) {
        // @formatter:off
        var interfaceType = collections.entrySet().stream()
            .filter(e -> processingEnv().getTypeUtils().isSameType(element.asType(), e.getKey()))
            .map(e -> e.getValue()).findAny()
            .orElseThrow(() -> new ProcessorException(msg(RETURN_INTERFACES, COLLECTIONS), methodElement));
        // @formatter:on
        var arguments = declaredType.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new ProcessorException(msg(RETURN_INTERFACE_ARG_EMPTY), methodElement);
        }
        return new InterfaceType(interfaceType, arguments);
    }

    private boolean isMap(InterfaceType interfaceType) {
        return interfaceType.interfaceType() == Map.class || interfaceType.interfaceType() == SortedMap.class;
    }

    private MethodInfo.Builder validateMap(ExecutableElement methodElement, InterfaceType interfaceType) {
        var key = validateArgument(methodElement, interfaceType, 0, keyValidator, false);
        var value = validateArgument(methodElement, interfaceType, 1, valueValidator, true).withMapKey(key.build());
        return value;
    }

    private boolean isDefaultConvertor(Element element) {
        return isElementTypeInClasses(element, DefaultConverterValidator.CONVERTERS.keySet());
    }

    private MethodInfo.Builder validateArgument(ExecutableElement methodElement, InterfaceType interfaceType, int argumentIndex,
            MethodValidator validator, boolean checkCustomConverter) {
        if (interfaceType.arguments().get(argumentIndex).getKind() == TypeKind.WILDCARD) {
            throw new ProcessorException(msg(RETURN_INTERFACE_ARG_WILDCARD), methodElement);
        }
        var argumentDeclaredType = (DeclaredType) interfaceType.arguments().get(argumentIndex);
        var argumentElement = argumentDeclaredType.asElement();
        if (argumentElement.getKind() != ElementKind.INTERFACE) {
            return validator.validate(methodElement, interfaceType.arguments().get(argumentIndex))
                    .withInterface(interfaceType.interfaceType());
        }
        if (isDefaultConvertor(argumentElement)) {
            return validator.validate(methodElement, interfaceType.arguments().get(argumentIndex))
                    .withInterface(interfaceType.interfaceType());
        }
        if (checkCustomConverter && existConverterAnnotation(methodElement)) {
            return validator.validate(methodElement, interfaceType.arguments().get(argumentIndex))
                    .withInterface(interfaceType.interfaceType());
        }
        throw new ProcessorException(msg(RETURN_INTERFACE_ARG_INTERFACE), methodElement);
    }
}
