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
