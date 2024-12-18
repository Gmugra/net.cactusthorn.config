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

import static com.google.testing.compile.CompilationSubject.assertThat;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import java.util.Locale;
import java.util.NavigableMap;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

import java.util.AbstractList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MethodValidatorTest {

    @BeforeAll static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    static Compiler compiler() {
        return Compiler.javac().withProcessors(new ConfigProcessor());
    }

    @Test void methodWithParameter() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/MethodWithParameter.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_WITHOUT_PARAMETERS));
    }

    @Test void voidMethod() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/VoidMethod.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_VOID));
    }

    @Test void abstractReturn() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/AbstractReturn.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_ABSTRACT, AbstractList.class.getName()));
    }

    @Test void stringMethods() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/ValueOf.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void wrongClass() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongClass.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_STRING_CLASS));
    }

    @Test void wrongInterface() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterface.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE, NavigableMap.class.getName()));
    }

    @Test void wrongInterfaceArgEmpty() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceArgEmpty.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE_ARG_EMPTY));
    }

    @Test void wrongInterfaceWildcard() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceWildcard.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE_ARG_WILDCARD));
    }

    @Test void wrongInterfaceArgInterface() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceArgAbstract.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE_ARG_INTERFACE));
    }

    @Test void wrongInterfaceArgWrongClass() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceArgWrongClass.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_STRING_CLASS));
    }

    @Test void wrongOptionalArgEmpty() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalArgEmpty.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_OPTIONAL_ARG_EMPTY));
    }

    @Test void wrongOptionalWildcard() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalWildcard.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_OPTIONAL_ARG_WILDCARD));
    }

    @Test void wrongOptionalArgWrongClass() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalArgWrongClass.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_STRING_CLASS));
    }

    @Test void wrongOptionalArgWrongInterface() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalArgWrongInterface.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE, NavigableMap.class.getName()));
    }

    @Test void wrongOptionalDefaultAnnotation() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalDefaultAnnotation.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_OPTIONAL_DEFAULT));
    }

    @Test void converter() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/ConfigConverter.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void defaultConverter() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/ConfigDefaultConverters.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void keyWithSysProperty() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/KeyWithSysProperty.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
