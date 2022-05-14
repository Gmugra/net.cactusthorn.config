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

import static net.cactusthorn.config.compiler.methodvalidator.MethodInfo.StringMethod;
import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_STRING_CLASS;
import static javax.lang.model.element.ElementKind.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public class StringTypeValidator extends MethodValidatorAncestor {

    private final TypeMirror stringTM;
    private final TypeMirror runtimeExceptionTM;

    public StringTypeValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv);
        stringTM = processingEnv.getElementUtils().getTypeElement(String.class.getName()).asType();
        runtimeExceptionTM = processingEnv.getElementUtils().getTypeElement(RuntimeException.class.getName()).asType();
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return next(methodElement, typeMirror);
        }
        if (processingEnv().getTypeUtils().isSameType(stringTM, typeMirror)) {
            return new MethodInfo(methodElement).withStringMethod(StringMethod.STRING, typeMirror);
        }

        TypeElement typeElement = (TypeElement) ((DeclaredType) typeMirror).asElement();

        Set<StringMethod> methods =
            typeElement.getEnclosedElements().stream().map(this::find).filter(sm -> sm != null).collect(Collectors.toSet());

        StringMethod stringMethod = null;
        if (typeElement.getKind() == ENUM && methods.contains(StringMethod.FROMSTRING)) {
            stringMethod = StringMethod.FROMSTRING;
        } else if (methods.contains(StringMethod.VALUEOF)) {
            stringMethod = StringMethod.VALUEOF;
        } else if (methods.contains(StringMethod.FROMSTRING)) {
            stringMethod = StringMethod.FROMSTRING;
        } else if (methods.contains(StringMethod.CONSTRUCTOR)) {
            stringMethod = StringMethod.CONSTRUCTOR;
        }

        if (stringMethod != null) {
            return new MethodInfo(methodElement).withStringMethod(stringMethod, typeMirror);
        }

        throw new ProcessorException(msg(RETURN_STRING_CLASS), methodElement);
    }

    private StringMethod find(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        boolean isStatic = modifiers.contains(Modifier.STATIC);
        boolean isPublic = modifiers.contains(Modifier.PUBLIC);
        if (element.getKind() == METHOD && isStatic && isPublic && checkParameter(element) && checkExceptions(element)) {
            String methodName = element.getSimpleName().toString();
            return StringMethod.METHODS.contains(methodName) ? StringMethod.fromMethodName(methodName) : null;
        }
        if (element.getKind() == CONSTRUCTOR && isPublic && checkParameter(element) && checkExceptions(element)) {
            return StringMethod.CONSTRUCTOR;
        }
        return null;
    }

    // Method must contain single String parameter
    private boolean checkParameter(Element methodElement) {
        List<? extends TypeMirror> parameters = ((ExecutableType) methodElement.asType()).getParameterTypes();
        return parameters.size() == 1 && processingEnv().getTypeUtils().isSameType(stringTM, parameters.get(0));
    }

    // Method can throws only RuntimeExceptions
    private boolean checkExceptions(Element methodElement) {
        List<? extends TypeMirror> throwns = ((ExecutableType) methodElement.asType()).getThrownTypes();
        return throwns.stream().noneMatch(tm -> !processingEnv().getTypeUtils().isAssignable(tm, runtimeExceptionTM));
    }
}
