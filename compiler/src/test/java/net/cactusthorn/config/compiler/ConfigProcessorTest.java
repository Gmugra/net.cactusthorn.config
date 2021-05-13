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

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.METHOD_MUST_EXIST;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.ONLY_INTERFACE;

public class ConfigProcessorTest {

    private static Compiler compiler() {
        return Compiler.javac().withProcessors(new ConfigProcessor());
    }

    @Test public void allCorrect() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/AllCorrect.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test public void abstractClass() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/AbstractClass.java"));
        assertThat(compilation).hadErrorContaining(msg(ONLY_INTERFACE));
    }

    @Test public void withoutMethods() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WithoutMethods.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_MUST_EXIST));
    }

    @Test public void extendsInterface() {
        Compilation compilation = compiler().compile(
            JavaFileObjects.forResource("test/AllCorrect.java"),
            JavaFileObjects.forResource("test/SubOne.java"),
            JavaFileObjects.forResource("test/SubTwo.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
