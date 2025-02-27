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
package net.cactusthorn.config.compiler;

import static net.cactusthorn.config.core.Key.KEY_SEPARATOR;
import static net.cactusthorn.config.core.Split.DEFAULT_SPLIT;
import static net.cactusthorn.config.core.Disable.Feature.AUTO_RELOAD;
import static net.cactusthorn.config.core.Disable.Feature.GLOBAL_PREFIX;

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

import net.cactusthorn.config.core.Accessible;
import net.cactusthorn.config.core.Reloadable;

public final class InterfaceInfo {

    private final String prefix;
    private final String split;
    private final Optional<Long> serialVersionUID;
    private final boolean accessible;
    private final boolean reloadable;
    private final boolean autoReloadable;
    private final boolean globalPrefix;
    private final Annotations.ConfigInfo configInfo;

    InterfaceInfo(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        var annotations = new Annotations(interfaceTypeElement);
        prefix = annotations.prefix().map(s -> s + KEY_SEPARATOR).orElse("");
        split = annotations.split().orElse(DEFAULT_SPLIT);
        var disable = annotations.disable();
        autoReloadable = !disable.contains(AUTO_RELOAD);
        globalPrefix = !disable.contains(GLOBAL_PREFIX);
        configInfo = annotations.config();
        serialVersionUID = findSerializable(processingEnv, interfaceTypeElement);
        accessible = findInterface(processingEnv, interfaceTypeElement, Accessible.class);
        reloadable = findInterface(processingEnv, interfaceTypeElement, Reloadable.class);
    }

    public String prefix() {
        return prefix;
    }

    public String split() {
        return split;
    }

    public Optional<Long> serialVersionUID() {
        return serialVersionUID;
    }

    public boolean accessible() {
        return accessible;
    }

    public boolean reloadable() {
        return reloadable;
    }

    public boolean autoReloadable() {
        return autoReloadable;
    }

    public boolean globalPrefix() {
        return globalPrefix;
    }

    public Annotations.ConfigInfo configInfo() {
        return configInfo;
    }

    private boolean findInterface(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement, Class<?> interfaceClass) {
        var accessibleType = processingEnv.getElementUtils().getTypeElement(interfaceClass.getName()).asType();
        return processingEnv.getTypeUtils().isAssignable(interfaceTypeElement.asType(), accessibleType);
    }

    private Optional<Long> findSerializable(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        var serializableType = processingEnv.getElementUtils().getTypeElement(Serializable.class.getName()).asType();
        if (processingEnv.getTypeUtils().isAssignable(interfaceTypeElement.asType(), serializableType)) {
            // @formatter:off
            return
                Optional.of(
                    interfaceTypeElement.getEnclosedElements()
                        .stream()
                        .filter(e -> e.getKind() == ElementKind.FIELD)
                        .map(VariableElement.class::cast)
                        .filter(ve -> ve.asType().getKind() == TypeKind.LONG)
                        .filter(ve -> ve.getSimpleName().toString().equals("serialVersionUID"))
                        .map(VariableElement::getConstantValue)
                        .map(Long.class::cast)
                        .findAny()
                        .orElse(0L));
            // @formatter:on
        }
        return Optional.empty();
    }
}
