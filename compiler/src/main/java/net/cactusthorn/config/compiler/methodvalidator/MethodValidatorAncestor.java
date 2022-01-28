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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.core.converter.ConverterClass;

public abstract class MethodValidatorAncestor implements MethodValidator {

    private final ProcessingEnvironment processingEnv;

    public MethodValidatorAncestor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    protected ProcessingEnvironment processingEnv() {
        return processingEnv;
    }

    private static final MethodType CONSTRUCTOR = MethodType.methodType(void.class, ProcessingEnvironment.class);

    static MethodValidatorAncestor create(ProcessingEnvironment pe, Class<? extends MethodValidatorAncestor> clazz) {
        try {
            return (MethodValidatorAncestor) MethodHandles.publicLookup().findConstructor(clazz, CONSTRUCTOR).invoke(pe);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private MethodValidator next;

    void setNext(MethodValidator validator) {
        next = validator;
    }

    protected MethodInfo next(ExecutableElement methodElement, TypeMirror typeMirror) {
        if (next != null) {
            return next.validate(methodElement, typeMirror);
        }
        return new MethodInfo(methodElement);
    }

    protected boolean existConverterAnnotation(Element methodElement) {
        ConverterClass annotation = methodElement.getAnnotation(ConverterClass.class);
        if (annotation != null) {
            return true;
        }
        List<? extends AnnotationMirror> annotationMirrors = methodElement.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            ConverterClass superAnnotation = annotationMirror.getAnnotationType().asElement().getAnnotation(ConverterClass.class);
            if (superAnnotation != null) {
                return true;
            }
        }
        return false;
    }
}
