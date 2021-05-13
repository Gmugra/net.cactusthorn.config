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

import java.io.Serializable;
import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.core.Accessible;

public final class InterfaceInfo {

    private final String prefix;
    private final String split;
    private final Optional<Long> serialVersionUID;
    private final boolean accessible;
    private final Annotations.ConfigInfo configInfo;

    InterfaceInfo(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        Annotations a = new Annotations(interfaceTypeElement);
        prefix = a.prefix().map(s -> s + KEY_SEPARATOR).orElse("");
        split = a.split().orElse(DEFAULT_SPLIT);
        configInfo = a.config();
        serialVersionUID = findSerializable(processingEnv, interfaceTypeElement);
        accessible = findAccessible(processingEnv, interfaceTypeElement);
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

    public Annotations.ConfigInfo configInfo() {
        return configInfo;
    }

    private boolean findAccessible(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        TypeMirror accessibleType = processingEnv.getElementUtils().getTypeElement(Accessible.class.getName()).asType();
        return processingEnv.getTypeUtils().isAssignable(interfaceTypeElement.asType(), accessibleType);
    }

    private Optional<Long> findSerializable(ProcessingEnvironment processingEnv, TypeElement interfaceTypeElement) {
        TypeMirror serializableType = processingEnv.getElementUtils().getTypeElement(Serializable.class.getName()).asType();
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
