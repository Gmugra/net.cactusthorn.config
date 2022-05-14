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
import static net.cactusthorn.config.compiler.CompilerMessages.Key.ONLY_INTERFACE;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import com.squareup.javapoet.JavaFile;

import net.cactusthorn.config.compiler.configgenerator.ConfigGenerator;
import net.cactusthorn.config.compiler.configinitgenerator.ConfigInitGenerator;
import net.cactusthorn.config.compiler.methodinfo.MethodInfo;
import net.cactusthorn.config.compiler.methodvalidator.AbstractTypeValidator;
import net.cactusthorn.config.compiler.methodvalidator.ConverterValidator;
import net.cactusthorn.config.compiler.methodvalidator.DefaultConverterValidator;
import net.cactusthorn.config.compiler.methodvalidator.InterfaceTypeValidator;
import net.cactusthorn.config.compiler.methodvalidator.MethodValidator;
import net.cactusthorn.config.compiler.methodvalidator.MethodValidatorChain;
import net.cactusthorn.config.compiler.methodvalidator.OptionalTypeValidator;
import net.cactusthorn.config.compiler.methodvalidator.ReturnVoidValidator;
import net.cactusthorn.config.compiler.methodvalidator.StringTypeValidator;
import net.cactusthorn.config.compiler.methodvalidator.WithoutParametersValidator;
import net.cactusthorn.config.core.Accessible;
import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Reloadable;

public class ConfigClassesGenerator implements ClassesGenerator {

    private MethodValidator typeValidator;
    private List<ExecutableElement> objectMethods;
    private List<ExecutableElement> accessibleMethods;
    private List<ExecutableElement> reloadableMethods;

    public static final Comparator<MethodInfo> METHODINFO_COMPARATOR = (mi1, mi2) -> {
        if (mi1 == null && mi2 == null) {
            return 0;
        }
        if (mi1 == null) {
            return 1;
        }
        if (mi2 == null) {
            return -1;
        }
        return mi1.name().compareTo(mi2.name());
    };

    @Override public void init(ProcessingEnvironment processingEnv) {
        objectMethods = ElementFilter
                .methodsIn(processingEnv.getElementUtils().getTypeElement(Object.class.getName()).getEnclosedElements());
        accessibleMethods = ElementFilter
                .methodsIn(processingEnv.getElementUtils().getTypeElement(Accessible.class.getName()).getEnclosedElements());
        reloadableMethods = ElementFilter
                .methodsIn(processingEnv.getElementUtils().getTypeElement(Reloadable.class.getName()).getEnclosedElements());

        // @formatter:off
        typeValidator =
            MethodValidatorChain.builder(processingEnv, WithoutParametersValidator.class)
            .next(ReturnVoidValidator.class)
            .next(InterfaceTypeValidator.class)
            .next(AbstractTypeValidator.class)
            .next(OptionalTypeValidator.class)
            .next(ConverterValidator.class)
            .next(DefaultConverterValidator.class)
            .next(StringTypeValidator.class)
            .build();
        // @formatter:on
    }

    @Override public void generate(RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) throws ProcessorException, IOException {

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Config.class);
        for (Element element : elements) {

            validateInterface(element);

            TypeElement interfaceTypeElement = (TypeElement) element;
            InterfaceInfo interfaceInfo = new InterfaceInfo(processingEnv, interfaceTypeElement);

            // @formatter:off
                List<MethodInfo> methodsInfo =
                     ElementFilter.methodsIn(processingEnv.getElementUtils().getAllMembers(interfaceTypeElement))
                     .stream()
                     .filter(e -> !objectMethods.contains(e))
                     .filter(e -> !(interfaceInfo.accessible() && accessibleMethods.contains(e)))
                     .filter(e -> !(interfaceInfo.reloadable() && reloadableMethods.contains(e)))
                     .map(m -> typeValidator.validate(m, m.getReturnType()).withInterfaceInfo(interfaceInfo).build())
                     .sorted(METHODINFO_COMPARATOR)
                     .collect(Collectors.toList());
                // @formatter:on

            validateMethodExist(element, methodsInfo);

            JavaFile configBuilderFile = new ConfigInitGenerator(interfaceTypeElement, methodsInfo, interfaceInfo).generate();
            // System.out.println(configBuilderFile.toString());
            configBuilderFile.writeTo(processingEnv.getFiler());

            JavaFile configFile = new ConfigGenerator(interfaceTypeElement, methodsInfo, interfaceInfo).generate();
            // System.out.println(configFile.toString());
            configFile.writeTo(processingEnv.getFiler());
        }
    }

    private void validateInterface(Element element) {
        if (element.getKind() != ElementKind.INTERFACE) {
            throw new ProcessorException(msg(ONLY_INTERFACE), element);
        }
    }

    private void validateMethodExist(Element element, List<MethodInfo> methodsInfo) {
        if (methodsInfo.isEmpty()) {
            throw new ProcessorException(msg(METHOD_MUST_EXIST), element);
        }
    }
}
