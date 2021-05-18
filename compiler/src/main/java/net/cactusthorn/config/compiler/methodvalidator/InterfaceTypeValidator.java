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
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import java.util.Arrays;
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

public class InterfaceTypeValidator extends MethodValidatorAncestor {

    private static final class ElementInfo {

        private final ProcessingEnvironment processingEnv;
        private final ExecutableElement methodElement;
        private final Map<TypeMirror, Type> interfaces;

        private final DeclaredType declaredType;
        private final Element element;

        private Type interfaceType;
        private List<? extends TypeMirror> arguments;

        private ElementInfo(ProcessingEnvironment processingEnv, ExecutableElement methodElement, TypeMirror typeMirror,
                Map<TypeMirror, Type> interfaces) {

            this.processingEnv = processingEnv;
            this.methodElement = methodElement;
            this.interfaces = interfaces;

            declaredType = (DeclaredType) typeMirror;
            element = declaredType.asElement();
        }

        private void initInterface() {
         // @formatter:off
            interfaceType = interfaces.entrySet().stream()
                .filter(e -> processingEnv.getTypeUtils().isSameType(element.asType(), e.getKey()))
                .map(e -> e.getValue()).findAny()
                .orElseThrow(() -> new ProcessorException(msg(RETURN_INTERFACES, INTERFACES), element));
            // @formatter:on
            arguments = declaredType.getTypeArguments();
            if (arguments.isEmpty()) {
                throw new ProcessorException(msg(RETURN_INTERFACE_ARG_EMPTY), element);
            }
        }

        private boolean isInterface() {
            return element.getKind() == ElementKind.INTERFACE;
        }

        private boolean isMap() {
            return interfaceType == Map.class || interfaceType == SortedMap.class;
        }

        private MethodInfo validateArgument(int argumentIndex, MethodValidator validator) {
            if (arguments().get(argumentIndex).getKind() == TypeKind.WILDCARD) {
                throw new ProcessorException(msg(RETURN_INTERFACE_ARG_WILDCARD), element);
            }
            DeclaredType argumentDeclaredType = (DeclaredType) arguments.get(argumentIndex);
            Element argumentElement = argumentDeclaredType.asElement();
            if (argumentElement.getKind() == ElementKind.INTERFACE) {
                throw new ProcessorException(msg(RETURN_INTERFACE_ARG_INTERFACE), element);
            }
            return validator.validate(methodElement, arguments.get(argumentIndex)).withInterface(interfaceType);
        }

        private List<? extends TypeMirror> arguments() {
            return arguments;
        }
    }

    public static final List<Class<?>> INTERFACES = Arrays.asList(List.class, Set.class, SortedSet.class, Map.class, SortedMap.class);
    private final Map<TypeMirror, Type> interfaces = new HashMap<>();

    private final MethodValidator valueValidator;
    private final MethodValidator keyValidator;

    public InterfaceTypeValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        INTERFACES.forEach(c -> interfaces.put(processingEnv.getElementUtils().getTypeElement(c.getName()).asType(), c));

        // @formatter:off
        valueValidator = MethodValidatorChain.builder(processingEnv, AbstractTypeValidator.class)
                .next(DefaultConvertorValidator.class)
                .next(ConverterValidator.class)
                .next(StringTypeValidator.class)
                .build();

        keyValidator = MethodValidatorChain.builder(processingEnv, AbstractTypeValidator.class)
                .next(DefaultConvertorValidator.class)
                .next(StringTypeValidator.class)
                .build();
        // @formatter:on
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        ElementInfo elementInfo = new ElementInfo(processingEnv(), methodElement, typeMirror, interfaces);
        if (!elementInfo.isInterface()) {
            return next(methodElement, typeMirror);
        }
        elementInfo.initInterface();
        if (elementInfo.isMap()) {
            return validateMap(elementInfo);
        } else {
            return elementInfo.validateArgument(0, valueValidator);
        }
    }

    private MethodInfo validateMap(ElementInfo elementInfo) {
        MethodInfo key = elementInfo.validateArgument(0, keyValidator);
        MethodInfo value = elementInfo.validateArgument(1, valueValidator).withMapKey(key);
        return value;
    }
}
