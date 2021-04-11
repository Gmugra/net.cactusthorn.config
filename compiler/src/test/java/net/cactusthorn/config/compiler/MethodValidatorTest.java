package net.cactusthorn.config.compiler;

import static com.google.testing.compile.CompilationSubject.assertThat;

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.*;

import java.util.Locale;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MethodValidatorTest {

    @BeforeAll static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    private static Compiler compiler() {
        return Compiler.javac().withProcessors(new ConfigProcessor());
    }

    @Test public void methodWithParameter() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/MethodWithParameter.java"));
        assertThat(compilation).hadErrorContaining(msg(METHOD_WITHOUT_PARAMETERS));
    }

    @Test public void voidMethod() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/VoidMethod.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_VOID));
    }

    @Test public void abstractReturn() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/AbstractReturn.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_ABSTRACT));
    }

    @Test public void stringMethods() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/ValueOf.java"));
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test public void wrongClass() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongClass.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_STRING_CLASS));
    }

    @Test public void wrongInterface() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterface.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACES, MethodValidator.INTERFACES));
    }

    @Test public void wrongInterfaceArgEmpty() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceArgEmpty.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE_ARG_EMPTY));
    }

    @Test public void wrongInterfaceWildcard() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceWildcard.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE_ARG_WILDCARD));
    }

    @Test public void WrongInterfaceArgInterface() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceArgAbstract.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACE_ARG_INTERFACE));
    }

    @Test public void WrongInterfaceArgWrongClass() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongInterfaceArgWrongClass.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_STRING_CLASS));
    }

    @Test public void wrongOptionalArgEmpty() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalArgEmpty.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_OPTIONAL_ARG_EMPTY));
    }

    @Test public void wrongOptionalWildcard() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalWildcard.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_OPTIONAL_ARG_WILDCARD));
    }

    @Test public void wrongOptionalArgWrongClass() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalArgWrongClass.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_STRING_CLASS));
    }

    @Test public void wrongOptionalArgWrongInterface() {
        Compilation compilation = compiler().compile(JavaFileObjects.forResource("test/WrongOptionalArgWrongInterface.java"));
        assertThat(compilation).hadErrorContaining(msg(RETURN_INTERFACES, MethodValidator.INTERFACES));
    }
}
