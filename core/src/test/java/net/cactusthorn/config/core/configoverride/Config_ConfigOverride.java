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
package net.cactusthorn.config.core.configoverride;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.cactusthorn.config.core.loader.Loaders;

public final class Config_ConfigOverride implements ConfigOverride {
  private final ConcurrentHashMap<String, Object> VALUES = new ConcurrentHashMap<>();

  public Config_ConfigOverride(final Loaders loaders) {
    var initializer = new ConfigInitializer_ConfigOverride(loaders);
    VALUES.putAll(initializer.initialize());
  }

  @Override
  public String string() {
    return (String)VALUES.get("test.string");
  }

  @Override
  public int hashCode() {
    return Objects.hash(string());
  }

  @Override
  public String toString() {
    var buf = new StringBuilder();
    buf.append('[');
    buf.append("string").append('=').append(String.valueOf(VALUES.get("test.string")));
    buf.append(']');
    return buf.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Config_ConfigOverride)) return false;
    var other = (Config_ConfigOverride) o;
    return this.string().equals(other.string());
  }
}
