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

import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.METHOD_MUST_EXIST;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.ONLY_INTERFACE;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.METHOD_WITHOUT_PARAMETERS;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_FACTORY_METHOD_CONFIG;

class ConfigProcessorTest {

    static Compiler compiler() {
        return Compiler.javac().withProcessors(new ConfigProcessor());
    }

    @Test void allCorrect() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/AllCorrect.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void abstractClass() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/AbstractClass.java"));
        assertThat(compilation).hadErrorContaining(msg(ONLY_INTERFACE));
    }

    @Test void withoutMethods() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/WithoutMethods.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_MUST_EXIST));
    }

    @Test void extendsInterface() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/AllCorrect.java"),
                JavaFileObjects.forResource("test/SubOne.java"), JavaFileObjects.forResource("test/SubTwo.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void disableAutoReload() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/DisableAutoReload.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void factory() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/SimpleConfig.java"),
                JavaFileObjects.forResource("factory/MyFactory.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void factoryAbstractClass() {
        var compilation = compiler().compile(JavaFileObjects.forResource("test/SimpleConfig.java"),
                JavaFileObjects.forResource("factory/MyAbstractFactory.java"));
        assertThat(compilation).hadErrorContaining(msg(ONLY_INTERFACE));
    }

    @Test void factoryWithoutMethods() {
        var compilation = compiler().compile(JavaFileObjects.forResource("factory/WithoutMethods.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_MUST_EXIST));
    }

    @Test void factoryMethodWithParameter() {
        var compilation = compiler().compile(JavaFileObjects.forResource("factory/MethodWithParameter.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_WITHOUT_PARAMETERS));
    }

    @Test void factoryWrongReturn() {
        var compilation = compiler().compile(JavaFileObjects.forResource("factory/WrongReturn.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_FACTORY_METHOD_CONFIG));
    }

    @Test void disableGPMethod() {
        var compilation = compiler().compile(JavaFileObjects.forResource("globalprefix/DisableGPMethod.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test void disableGPGlobal() {
        var compilation = compiler().compile(JavaFileObjects.forResource("globalprefix/DisableGPGlobal.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
