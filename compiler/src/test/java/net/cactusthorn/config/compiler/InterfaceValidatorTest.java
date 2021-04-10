package net.cactusthorn.config.compiler;

import static com.google.testing.compile.CompilationSubject.assertThat;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

public class InterfaceValidatorTest {

    @BeforeAll static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    private static Compiler compiler() {
        return Compiler.javac().withProcessors(new ConfigProcessor());
    }

    @Test public void abstractClass() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/AbstractClass.java"));
        assertThat(compilation).hadErrorContaining(msg(ONLY_INTERFACE));
    }

    @Test public void withoutMethods() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WithoutMethods.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_MUST_EXIST));
    }

    @Test public void methodWithParameter() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/MethodWithParameter.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_WITHOUT_PARAMETERS));
    }
}
