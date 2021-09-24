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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public final class ConfigProcessor extends AbstractProcessor {

    private static final ConfigClassesGenerator CONFIG_CLASSES_GENERATOR = new ConfigClassesGenerator();
    private static final FactoryClassGenerator FACTORY_CLASS_GENERATOR = new FactoryClassGenerator();

    private static final Set<String> SUPPORTED_ANNOTATIONS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("net.cactusthorn.config.core.Config", "net.cactusthorn.config.core.factory.Factory")));

    @Override public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        CONFIG_CLASSES_GENERATOR.init(processingEnv);
        FACTORY_CLASS_GENERATOR.init(processingEnv);
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            CONFIG_CLASSES_GENERATOR.generate(roundEnv, processingEnv);
            FACTORY_CLASS_GENERATOR.generate(roundEnv, processingEnv);
        } catch (ProcessorException e) {
            if (e.getAnnotationMirror() != null) {
                error(e.getMessage(), e.getElement(), e.getAnnotationMirror());
            } else {
                error(e.getMessage(), e.getElement());
            }
        } catch (IOException e) {
            error("Can't generate source file: " + e.getMessage());
        }
        return true;
    }

    private void error(String msg, Element element, AnnotationMirror annotationMirror) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element, annotationMirror);
    }

    private void error(String msg, Element element) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    private void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
