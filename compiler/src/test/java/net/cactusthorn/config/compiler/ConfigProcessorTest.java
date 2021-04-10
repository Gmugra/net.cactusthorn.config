package net.cactusthorn.config.compiler;

import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import static com.google.testing.compile.CompilationSubject.assertThat;

public class ConfigProcessorTest {

    private static Compiler compiler() {
        return Compiler.javac().withProcessors(new ConfigProcessor());
    }

    @Test public void allCorrect() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/AllCorrect.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
