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

import java.util.Collections;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;

public class AccessiblePart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        if (!generator.interfaceInfo().accessible()) {
            return;
        }
        addKeys(classBuilder);
        addGet(classBuilder);
        addAsMap(classBuilder);
    }

    private void addKeys(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder keysBuilder = MethodSpec.methodBuilder("keys").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(SET_STRING).addStatement("return $T.unmodifiableSet($L.keySet())", Collections.class, VALUES_ATTR);
        classBuilder.addMethod(keysBuilder.build());
    }

    private void addGet(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder keysBuilder = MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(Object.class).addParameter(String.class, "key").addStatement("return $L.get(key)", VALUES_ATTR);
        classBuilder.addMethod(keysBuilder.build());
    }

    private void addAsMap(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder keysBuilder = MethodSpec.methodBuilder("asMap").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class)
                .returns(MAP_STRING_OBJECT).addStatement("return $T.unmodifiableMap($L)", Collections.class, VALUES_ATTR);
        classBuilder.addMethod(keysBuilder.build());
    }
}
