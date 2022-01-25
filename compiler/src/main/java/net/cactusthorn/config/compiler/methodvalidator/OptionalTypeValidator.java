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
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_OPTIONAL_ARG_EMPTY;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_OPTIONAL_ARG_WILDCARD;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_OPTIONAL_DEFAULT;

import java.util.List;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;
import net.cactusthorn.config.core.Default;

public class OptionalTypeValidator extends MethodValidatorAncestor {

    private final TypeMirror optionalTM;
    private final TypeMirror defaultTM;

    private final MethodValidator argumentValidator;

    public OptionalTypeValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        optionalTM = processingEnv.getElementUtils().getTypeElement(Optional.class.getName()).asType();
        defaultTM = processingEnv.getElementUtils().getTypeElement(Default.class.getName()).asType();
        // @formatter:off
        argumentValidator = MethodValidatorChain.builder(processingEnv, InterfaceTypeValidator.class)
            .next(AbstractTypeValidator.class)
            .next(DefaultConvertorValidator.class)
            .next(ConverterValidator.class)
            .next(StringTypeValidator.class)
            .build();
        // @formatter:on
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        Element element = declaredType.asElement();
        TypeMirror optionalTypeMirror = element.asType();
        if (!processingEnv().getTypeUtils().isSameType(optionalTM, optionalTypeMirror)) {
            return next(methodElement, typeMirror);
        }

        checkDefaultAnnotation(methodElement);

        List<? extends TypeMirror> arguments = declaredType.getTypeArguments();
        if (arguments.isEmpty()) {
            throw new ProcessorException(msg(RETURN_OPTIONAL_ARG_EMPTY), methodElement);
        }
        if (arguments.get(0).getKind() == TypeKind.WILDCARD) {
            throw new ProcessorException(msg(RETURN_OPTIONAL_ARG_WILDCARD), methodElement);
        }
        return argumentValidator.validate(methodElement, arguments.get(0)).withOptional();
    }

    private void checkDefaultAnnotation(ExecutableElement methodElement) throws ProcessorException {
        for (AnnotationMirror annotationMirror : methodElement.getAnnotationMirrors()) {
            if (processingEnv().getTypeUtils().isSameType(defaultTM, annotationMirror.getAnnotationType())) {
                throw new ProcessorException(msg(RETURN_OPTIONAL_DEFAULT), methodElement, annotationMirror);
            }
        }
    }
}
