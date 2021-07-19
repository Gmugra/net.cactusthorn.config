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

import java.util.ArrayList;
import java.util.HashMap;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.core.loader.ReloadEvent;
import net.cactusthorn.config.core.loader.ReloadListener;

public final class ReloadablePart implements GeneratorPart {

    private static final String LISTENERS_ATTR = "LISTENERS";

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        if (!generator.interfaceInfo().reloadable()) {
            return;
        }
        addListeners(classBuilder);
        addReload(classBuilder);
        autoReloadable(classBuilder, generator);
    }

    private void addListeners(TypeSpec.Builder classBuilder) {

        ClassName reloadListner = ClassName.get(ReloadListener.class);
        TypeName listReloadListner = ParameterizedTypeName.get(LIST, reloadListner);

        FieldSpec fieldSpec = FieldSpec.builder(listReloadListner, LISTENERS_ATTR, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T<>()", ArrayList.class).build();
        classBuilder.addField(fieldSpec);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("addReloadListener").addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ReloadListener.class, "listener")
                .addStatement("$L.add(listener)", LISTENERS_ATTR);
        classBuilder.addMethod(builder.build());
    }

    private void addReload(TypeSpec.Builder classBuilder) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("reload").addModifiers(Modifier.PUBLIC).addAnnotation(Override.class);
        builder.addStatement("$T old = new $T<>($L)", MAP_STRING_OBJECT, HashMap.class, VALUES_ATTR);
        builder.addStatement("$T reloaded = $L.initialize()", MAP_STRING_OBJECT, INITIALIZER_ATTR);
        builder.addStatement("$L.entrySet().removeIf(e -> !reloaded.containsKey(e.getKey()))", VALUES_ATTR);
        builder.addStatement("$L.putAll(reloaded)", VALUES_ATTR);
        builder.addStatement("$T event = new $T(this, old, $L)", ReloadEvent.class, ReloadEvent.class, VALUES_ATTR);
        builder.addStatement("$L.forEach(l -> l.reloadPerformed(event))", LISTENERS_ATTR);

        classBuilder.addMethod(builder.build());
    }

    private void autoReloadable(TypeSpec.Builder classBuilder, Generator generator) {
        if (generator.interfaceInfo().autoReloadable()) {
            return;
        }
        MethodSpec.Builder builder = MethodSpec.methodBuilder("autoReloadable").returns(boolean.class).addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class).addStatement("return false");
        classBuilder.addMethod(builder.build());
    }
}
