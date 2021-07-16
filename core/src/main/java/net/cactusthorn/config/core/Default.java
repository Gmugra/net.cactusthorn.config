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
 * Set default value (if property will not found in sources, the default value will be used).
 * <p>
 * There are three ways for dealing with properties that are not found in sources
 * <ul>
 * <li>If the method return type is not {@link java.util.Optional} and the method do not annotated with @Default,
 * the {@link net.cactusthorn.config.core.factory.ConfigFactory#create(Class)} method will throw runtime exception "property ... not found"
 * <li>If the method return type is {@link java.util.Optional}:  method will return {@link java.util.Optional#empty()}
 * <li>If the method return type is not {@link java.util.Optional}, but the method do annotated with @Default:
 * method will return converted to return type default value.
 * </ul>
 *
 * <h3>Warning</h3>
 *
 * The @Default annotation can't be used with a method that returns {@link java.util.Optional}.
 *
 * <h3>Example</h3>
 *
 * <pre>
 * &#064;Config
 * public interface MyConfiguration {
 *
 *     &#064;Default("my default value")
 *     String value();
 *
 *     Optional&lt;Integer&gt; number(); //can't be annotated with &#064;Default
 * }
 * </pre>
 *
 * @author Alexei Khatskevich
 */
@Documented @Retention(SOURCE) @Target(METHOD) public @interface Default {
    String value();
}
