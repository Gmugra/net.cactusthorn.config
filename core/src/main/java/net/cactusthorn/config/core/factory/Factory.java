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
package net.cactusthorn.config.core.factory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * There is one place where Java-reflection is used: {@link ConfigFactory#create(Class)} method.<br>
 * This annotation provides the ability to generate Factory-class(es), which helps to avoid reflection completely.
 *
 * <h3>Example</h3>
 *
 * <pre>
 * &#064;Config
 * public interface MyConfig {
 *     String val();
 * }
 * </pre>
 * <pre>
 * &#064;Factory
 * public interface MyFactory {
 *     MyConfig createMyConfig();
 * }
 * </pre>
 * Based on the "MyFactory"-interface annotated by @Factory,
 * the class "Factory_MyFactory" will be generated, which has same API with {@link ConfigFactory}
 * but instead of {@link ConfigFactory#create(Class)}-method it provides "create"-methods for the interface annotated by @Factory.
 *
 * <h3>Usage example</h3>
 *
 * <pre>
 * MyConfig myConfig = Factory_MyFactory.builder().addSource("file:./myconfig.properties").build().createMyConfig();
 * </pre>
 *
 * <h3>FYI</h3>
 *
 * <ul>
 * <li>the generated class name begins with the prefix "Factory_" and ends with the interface name
 * <li>an interface annotated by @Factory must contains at least one method
 * <li>an interface annotated by @Factory must contains only methods without parameters
 * <li>all methods of an interface annotated by @Factory must return only types annotated by {@link net.cactusthorn.config.core.Config}
 * </ul>
 *
 * @author Alexei Khatskevich
 */
@Documented @Target(ElementType.TYPE) @Retention(RetentionPolicy.SOURCE) public @interface Factory {
}
