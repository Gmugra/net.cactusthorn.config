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

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.util.ConfigInitializer;

final class ConstructorPart implements GeneratorPart {

    @Override public void addPart(TypeSpec.Builder classBuilder, Generator generator) {
        if (generator.interfaceInfo().reloadable()) {
            classBuilder.addField(initializer());
            classBuilder.addMethod(reloadableConstructor(generator));
        } else {
            classBuilder.addMethod(simpleConstructor(generator));
        }
    }

    private MethodSpec simpleConstructor(Generator generator) {
        // @formatter:off
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Loaders.class, "loaders", Modifier.FINAL)
            .addStatement("$L$L initializer = new $L$L(loaders)",
                    ConfigInitializer.INITIALIZER_CLASSNAME_PREFIX,
                    generator.interfaceName().simpleName(),
                    ConfigInitializer.INITIALIZER_CLASSNAME_PREFIX,
                    generator.interfaceName().simpleName()
            )
            .addStatement("$L.putAll(initializer.initialize())", VALUES_ATTR)
            .build();
        // @formatter:on
    }

    public FieldSpec initializer() {
        return FieldSpec.builder(ConfigInitializer.class, INITIALIZER_ATTR, Modifier.PRIVATE, Modifier.FINAL).build();
    }

    private MethodSpec reloadableConstructor(Generator generator) {
        // @formatter:off
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Loaders.class, "loaders", Modifier.FINAL)
            .addStatement("$L = new $L$L(loaders)",
                    INITIALIZER_ATTR,
                    ConfigInitializer.INITIALIZER_CLASSNAME_PREFIX,
                    generator.interfaceName().simpleName()
            )
            .addStatement("$L.putAll($L.initialize())", VALUES_ATTR, INITIALIZER_ATTR)
            .build();
        // @formatter:on
    }
}
