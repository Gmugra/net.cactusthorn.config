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
package net.cactusthorn.config.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.cactusthorn.config.core.loader.LoadStrategy;

/**
 * The "source" interface must be annotated with this annotation.
 *
 * <h3>Example</h3>
 *
 * <pre>
 * &#064;Config(
 *     sources = {"classpath:config/testconfig2.properties","nocache:system:properties"},
 *     loadStrategy = LoadStrategy.FIRST
 * )
 * public interface MyConfiguration {
 *
 *     String value();
 * }
 * </pre>
 *
 * @author Alexei Khatskevich
 */
@Documented @Target(ElementType.TYPE) @Retention(RetentionPolicy.SOURCE) public @interface Config {

    /**
     * If returns not empty string,
     * all sources added in the {@link net.cactusthorn.config.core.factory.ConfigFactory}
     * (using {@link net.cactusthorn.config.core.factory.ConfigFactory.Builder#addSource} methods) will be ignored.
     *
     * @return an array of source URIs
     */
    String[] sources() default "";

    /**
     * If returns not {@link LoadStrategy#UNKNOWN},
     * it will be used instead of loadStrategy from {@link net.cactusthorn.config.core.factory.ConfigFactory}.
     *
     * @return loading strategy
     */
    LoadStrategy loadStrategy() default LoadStrategy.UNKNOWN;
}
