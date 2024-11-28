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
package net.cactusthorn.config.compiler.configgenerator;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodinfo.MethodInfo;

final class EqualsPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        // @formatter:off
        var equalsBuilder =
            MethodSpec.methodBuilder("equals")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(boolean.class)
            .addParameter(ParameterSpec.builder(Object.class, "o").build())
            .addStatement("if (o == this) return true")
            .addStatement("if (!(o instanceof $L)) return false", generator.className())
            .addStatement("$L other = ($L) o", generator.className(), generator.className());
        // @formatter:on
        equalsBuilder.addCode(createReturn(generator.methodsInfo()));
        classBuilder.addMethod(equalsBuilder.build());
    }

    private static final byte THREE = (byte) 3;

    private CodeBlock createReturn(List<MethodInfo> methodsInfo) {
        var template = new StringJoiner(" && ", "return ", "");
        var args = new Object[methodsInfo.size() * THREE];
        for (int i = 0; i < methodsInfo.size(); i++) {
            template.add("$T.equals($L(), other.$L())");
            var mi = methodsInfo.get(i);
            args[i * THREE] = Objects.class;
            args[i * THREE + 1] = mi.name();
            args[i * THREE + 2] = mi.name();
        }
        var builder = CodeBlock.builder();
        builder.addStatement(template.toString(), args);
        return builder.build();
    }
}
