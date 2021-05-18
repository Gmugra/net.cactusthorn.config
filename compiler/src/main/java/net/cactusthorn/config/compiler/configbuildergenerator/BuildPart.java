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
package net.cactusthorn.config.compiler.configbuildergenerator;

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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Annotations;
import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo.ConverterInfo;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo.StringMethod;
import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.util.ConfigBuilder;

public class BuildPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        ClassName configClass = ClassName.get(generator.packageName(),
                ConfigBuilder.CONFIG_CLASSNAME_PREFIX + generator.interfaceName().simpleName());

        MethodSpec.Builder buildBuilder = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(configClass);

        addConfigHolder(buildBuilder, generator);

        addConverters(buildBuilder, generator.methodsInfo());

        buildBuilder.addStatement("$T<$T,$T> values = new $T<>()", Map.class, String.class, Object.class, HashMap.class);
        generator.methodsInfo().forEach(mi -> buildBuilder.addStatement("values.put($S, $L)", mi.key(), convert(mi)));

        classBuilder.addMethod(buildBuilder.addStatement("return new $T(values)", configClass).build());
    }

    private static final String CONFIG_HOLDER = "ch";

    private void addConfigHolder(MethodSpec.Builder buildBuilder, Generator generator) {

        Annotations.ConfigInfo configInfo = generator.interfaceInfo().configInfo();

        CodeBlock strategyBlock = CodeBlock.builder().add("$T.$L", LoadStrategy.class, configInfo.loadStrategy().name()).build();

        buildBuilder.addStatement("$T $L = loaders().load($L.class.getClassLoader(), $L, $L)", ConfigHolder.class, CONFIG_HOLDER,
                ConfigBuilder.CONFIG_CLASSNAME_PREFIX + generator.interfaceName().simpleName(), strategyBlock, URIS_ATTR);
    }

    private void addConverters(MethodSpec.Builder buildBuilder, List<MethodInfo> methodInfo) {
        Set<TypeMirror> converters = new HashSet<>();
        methodInfo.forEach(mi -> {
            if (mi.returnConverter().isPresent()) {
                TypeMirror converter = mi.returnConverter().get().type();
                if (!converters.contains(converter)) {
                    converters.add(converter);
                    buildBuilder.addStatement("CONVERTERS.computeIfAbsent($T.class, c -> new $T())", converter, converter);
                }
            }
        });
    }

    private CodeBlock convert(MethodInfo mi) {
        CodeBlock.Builder builder = findGetMethod(mi).add("(");
        CodeBlock defaultValue = defaultValue(mi);
        if (mi.returnMapKeyInfo().isPresent()) {
            MethodInfo keyInfo = mi.returnMapKeyInfo().get();
            builder.add("$L, ", function(Optional.empty(), keyInfo.returnStringMethod(), keyInfo.returnTypeName()));
            builder.add("$L, ", function(mi.returnConverter(), mi.returnStringMethod(), mi.returnTypeName()));
            return builder.add("$S", mi.key()).add(split(mi)).add(defaultValue).add(")").build();
        }
        builder.add("$L, ", function(mi.returnConverter(), mi.returnStringMethod(), mi.returnTypeName()));
        return builder.add("$S", mi.key()).add(split(mi)).add(defaultValue).add(")").build();
    }

    private CodeBlock.Builder findGetMethod(MethodInfo mi) {
        CodeBlock.Builder builder = CodeBlock.builder().add("$L.", CONFIG_HOLDER);
        if (mi.returnOptional()) {
            return builder.add(mi.returnInterface().map(t -> {
                if (t == List.class) {
                    return "getOptionalList";
                }
                if (t == Set.class) {
                    return "getOptionalSet";
                }
                if (t == Map.class) {
                    return "getOptionalMap";
                }
                if (t == SortedMap.class) {
                    return "getOptionalSortedMap";
                }
                return "getOptionalSortedSet";
            }).orElse("getOptional"));
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

    private CodeBlock function(Optional<ConverterInfo> converterInfo, Optional<MethodInfo.StringMethodInfo> stringMethodInfo,
            TypeName returnTypeName) {
        CodeBlock.Builder builder = CodeBlock.builder();
        if (converterInfo.isPresent()) {
            ConverterInfo ci = converterInfo.get();
            builder.add("s -> convert($T.class, s, $L)", ci.type(), converterParameters(ci.parameters()));
        } else if (stringMethodInfo.isPresent()) {
            MethodInfo.StringMethodInfo smi = stringMethodInfo.get();
            StringMethod sm = smi.stringMethod();
            if (sm == StringMethod.STRING) {
                builder.add("s -> s");
            } else if (sm == StringMethod.CONSTRUCTOR) {
                builder.add("s -> new $T(s)", returnTypeName);
            } else {
                builder.add("$T::$L", smi.methodType(), sm.methodName().get());
            }
        } else if (returnTypeName.equals(TypeName.CHAR)) {
            builder.add("s -> s.charAt(0)");
        } else {
            builder.add("$T::valueOf", returnTypeName.box());
        }
        return builder.build();
    }

    private CodeBlock converterParameters(String[] parameters) {
        if (Arrays.equals(Converter.EMPTY, parameters)) {
            return CodeBlock.builder().add("$T.EMPTY", Converter.class).build();
        }
        CodeBlock.Builder builder = CodeBlock.builder().add("new $T[] {", String.class);
        for (int i = 0; i < parameters.length; i++) {
            if (i != 0) {
                builder.add(", ");
            }
            builder.add("$S", parameters[i]);
        }
        return builder.add("}").build();
    }
}
