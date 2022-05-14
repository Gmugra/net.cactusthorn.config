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

import java.util.StringJoiner;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

public class GenerateToStringPart implements GeneratorPart {

    private static final String BUF_NAME = "buf";

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        MethodSpec.Builder toStringBuilder = MethodSpec.methodBuilder(GENERATE_TO_STRING_METHOD).addModifiers(Modifier.PRIVATE)
                .returns(String.class);
        toStringBuilder.addStatement("$T $L = new $T($S, $S, $S)", StringJoiner.class, BUF_NAME, StringJoiner.class, ", ", "[", "]");
        for (MethodInfo mi : generator.methodsInfo()) {
            toStringBuilder.addStatement("$L.add($S + '$L' + $L.get($S))", BUF_NAME, mi.name(), "=", VALUES_ATTR, mi.key());
        }
        toStringBuilder.addStatement("return $L.toString()", BUF_NAME);
        classBuilder.addMethod(toStringBuilder.build());
    }
}
