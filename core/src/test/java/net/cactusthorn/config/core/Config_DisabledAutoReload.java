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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.loader.ReloadEvent;
import net.cactusthorn.config.core.loader.ReloadListener;
import net.cactusthorn.config.core.util.ConfigInitializer;

public final class Config_DisabledAutoReload implements DisabledAutoReload {
  private static final List<ReloadListener> LISTENERS = new ArrayList<>();

  private final ConcurrentHashMap<String, Object> VALUES = new ConcurrentHashMap<>();

  private int hashCode;

  private String toString;

  private final ConfigInitializer INITIALIZER;

  public Config_DisabledAutoReload(final Loaders loaders) {
    INITIALIZER = new ConfigInitializer_DisabledAutoReload(loaders);
    VALUES.putAll(INITIALIZER.initialize());
    hashCode = calculate__Hash__Code();
    toString = generate__To__String();
  }

  private int calculate__Hash__Code() {
    return Objects.hash(aaa());
  }

  private String generate__To__String() {
    StringJoiner buf = new StringJoiner(", ", "[", "]");
    buf.add("aaa" + '=' + VALUES.get("aaa"));
    return buf.toString();
  }

  @Override
  public String aaa() {
    return (String)VALUES.get("aaa");
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return toString;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Config_DisabledAutoReload)) return false;
    Config_DisabledAutoReload other = (Config_DisabledAutoReload) o;
    return Objects.equals(aaa(), other.aaa());
  }

  @Override
  public void addReloadListener(ReloadListener listener) {
    LISTENERS.add(listener);
  }

  @Override
  public void reload() {
    Map<String, Object> old = new HashMap<>(VALUES);
    Map<String, Object> reloaded = INITIALIZER.initialize();
    VALUES.entrySet().removeIf(e -> !reloaded.containsKey(e.getKey()));
    VALUES.putAll(reloaded);
    hashCode = calculate__Hash__Code();
    toString = generate__To__String();
    ReloadEvent event = new ReloadEvent(this, old, VALUES);
    LISTENERS.forEach(l -> l.reloadPerformed(event));
  }

  @Override
  public boolean autoReloadable() {
    return false;
  }
}
