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
package net.cactusthorn.config.compiler.methodvalidator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import net.cactusthorn.config.compiler.ProcessorException;

public final class MethodValidatorChain implements MethodValidator {

    private final MethodValidator validator;

    private MethodValidatorChain(MethodValidator validator) {
        this.validator = validator;
    }

    @Override public MethodInfo validate(ExecutableElement methodElement, TypeMirror typeMirror) throws ProcessorException {
        return validator.validate(methodElement, typeMirror);
    }

    public static Builder builder(ProcessingEnvironment processingEnv, Class<? extends MethodValidatorAncestor> clazz) {
        return new Builder(processingEnv, clazz);
    }

    public static final class Builder {

        private final ProcessingEnvironment processingEnv;

        private MethodValidatorAncestor first;
        private MethodValidatorAncestor last;

        private Builder(ProcessingEnvironment processingEnv, Class<? extends MethodValidatorAncestor> clazz) {
            this.processingEnv = processingEnv;
            first = MethodValidatorAncestor.create(processingEnv, clazz);
            last = first;
        }

        public Builder next(Class<? extends MethodValidatorAncestor> clazz) {
            MethodValidatorAncestor validator = MethodValidatorAncestor.create(processingEnv, clazz);
            last.setNext(validator);
            last = validator;
            return this;
        }

        public MethodValidator build() {
            return new MethodValidatorChain(first);
        }
    }
}
