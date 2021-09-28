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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Set property name associated with the method.<br>
 * If this annotation is not present method-name will be used as property name.
 *
 * <h3>Example</h3>
 *
 * <pre>
 * &#064;Config
 * public interface MyConfiguration {
 *
 *     String value();
 *
 *     &#064;Key("config.super-number")
 *     Optional&lt;Integer&gt; number();
 * }
 * </pre>
 *
 * <h3>System properties and/or environment variables</h3>
 *
 * <pre>
 * &#064;Config
 * public interface MyConfiguration {
 *
 *     //"env" is system-property or environment variable name
 *     &#064;Key("{env}.host") URL host();
 *     &#064;Key("{env}.port") int port();
 * }
 * </pre>
 *
 * This feature makes it possible to store, for example, settings for different environments in a single configuration file.
 * e.g. (<a href="https://toml.io/en/">TOML</a>):<br>
 * <br>
 *
 * <pre>
 * host = "https://www.google.com/"
 * port = 80
 *
 * [dev]
 * host = "https://github.com/"
 * port = 90
 *
 * [prod]
 * host = "https://www.wikipedia.org/"
 * port = 100
 * </pre>
 *
 * <h4>FYI</h4>
 *
 * <ul>
 * <li>If a system property or environment variable does not exist, an "empty string" will be used as the value.
 * <li>After expanding, starting and ending dot-characters "." will be dropped.
 * <li>After expanding, multiple dot-characters (e.g "...") inside the key name will be substituted to single ".".
 * </ul>
 *
 * <br>
 *
 * <table border="1" summary="Expanding Examples">
 *      <tr>
 *          <th>system property value</th><th>key config</th><th>resulting key</th>
 *      </tr>
 *      <tr>
 *          <td>dev</td><td>{env}.host</td><td>dev.host</td>
 *      </tr>
 *      <tr>
 *          <td> </td><td>{env}.host</td><td>host</td>
 *      </tr>
 *      <tr>
 *          <td>dev</td><td>server.{env}.host</td><td>server.dev.host</td>
 *      </tr>
 *      <tr>
 *          <td> </td><td>server.{env}.host</td><td>server.host</td>
 *      </tr>
 *      <tr>
 *          <td>dev</td><td>host.{env}</td><td>host.dev</td>
 *      </tr>
 *      <tr>
 *          <td> </td><td>host.{env}</td><td>host</td>
 *      </tr>
 * </table>
 *
 * @author Alexei Khatskevich
 */
@Documented @Retention(SOURCE) @Target(METHOD) public @interface Key {
    String value();
    char KEY_SEPARATOR = '.';
}
