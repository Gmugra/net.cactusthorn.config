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
package net.cactusthorn.config.core.loader;

import java.net.URI;
import java.util.Map;

/**
 * @author Alexei Khatskevich
 */
public interface Loader {

     /**
     * Indicates whether this Loader does accept the URI.
     *
     * @param   uri   the {@link URI} as String
     * @return  {@code true}, if the loader is able to handle the content of the URI.
     */
    boolean accept(URI uri);

     /**
     * Loads content for the given {@link URI uri}
     *
     * @param   uri   the {@link URI} from where to load the properties.
     * @param   classLoader   the {@link ClassLoader}
     * @return  immutable Map, can't be null.
     */
    Map<String, String> load(URI uri, ClassLoader classLoader);

    /**
    * Returns a hash code value for the content of the {@link URI uri}.
    * <p>
    * This method is for auto reloading:
    * <ul>
    * <li>if the content has changed, the method should return the new value
    * <li>if the {@link URI} not support auto reloading, the method should always return same value e.g. {@code 0L}
    * </ul>
    * <p>
    * The default implementation always returns {@code 0L}
    *
    * @param   uri   the {@link URI} from where to load the properties.
    * @param   classLoader   the {@link ClassLoader}
    * @return  a hash code value for the content.
    */
    default long contentHashCode(URI uri, ClassLoader classLoader) {
        return 0L;
    }
}
