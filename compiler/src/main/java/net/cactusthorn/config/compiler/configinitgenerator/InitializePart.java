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
package net.cactusthorn.config.compiler.configinitgenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

import com.squareup.javapoet.CodeBlock;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.InterfaceInfo;
import net.cactusthorn.config.compiler.methodinfo.MethodInfo;
import net.cactusthorn.config.compiler.methodinfo.ConverterInfo;
import net.cactusthorn.config.compiler.methodinfo.StringMethod;
import net.cactusthorn.config.compiler.methodinfo.StringMethodInfo;

import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.util.ConfigInitializer;

public class InitializePart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {

        var buildBuilder = MethodSpec.methodBuilder("initialize").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(MAP_STRING_OBJECT);

        addConfigHolder(buildBuilder, generator);

        addConverters(buildBuilder, generator.methodsInfo());

        buildBuilder.addStatement("$T<$T,$T> values = new $T<>()", Map.class, String.class, Object.class, HashMap.class);
        generator.methodsInfo()
            .forEach(mi -> buildBuilder.addStatement("values.put($S, $L)", mi.key(), convert(mi, generator.interfaceInfo())));

        classBuilder.addMethod(buildBuilder.addStatement("return values").build());
    }

    private static final String CONFIG_HOLDER = "ch";

    private void addConfigHolder(MethodSpec.Builder buildBuilder, Generator generator) {

        var configInfo = generator.interfaceInfo().configInfo();

        var strategyBlock = CodeBlock.builder().add("$T.$L", LoadStrategy.class, configInfo.loadStrategy().name()).build();

        buildBuilder.addStatement("$T $L = loaders().load($L.class.getClassLoader(), $L, $L)", ConfigHolder.class, CONFIG_HOLDER,
                ConfigInitializer.CONFIG_CLASSNAME_PREFIX + generator.interfaceName().simpleName(), strategyBlock, URIS_ATTR);
    }

    private void addConverters(MethodSpec.Builder buildBuilder, List<MethodInfo> methodInfo) {
        var converters = new HashSet<TypeMirror>();
        methodInfo.forEach(mi -> {
            addConverter(buildBuilder, mi, converters);
            mi.returnMapKeyInfo().ifPresent(mki -> {
                addConverter(buildBuilder, mki, converters);
            });
        });
    }

    private void addConverter(MethodSpec.Builder buildBuilder, MethodInfo methodInfo, Set<TypeMirror> converters) {
        methodInfo.returnConverter().ifPresent(c -> {
            var converter = c.type();
            if (!converters.contains(converter)) {
                converters.add(converter);
                buildBuilder.addStatement("CONVERTERS.computeIfAbsent($T.class, c -> new $T())", converter, converter);
            }
        });
    }

    private CodeBlock convert(MethodInfo mi, InterfaceInfo ii) {
        var builder = findGetMethod(mi).add("(");
        var defaultValue = defaultValue(mi);
        return mi.returnMapKeyInfo().map(keyInfo -> {
            builder.add("$L, ", function(keyInfo.returnConverter(), keyInfo.returnStringMethod(), keyInfo.returnTypeName()));
            builder.add("$L, ", function(mi.returnConverter(), mi.returnStringMethod(), mi.returnTypeName()));
            return builder.add("$L", getKey(mi, ii)).add(split(mi)).add(defaultValue).add(")").build();
        }).orElseGet(() -> {
            builder.add("$L, ", function(mi.returnConverter(), mi.returnStringMethod(), mi.returnTypeName()));
            return builder.add("$L", getKey(mi, ii)).add(split(mi)).add(defaultValue).add(")").build();
        });
    }

    private CodeBlock.Builder findGetMethod(MethodInfo mi) {
        var builder = CodeBlock.builder().add("$L.", CONFIG_HOLDER);
        if (mi.returnOptional()) {
            return builder.add("getOptional");
        }
        return builder.add(mi.returnInterface().map(t -> {
            if (t == List.class) {
                return "getList";
            }
            if (t == Set.class) {
                return "getSet";
            }
            if (t == Map.class) {
                return "getMap";
            }
            if (t == SortedMap.class) {
                return "getSortedMap";
            }
            return "getSortedSet";
        }).orElse("get"));
    }

    private CodeBlock split(MethodInfo mi) {
        return mi.returnInterface().map(i -> CodeBlock.of(", $S", mi.split())).orElse(CodeBlock.of(""));
    }

    private CodeBlock defaultValue(MethodInfo mi) {
        return mi.defaultValue().map(s -> CodeBlock.of(", $S", s)).orElse(CodeBlock.of(""));
    }

    private CodeBlock function(Optional<ConverterInfo> converterInfo, Optional<StringMethodInfo> stringMethodInfo,
            TypeName returnTypeName) {
        return converterInfo.map(ci -> {
            return CodeBlock.builder().add("s -> convert($T.class, s, $L)", ci.type(), converterParameters(ci.parameters())).build();
        }).orElseGet(() -> {
            return stringMethodInfo.map(smi -> {
                var sm = smi.stringMethod();
                if (sm == StringMethod.STRING) {
                    return CodeBlock.builder().add("s -> s").build();
                } else if (sm == StringMethod.CONSTRUCTOR) {
                    return CodeBlock.builder().add("s -> new $T(s)", returnTypeName).build();
                }
                return sm.methodName().map(n -> CodeBlock.builder().add("$T::$L", smi.methodType(), n).build()).orElseThrow();
            }).orElseGet(() -> {
                if (returnTypeName.equals(TypeName.CHAR)) {
                    return CodeBlock.builder().add("s -> s.charAt(0)").build();
                }
                return CodeBlock.builder().add("$T::valueOf", returnTypeName.box()).build();
            });
        });
    }

    private CodeBlock converterParameters(String[] parameters) {
        if (Arrays.equals(Converter.EMPTY, parameters)) {
            return CodeBlock.builder().add("$T.EMPTY", Converter.class).build();
        }
        var builder = CodeBlock.builder().add("new $T[] {", String.class);
        for (int i = 0; i < parameters.length; i++) {
            if (i != 0) {
                builder.add(", ");
            }
            builder.add("$S", parameters[i]);
        }
        return builder.add("}").build();
    }

    private CodeBlock getKey(MethodInfo mi, InterfaceInfo ii) {
        var expression = "expandKey($S)";
        if (ii.globalPrefix() && !mi.disabledFeatures().contains(Disable.Feature.GLOBAL_PREFIX)) {
            expression = "expandKey(globalPrefix($S))";
        }
        return CodeBlock.builder().add(expression, mi.key()).build();
    }
}
