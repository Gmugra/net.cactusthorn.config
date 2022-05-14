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
package net.cactusthorn.config.compiler.methodinfo;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import net.cactusthorn.config.compiler.Annotations;
import net.cactusthorn.config.compiler.InterfaceInfo;
import net.cactusthorn.config.core.Disable;

public final class MethodInfo {

    private final TypeName returnTypeName;
    private final String name;
    private final Set<Disable.Feature> disabledFeatures;
    private final Optional<String> defaultValue;

    private String key;
    private String split;

    private Optional<StringMethodInfo> returnStringMethod;
    private Optional<Type> returnInterface;
    private boolean returnOptional;
    private Optional<ConverterInfo> returnConverter;
    private Optional<MethodInfo> returnMapKeyInfo;

    // @formatter:off
    private MethodInfo(ExecutableElement methodElement,
                        Optional<StringMethodInfo> returnStringMethod,
                        Optional<Type> returnInterface,
                        boolean returnOptional,
                        Optional<ConverterInfo> returnConverter,
                        Optional<MethodInfo> returnMapKeyInfo,
                        InterfaceInfo interfaceInfo) {
        // @formatter:on

        Annotations annotations = new Annotations(methodElement);
        returnTypeName = ClassName.get(methodElement.getReturnType());
        name = methodElement.getSimpleName().toString();
        key = annotations.key().orElse(name);
        disabledFeatures = annotations.disable();
        defaultValue = annotations.defaultValue();

        this.returnStringMethod = returnStringMethod;
        this.returnInterface = returnInterface;
        this.returnOptional = returnOptional;
        this.returnConverter = returnConverter;
        this.returnMapKeyInfo = returnMapKeyInfo;

        if (interfaceInfo != null) {
            if (!disabledFeatures.contains(Disable.Feature.PREFIX)) {
                key = interfaceInfo.prefix() + key;
            }
            split = annotations.split().orElse(interfaceInfo.split());
        }
    }

    public String key() {
        return key;
    }

    public String name() {
        return name;
    }

    public TypeName returnTypeName() {
        return returnTypeName;
    }

    public Optional<Type> returnInterface() {
        return returnInterface;
    }

    public boolean returnOptional() {
        return returnOptional;
    }

    public Optional<StringMethodInfo> returnStringMethod() {
        return returnStringMethod;
    }

    public Optional<ConverterInfo> returnConverter() {
        return returnConverter;
    }

    public Optional<MethodInfo> returnMapKeyInfo() {
        return returnMapKeyInfo;
    }

    public String split() {
        return split;
    }

    public Optional<String> defaultValue() {
        return defaultValue;
    }

    public Set<Disable.Feature> disabledFeatures() {
        return disabledFeatures;
    }

    public static Builder builder(ExecutableElement methodElement) {
        return new Builder(methodElement);
    }

    public static final class Builder {

        private ExecutableElement methodElement;

        private Optional<StringMethodInfo> returnStringMethod = Optional.empty();
        private Optional<Type> returnInterface = Optional.empty();
        private boolean returnOptional = false;
        private Optional<ConverterInfo> returnConverter = Optional.empty();
        private Optional<MethodInfo> returnMapKeyInfo = Optional.empty();
        private InterfaceInfo interfaceInfo;

        private Builder(ExecutableElement methodElement) {
            this.methodElement = methodElement;
        }

        public Builder withStringMethod(StringMethod stringMethod, TypeMirror stringMethodTM) {
            returnStringMethod = Optional.of(new StringMethodInfo(stringMethod, stringMethodTM));
            return this;
        }

        public Builder withInterface(Type interfaceType) {
            returnInterface = Optional.of(interfaceType);
            return this;
        }

        public Builder withOptional() {
            returnOptional = true;
            return this;
        }

        public Builder withConverter(TypeMirror converterType, String[] parameters) {
            returnConverter = Optional.of(new ConverterInfo(converterType, parameters));
            return this;
        }

        public Builder withInterfaceInfo(InterfaceInfo interfaceInfoo) {
            interfaceInfo = interfaceInfoo;
            return this;
        }

        public Builder withMapKey(MethodInfo mapKeyInfo) {
            returnMapKeyInfo = Optional.of(mapKeyInfo);
            return this;
        }

        public MethodInfo build() {
            return new MethodInfo(methodElement, returnStringMethod, returnInterface, returnOptional, returnConverter, returnMapKeyInfo,
                    interfaceInfo);
        }
    }
}
