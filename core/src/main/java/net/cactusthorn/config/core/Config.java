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
 * <h3>Interfaces inheritance</h3>
 *
 * <pre>
 * interface MyRoot {
 *
 *      &#064;Key(rootVal) String value();
 * }
 *
 * &#064;Config
 * interface MyConfig extends MyRoot {
 *
 *      int intValue();
 * }
 * </pre>
 *
 * There is no limit to the number and "depth" of super-interfaces.<br>
 * Interface level annotations (e.g. {@link Prefix}) on super-interfaces will be ignored.
 *
 * <h3>Accessible</h3>
 *
 * {@link Config}-interface can extends (directly or over super-interface) the interface {@link Accessible}.<br>
 * In this case generated class will also get methods for this interface.
 *
 * <h3>Serializable</h3>
 * {@link Config}-interface can extends (directly or over super-interface) {@link java.io.Serializable}.<br>
 * In this case generated class will also get {@code private static final long serialVersionUID = 0L} attribute.<br>
 * <br>
 * However, the interface (as in the example later) can, optionally, contains {@code long serialVersionUID} constant.<br>
 * If the constant is present, the value will be used for the {@code private static final long serialVersionUID} attribute
 * in the generated class.<br>
 * <br>
 * <pre>
 * &#064;Config
 * public interface MyConfig extends java.io.Serializable {
 *
 *      long serialVersionUID = 100L;
 *
 *      String val();
 * }
 * </pre>
 *
 * <h3>Reloadable</h3>
 *
 * {@link Config}-interface can extends (directly or over super-interface) {@link Reloadable}.<br>
 * In this case generated class will also get methods for this interface.<br>
 * This is necessary to do to switch on "Periodical auto reloading" for the generated class.
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
