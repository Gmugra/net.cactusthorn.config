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

import java.util.Collections;
import java.util.EventObject;
import java.util.Map;

/**
 * A event which indicates that a reload occurred.
 *
 * @author Alexei Khatskevich
 * @see ReloadListener
 */
public class ReloadEvent extends EventObject {

    private static final long serialVersionUID = 0L;

    private final Map<String, Object> oldProperties;
    private final Map<String, Object> newProperties;

    /**
     * Constructs a Event.
     *
     * @param source        The object on which the Event initially occurred.
     * @param oldProperties the properties before the reload.
     * @param newProperties the properties after the reload.
     * @throws IllegalArgumentException if source is null.
     */
    public ReloadEvent(Object source, Map<String, Object> oldProperties, Map<String, Object> newProperties) {
        super(source);
        this.oldProperties = Collections.unmodifiableMap(oldProperties);
        this.newProperties = Collections.unmodifiableMap(newProperties);
    }

    /**
     * Returns unmodifiable map with properties before the reload.
     *
     * @return the properties before the reload.
     */
    public Map<String, Object> oldProperties() {
        return oldProperties;
    }

    /**
     * Returns unmodifiable map with properties after the reload.
     *
     * @return the properties after the reload.
     */
    public Map<String, Object> newProperties() {
        return newProperties;
    }
}
