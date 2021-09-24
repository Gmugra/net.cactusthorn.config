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

import net.cactusthorn.config.core.loader.ReloadListener;

/**
 * {@link Config}-interface can extends this interface (directly or over super-interface)<br>
 * In this case generated class will also get methods for this interface<br>
 *
 * @author Alexei Khatskevich
 */
public interface Reloadable {

    /**
     * This method call reload sources associated with the child {@link Config}-interface<br>
     *
     * FYI: The method always reload not cached sources, even if they not changed.
     */
    void reload();

    /**
     * Indicate if generated class auto-reloadable or not. By default returns {@code true}.<br>
     * If the child {@link Config}-interface annotated with {@link Disable.Feature#AUTO_RELOAD} returns {@code false}.
     *
     * @return true if the generated class is auto-reloadable.
     */
    default boolean autoReloadable() {
        return true;
    }

    /**
     * Add a ReloadListener.
     *
     * @param listener the listener to be added
     */
    void addReloadListener(ReloadListener listener);
}
