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

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

final class EqualsPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        // @formatter:off
        MethodSpec.Builder equalsBuilder =
            MethodSpec.methodBuilder("equals")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .returns(boolean.class)
            .addParameter(ParameterSpec.builder(Object.class, "o").build())
            .addStatement("if (o == this) return true")
            .addStatement("if (!(o instanceof $L)) return false", generator.className())
            .addStatement("$L other = ($L) o", generator.className(), generator.className());
        // @formatter:on
        List<MethodInfo> methodsInfo = generator.methodsInfo();
        for (int i = 0; i < methodsInfo.size() - 1; i++) {
            equalsBuilder.addStatement(createIf(methodsInfo.get(i)));
        }
        equalsBuilder.addStatement(createFinalReturn(methodsInfo.get(methodsInfo.size() - 1)));
        classBuilder.addMethod(equalsBuilder.build());
    }

    private CodeBlock createIf(MethodInfo mi) {
        CodeBlock.Builder builder = CodeBlock.builder();
        if (mi.returnTypeName().isPrimitive()) {
            return builder.add("if (this.$L() != other.$L()) return false", mi.name(), mi.name()).build();
        }
        return builder.add("if (!this.$L().equals(other.$L())) return false", mi.name(), mi.name()).build();
    }

    private CodeBlock createFinalReturn(MethodInfo mi) {
        CodeBlock.Builder builder = CodeBlock.builder();
        if (mi.returnTypeName().isPrimitive()) {
            return builder.add("return this.$L() == other.$L()", mi.name(), mi.name()).build();
        }
        return builder.add("return this.$L().equals(other.$L())", mi.name(), mi.name()).build();
    }
}
