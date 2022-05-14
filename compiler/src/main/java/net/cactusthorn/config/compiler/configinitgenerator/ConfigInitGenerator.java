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
import java.util.List;

import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.Generator;
import net.cactusthorn.config.compiler.GeneratorPart;
import net.cactusthorn.config.compiler.InterfaceInfo;
import net.cactusthorn.config.compiler.methodinfo.MethodInfo;
import net.cactusthorn.config.core.util.ConfigInitializer;

public final class ConfigInitGenerator extends Generator {

    private static final List<GeneratorPart> PARTS = Arrays.asList(new UrisPart(), new ConstructorPart(), new InitializePart());

    public ConfigInitGenerator(TypeElement interfaceElement, List<MethodInfo> methodsInfo, InterfaceInfo interfaceInfo) {
        super(interfaceElement, methodsInfo, ConfigInitializer.INITIALIZER_CLASSNAME_PREFIX, interfaceInfo);
    }

    @Override public JavaFile generate() {
        TypeSpec.Builder classBuilder = classBuilder().superclass(ConfigInitializer.class);
        PARTS.forEach(p -> p.addPart(classBuilder, this));
        return JavaFile.builder(packageName(), classBuilder.build()).skipJavaLangImports(true).build();
    }
}
