package net.cactusthorn.config.compiler;

import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import static com.google.testing.compile.CompilationSubject.assertThat;

public class ConfigProcessorTest {

    @Test public void simple() {
        Compilation compilation = Compiler.javac().withProcessors(new ConfigProcessor())
                .compile(JavaFileObjects.forResource("test/Simple.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }
}
