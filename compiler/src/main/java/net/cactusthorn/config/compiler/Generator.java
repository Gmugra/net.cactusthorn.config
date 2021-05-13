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

import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import net.cactusthorn.config.compiler.methodvalidator.MethodInfo;

public abstract class Generator {

    private final ClassName interfaceName;
    private final TypeElement interfaceElement;
    private final String packageName;
    private final String className;
    private final List<MethodInfo> methodsInfo;
    private final InterfaceInfo interfaceInfo;

    public Generator(TypeElement interfaceElement, List<MethodInfo> methodsInfo, String classNamePrefix, InterfaceInfo interfaceInfo) {
        interfaceName = ClassName.get(interfaceElement);
        this.interfaceElement = interfaceElement;
        this.packageName = interfaceName.packageName();
        this.className = classNamePrefix + interfaceName.simpleName();
        this.methodsInfo = methodsInfo;
        this.interfaceInfo = interfaceInfo;
    }

    public abstract JavaFile generate();

    public TypeSpec.Builder classBuilder() {
        return TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    public ClassName interfaceName() {
        return interfaceName;
    }

    public TypeElement interfaceElement() {
        return interfaceElement;
    }

    public String packageName() {
        return packageName;
    }

    public String className() {
        return className;
    }

    public List<MethodInfo> methodsInfo() {
        return methodsInfo;
    }

    public InterfaceInfo interfaceInfo() {
        return interfaceInfo;
    }
}
