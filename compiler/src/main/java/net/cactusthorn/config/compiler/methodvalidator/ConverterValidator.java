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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;
import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.converter.ConverterClass;

public class ConverterValidator extends MethodValidatorAncestor {

    public ConverterValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        System.out.println("*0*****************");
        System.out.println(methodElement);
        Optional<TypeMirror> converterType = getConverterClass(methodElement);
        if (converterType.isPresent()) {
            System.out.println("*1*****************");
            System.out.println(methodElement);
            System.out.println(converterType.get());
            System.out.println("*1*****************");
            return new MethodInfo(methodElement).withConverter(converterType.get(), Converter.EMPTY);
        }
        System.out.println("*0*****************");

        List<? extends AnnotationMirror> annotationMirrors = methodElement.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            Optional<TypeMirror> superConverterType = getConverterClass(annotationMirror.getAnnotationType().asElement());
            if (superConverterType.isPresent()) {
                String[] parameters = findParameters(annotationMirror);
                return new MethodInfo(methodElement).withConverter(superConverterType.get(), parameters);
            }
        }

        return next(methodElement, typeMirror);
    }

    private String[] findParameters(AnnotationMirror annotationMirror) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> values = annotationMirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : values.entrySet()) {
            if ("value()".equals(entry.getKey().toString())) {
                Object value = entry.getValue().getValue();
                if (value instanceof List<?>) {
                    @SuppressWarnings("unchecked") List<? extends AnnotationValue> list = (List<? extends AnnotationValue>) value;
                    String[] result = new String[list.size()];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = list.get(i).getValue().toString();
                    }
                    return result;
                }
            }
        }
        return Converter.EMPTY;
    }

    private Optional<TypeMirror> getConverterClass(Element element) {
        try {
            ConverterClass annotation = element.getAnnotation(ConverterClass.class);
            if (annotation != null) {
                annotation.value(); // this will throw MirroredTypeException
            }
            return Optional.empty();
        } catch (MirroredTypeException mte) {
            return Optional.of(mte.getTypeMirror());
        }
    }
}
