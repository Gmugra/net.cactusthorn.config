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

import com.squareup.javapoet.AnnotationSpec;

public final class GeneratedAnnotation {

    private static final AnnotationSpec GENERATED;

    static {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("javax.annotation.processing.Generated"); //Java 9
        } catch (ClassNotFoundException e) {
            try {
                clazz = Class.forName("javax.annotation.Generated"); //Java 8
            } catch (ClassNotFoundException e1) {
                throw new IllegalStateException(e);
            }
        }
        GENERATED = AnnotationSpec.builder(clazz).addMember("value", "$S", "net.cactusthorn.config.compiler.ConfigProcessor")
                .addMember("comments", "$S", "https://github.com/Gmugra/net.cactusthorn.config").build();
    }

    public static AnnotationSpec annotationSpec() {
        return GENERATED;
    }
}
