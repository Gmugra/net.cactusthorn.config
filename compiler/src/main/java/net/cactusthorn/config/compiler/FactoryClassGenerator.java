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

import static net.cactusthorn.config.compiler.CompilerMessages.msg;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.METHOD_MUST_EXIST;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.METHOD_WITHOUT_PARAMETERS;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.ONLY_INTERFACE;
import static net.cactusthorn.config.compiler.CompilerMessages.Key.RETURN_FACTORY_METHOD_CONFIG;
import static net.cactusthorn.config.core.util.ConfigInitializer.CONFIG_CLASSNAME_PREFIX;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.factory.ConfigFactoryAncestor;
import net.cactusthorn.config.core.factory.ConfigFactoryBuilder;
import net.cactusthorn.config.core.factory.Factory;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loader;
import net.cactusthorn.config.core.loader.Loaders;

public class FactoryClassGenerator implements ClassesGenerator {

    private static final String FACTORY_CLASSNAME_PREFIX = "Factory_";
    private static final TypeName BUILDER_TYPE_NAME = ClassName.get("", "Builder");

    @Override public void generate(RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) throws ProcessorException, IOException {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Factory.class);
        for (Element element : elements) {

            validateInterface(element);

            TypeElement interfaceTypeElement = (TypeElement) element;

            // @formatter:off
            List<ExecutableElement> intefaceMethods =
                interfaceTypeElement.getEnclosedElements()
                    .stream()
                    .filter(e -> e.getKind() == ElementKind.METHOD)
                    .map(ExecutableElement.class::cast)
                    .collect(Collectors.toList());
            // @formatter:on

            validateMethodsExist(element, intefaceMethods);
            validateMethodsParameters(intefaceMethods);
            validateMethodsReturns(processingEnv, intefaceMethods);

            String factoryClassName = FACTORY_CLASSNAME_PREFIX + interfaceTypeElement.getSimpleName();

            TypeSpec.Builder classBuilder = createClassBuilder(interfaceTypeElement, factoryClassName);
            addConstructor(classBuilder);
            addBuilder(classBuilder, factoryClassName);
            addBuilderMethod(classBuilder);
            addInterfaceMethods(processingEnv, classBuilder, intefaceMethods);

            String packageName = ClassName.get(interfaceTypeElement).packageName();
            JavaFile configFactoryFile = JavaFile.builder(packageName, classBuilder.build()).skipJavaLangImports(true).build();
            // System.out.println(configFactoryFile.toString());
            configFactoryFile.writeTo(processingEnv.getFiler());
        }
    }

    private void validateInterface(Element element) {
        if (element.getKind() != ElementKind.INTERFACE) {
            throw new ProcessorException(msg(ONLY_INTERFACE), element);
        }
    }

    private void validateMethodsExist(Element element, List<ExecutableElement> intefaceMethods) {
        if (intefaceMethods.isEmpty()) {
            throw new ProcessorException(msg(METHOD_MUST_EXIST), element);
        }
    }

    public void validateMethodsParameters(List<ExecutableElement> intefaceMethods) {
        for (ExecutableElement intefaceMethod : intefaceMethods) {
            if (!intefaceMethod.getParameters().isEmpty()) {
                throw new ProcessorException(msg(METHOD_WITHOUT_PARAMETERS), intefaceMethod);
            }
        }
    }

    public void validateMethodsReturns(ProcessingEnvironment processingEnv, List<ExecutableElement> intefaceMethods) {
        for (ExecutableElement intefaceMethod : intefaceMethods) {
            Element returnTypeElement = processingEnv.getTypeUtils().asElement(intefaceMethod.getReturnType());
            Config annotation = returnTypeElement.getAnnotation(Config.class);
            if (annotation == null) {
                throw new ProcessorException(msg(RETURN_FACTORY_METHOD_CONFIG), returnTypeElement);
            }
        }
    }

    private TypeSpec.Builder createClassBuilder(TypeElement interfaceTypeElement, String factoryClassName) {
        return TypeSpec.classBuilder(factoryClassName).addModifiers(Modifier.PUBLIC, Modifier.FINAL).superclass(ConfigFactoryAncestor.class)
                .addSuperinterface(interfaceTypeElement.asType());
    }

    private void addConstructor(TypeSpec.Builder classBuilder) {
        classBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE)
                .addParameter(Loaders.class, "loaders", Modifier.FINAL).addStatement("super(loaders)").build());
    }

    private void addBuilderMethod(TypeSpec.Builder classBuilder) {
        classBuilder.addMethod(MethodSpec.methodBuilder("builder").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(BUILDER_TYPE_NAME)
                .addStatement("return new $T()", BUILDER_TYPE_NAME).build());
    }

    private void addInterfaceMethods(ProcessingEnvironment processingEnv, TypeSpec.Builder classBuilder,
            List<ExecutableElement> intefaceMethods) {
        // @formatter:off
        intefaceMethods.stream()
            .map(method -> {
                ClassName returnClassName = ClassName.get((TypeElement) processingEnv.getTypeUtils().asElement(method.getReturnType()));
                return MethodSpec.methodBuilder(method.getSimpleName().toString())
                     .addModifiers(Modifier.PUBLIC)
                     .addAnnotation(Override.class)
                     .returns(TypeName.get(method.getReturnType()))
                     .addStatement("return new $T(loaders())",
                          ClassName.get(returnClassName.packageName(), CONFIG_CLASSNAME_PREFIX + returnClassName.simpleName()));
            })
            .forEach(methodBuilder -> classBuilder.addMethod(methodBuilder.build()));
        // @formatter:on
    }

    private void addBuilder(TypeSpec.Builder classBuilder, String factoryClassName) {
        TypeSpec.Builder builder = TypeSpec.classBuilder("Builder").addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .superclass(ConfigFactoryBuilder.class);
        builder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).addStatement("super()").build());
        addLoaderMethod(builder);
        addLoaderClassMethod(builder);
        addSetLoadStrategyMethod(builder);
        addSetSourceMethod(builder);
        addAddSourceURIMethod(builder);
        addAddSourceStringMethod(builder);
        addAutoReloadMethod(builder);
        addBuildMethod(builder, factoryClassName);
        classBuilder.addType(builder.build());
    }

    private void addLoaderMethod(TypeSpec.Builder builder) {
        builder.addMethod(
                MethodSpec.methodBuilder("addLoader").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(BUILDER_TYPE_NAME)
                        .addParameter(Loader.class, "loader").addStatement("return (Builder) super.addLoader(loader)").build());
    }

    private void addLoaderClassMethod(TypeSpec.Builder builder) {
        WildcardTypeName wildcard = WildcardTypeName.subtypeOf(Loader.class);
        ParameterizedTypeName parameterType = ParameterizedTypeName.get(ClassName.get(Class.class), wildcard);
        builder.addMethod(
                MethodSpec.methodBuilder("addLoader").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(BUILDER_TYPE_NAME)
                        .addParameter(parameterType, "loaderClass").addStatement("return (Builder) super.addLoader(loaderClass)").build());
    }

    private void addSetLoadStrategyMethod(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.methodBuilder("setLoadStrategy").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(BUILDER_TYPE_NAME).addParameter(LoadStrategy.class, "strategy")
                .addStatement("return (Builder) super.setLoadStrategy(strategy)").build());
    }

    private void addSetSourceMethod(TypeSpec.Builder builder) {
        ParameterizedTypeName parameterType = ParameterizedTypeName.get(ClassName.get(Map.class), TypeName.get(String.class),
                TypeName.get(String.class));
        builder.addMethod(
                MethodSpec.methodBuilder("setSource").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(BUILDER_TYPE_NAME)
                        .addParameter(parameterType, "properties").addStatement("return (Builder) super.setSource(properties)").build());
    }

    private void addAddSourceURIMethod(TypeSpec.Builder builder) {
        builder.addMethod(
                MethodSpec.methodBuilder("addSource").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(BUILDER_TYPE_NAME)
                        .addParameter(URI[].class, "uri").varargs(true).addStatement("return (Builder) super.addSource(uri)").build());
    }

    private void addAddSourceStringMethod(TypeSpec.Builder builder) {
        builder.addMethod(
                MethodSpec.methodBuilder("addSource").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class).returns(BUILDER_TYPE_NAME)
                        .addParameter(String[].class, "uri").varargs(true).addStatement("return (Builder) super.addSource(uri)").build());
    }

    private void addAutoReloadMethod(TypeSpec.Builder builder) {
        builder.addMethod(MethodSpec.methodBuilder("autoReload").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(BUILDER_TYPE_NAME).addParameter(long.class, "periodInSeconds")
                .addStatement("return (Builder) super.autoReload(periodInSeconds)").build());
    }

    private void addBuildMethod(TypeSpec.Builder builder, String factoryClassName) {
        TypeName factoryTypeName = ClassName.get("", factoryClassName);
        builder.addMethod(MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(factoryTypeName).addStatement("return new $T(createLoaders())", factoryTypeName).build());
    }
}
