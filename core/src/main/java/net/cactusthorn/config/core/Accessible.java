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

import java.util.Map;
import java.util.Set;

/**
 * {@link Config}-interface can extends this interface (directly or over super-interface)<br>
 * In this case generated class will also get methods for this interface<br>
 *
 * @author Alexei Khatskevich
 */
public interface Accessible {

    /**
     *
     * @return Unmodifiable {@link java.util.Set} of processed (after expanding and with prefix) keys
     */
    Set<String> keys();

    /**
     * @param  key full key (with prefix etc.)
     *
     * @return already converted value by key
     */
    Object get(String key);

    /**
     *
     * @return Unmodifiable {@link java.util.Map} with all properties represented by child
     * {@link net.cactusthorn.config.core.Config}-interface.
     */
    Map<String, Object> asMap();
}
