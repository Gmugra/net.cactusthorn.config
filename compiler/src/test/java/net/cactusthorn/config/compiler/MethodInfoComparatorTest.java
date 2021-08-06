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
package net.cactusthorn.config.compiler;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;
import static net.cactusthorn.config.compiler.ConfigClassesGenerator.METHODINFO_COMPARATOR;

public class MethodInfoComparatorTest {

    private static class SimpleName implements Name {

        private final String simpleName;

        public SimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        @Override public int length() {
            return simpleName.length();
        }

        @Override public char charAt(int index) {
            return simpleName.charAt(index);
        }

        @Override public CharSequence subSequence(int start, int end) {
            return simpleName.subSequence(start, end);
        }

        @Override public boolean contentEquals(CharSequence cs) {
            return simpleName.contentEquals(cs);
        }
    }

    private static class SimpleTypeMirror implements TypeMirror {

        @Override public List<? extends AnnotationMirror> getAnnotationMirrors() {
            return Collections.emptyList();
        }

        @Override public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return null;
        }

        @Override public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
            return null;
        }

        @Override public TypeKind getKind() {
            return TypeKind.DECLARED;
        }

        @Override public <R, P> R accept(TypeVisitor<R, P> v, P p) {
            return null;
        }
    }

    private static class SimpleExecutableElement implements ExecutableElement {

        private final Name simpleName;

        public SimpleExecutableElement(String simpleName) {
            this.simpleName = new SimpleName(simpleName);
        }

        @Override public TypeMirror asType() {
            return null;
        }

        @Override public ElementKind getKind() {
            return ElementKind.CLASS;
        }

        @Override public Set<Modifier> getModifiers() {
            return Collections.emptySet();
        }

        @Override public Element getEnclosingElement() {
            return null;
        }

        @Override public List<? extends Element> getEnclosedElements() {
            return Collections.emptyList();
        }

        @Override public List<? extends AnnotationMirror> getAnnotationMirrors() {
            return Collections.emptyList();
        }

        @Override public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return null;
        }

        @Override public <R, P> R accept(ElementVisitor<R, P> v, P p) {
            return null;
        }

        @Override public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
            return null;
        }

        @Override public List<? extends TypeParameterElement> getTypeParameters() {
            return Collections.emptyList();
        }

        @Override public TypeMirror getReturnType() {
            return new SimpleTypeMirror();
        }

        @Override public List<? extends VariableElement> getParameters() {
            return Collections.emptyList();
        }

        @Override public TypeMirror getReceiverType() {
            return null;
        }

        @Override public boolean isVarArgs() {
            return false;
        }

        @Override public boolean isDefault() {
            return false;
        }

        @Override public List<? extends TypeMirror> getThrownTypes() {
            return Collections.emptyList();
        }

        @Override public AnnotationValue getDefaultValue() {
            return null;
        }

        @Override public Name getSimpleName() {
            return simpleName;
        }

    }

    @Test public void nullNull() {
        assertEquals(0, METHODINFO_COMPARATOR.compare(null, null));
    }

    @Test public void nullNotNull() {
        MethodInfo methodInfo = new MethodInfo(new SimpleExecutableElement("TestOne"));
        assertEquals(1, METHODINFO_COMPARATOR.compare(null, methodInfo));
    }

    @Test public void notNullNull() {
        MethodInfo methodInfo = new MethodInfo(new SimpleExecutableElement("TestOne"));
        assertEquals(-1, METHODINFO_COMPARATOR.compare(methodInfo, null));
    }

    @Test public void same() {
        MethodInfo methodInfo = new MethodInfo(new SimpleExecutableElement("TestOne"));
        assertEquals(0, METHODINFO_COMPARATOR.compare(methodInfo, methodInfo));
    }

    @Test public void notSame() {
        MethodInfo methodInfo1 = new MethodInfo(new SimpleExecutableElement("TestOne"));
        MethodInfo methodInfo2 = new MethodInfo(new SimpleExecutableElement("TestTwo"));
        assertNotEquals(0, METHODINFO_COMPARATOR.compare(methodInfo1, methodInfo2));
    }
}
