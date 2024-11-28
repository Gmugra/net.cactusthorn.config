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
import java.util.Objects;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;
import net.cactusthorn.config.compiler.methodinfo.MethodInfo;
import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.converter.ConverterClass;

public class ConverterValidator extends MethodValidatorAncestor {

    public ConverterValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override public MethodInfo.Builder validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        var converterType = getConverterClass(methodElement);
        if (converterType != null) {
            return MethodInfo.builder(methodElement).withConverter(converterType, Converter.EMPTY);
        }
        return
            methodElement.getAnnotationMirrors()
                .stream()
                .map(am -> {
                    var superConverterType = getConverterClass(am.getAnnotationType().asElement());
                    if (superConverterType != null) {
                        var parameters = findParameters(am);
                        return MethodInfo.builder(methodElement).withConverter(superConverterType, parameters);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> next(methodElement, typeMirror));
    }

    private String[] findParameters(AnnotationMirror annotationMirror) {
        var values = annotationMirror.getElementValues();
        for (var entry : values.entrySet()) {
            if ("value()".equals(entry.getKey().toString())) {
                var value = entry.getValue().getValue();
                if (value instanceof List<?>) {
                    @SuppressWarnings("unchecked") var list = (List<? extends AnnotationValue>) value;
                    var result = new String[list.size()];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = list.get(i).getValue().toString();
                    }
                    return result;
                }
            }
        }
        return Converter.EMPTY;
    }

    private TypeMirror getConverterClass(Element element) {
        try {
            var annotation = element.getAnnotation(ConverterClass.class);
            if (annotation != null) {
                annotation.value(); // this will throw MirroredTypeException
            }
            return null;
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
    }
}
