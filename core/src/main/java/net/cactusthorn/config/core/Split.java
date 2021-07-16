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
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Set splitter {@link java.util.regex.Pattern regular-expression} or single character for splitting value for collections,<br>
 * or key+value "entries" for maps.<br>
 * <br>
 * If this annotation is not present, default "splitter" is <b>,</b> (comma).
 *
 * <h3>Examples</h3>
 *
 * <pre>
 * &#064;Config
 * public interface MyConfiguration {
 *
 *     &#064;Split("[,:;]")
 *     &#064;Default("DAYS:HOURS")
 *     Set&lt;TimeUnit&gt; units();
 *
 *     &#064;Split(";")
 *     &#064;Default("10000|10;20000|20") //as key-value separator can be used only | (pipe character)
 *     Optional&lt;Map&lt;Integer, Byte&gt;&gt; theMap();
 * }
 * </pre>
 *
 * <pre>
 * &#064;Config
 * &#064;Split(";")
 * public interface MyConfiguration {
 *
 *     &#064;Default("DAYS;HOURS")
 *     Set&lt;TimeUnit&gt; units();
 *
 *     &#064;Default("10000|10;20000|20") //as key-value separator can be used only | (pipe character)
 *     Optional&lt;Map&lt;Integer, Byte&gt;&gt; theMap();
 * }
 * </pre>
 *
 * @author Alexei Khatskevich
 */
@Documented @Retention(SOURCE) @Target({TYPE, METHOD}) public @interface Split {
    String value();

    String DEFAULT_SPLIT = ",";
}
