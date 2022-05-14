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

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.loader.ReloadEvent;
import net.cactusthorn.config.core.loader.ReloadListener;
import net.cactusthorn.config.core.util.ConfigInitializer;

public final class Config_TestConfig implements TestConfig {
  private static final List<ReloadListener> LISTENERS = new ArrayList<>();

  private final ConcurrentHashMap<String, Object> VALUES = new ConcurrentHashMap<>();

  private int hashCode;

  private String toString;

  private final ConfigInitializer INITIALIZER;

  public Config_TestConfig(final Loaders loaders) {
    INITIALIZER = new ConfigInitializer_TestConfig(loaders);
    VALUES.putAll(INITIALIZER.initialize());
    hashCode = calculate__Hash__Code();
    toString = generate__To__String();
  }

  private int calculate__Hash__Code() {
    return Objects.hash(aaa(), dlist(), dlist2(), dset(), dset2(), dsort(), dsort2(), dstr(), dstr2(), duration(), list(), olist(), olist2(), oset(), oset2(), osort(), osort2(), ostr(), ostr1(), set(), sort(), str(), testconverter());
  }

  private String generate__To__String() {
    StringJoiner buf = new StringJoiner(", ", "[", "]");
    buf.add("aaa" + '=' + VALUES.get("aaa"));
    buf.add("dlist" + '=' + VALUES.get("test.dlist"));
    buf.add("dlist2" + '=' + VALUES.get("test.dlist2"));
    buf.add("dset" + '=' + VALUES.get("test.dset"));
    buf.add("dset2" + '=' + VALUES.get("test.dset2"));
    buf.add("dsort" + '=' + VALUES.get("test.dsort"));
    buf.add("dsort2" + '=' + VALUES.get("test.dsort2"));
    buf.add("dstr" + '=' + VALUES.get("test.dstr"));
    buf.add("dstr2" + '=' + VALUES.get("test.dstr2"));
    buf.add("duration" + '=' + VALUES.get("test.duration"));
    buf.add("list" + '=' + VALUES.get("test.list"));
    buf.add("olist" + '=' + VALUES.get("test.olist"));
    buf.add("olist2" + '=' + VALUES.get("test.olist2"));
    buf.add("oset" + '=' + VALUES.get("test.oset"));
    buf.add("oset2" + '=' + VALUES.get("test.oset2"));
    buf.add("osort" + '=' + VALUES.get("test.osort"));
    buf.add("osort2" + '=' + VALUES.get("test.osort2"));
    buf.add("ostr" + '=' + VALUES.get("ostr"));
    buf.add("ostr1" + '=' + VALUES.get("test.ostr1"));
    buf.add("set" + '=' + VALUES.get("test.set"));
    buf.add("sort" + '=' + VALUES.get("test.sort"));
    buf.add("str" + '=' + VALUES.get("test.string"));
    buf.add("testconverter" + '=' + VALUES.get("test.testconverter"));
    return buf.toString();
  }

  @Override
  public String aaa() {
    return (String)VALUES.get("aaa");
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<String> dlist() {
    return (List<String>)VALUES.get("test.dlist");
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<String> dlist2() {
    return (List<String>)VALUES.get("test.dlist2");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<String> dset() {
    return (Set<String>)VALUES.get("test.dset");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<String> dset2() {
    return (Set<String>)VALUES.get("test.dset2");
  }

  @Override
  @SuppressWarnings("unchecked")
  public SortedSet<String> dsort() {
    return (SortedSet<String>)VALUES.get("test.dsort");
  }

  @Override
  @SuppressWarnings("unchecked")
  public SortedSet<String> dsort2() {
    return (SortedSet<String>)VALUES.get("test.dsort2");
  }

  @Override
  public String dstr() {
    return (String)VALUES.get("test.dstr");
  }

  @Override
  public String dstr2() {
    return (String)VALUES.get("test.dstr2");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<Duration> duration() {
    return (Optional<Duration>)VALUES.get("test.duration");
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<String> list() {
    return (List<String>)VALUES.get("test.list");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<List<String>> olist() {
    return (Optional<List<String>>)VALUES.get("test.olist");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<List<String>> olist2() {
    return (Optional<List<String>>)VALUES.get("test.olist2");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<Set<String>> oset() {
    return (Optional<Set<String>>)VALUES.get("test.oset");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<Set<String>> oset2() {
    return (Optional<Set<String>>)VALUES.get("test.oset2");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<SortedSet<String>> osort() {
    return (Optional<SortedSet<String>>)VALUES.get("test.osort");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<SortedSet<String>> osort2() {
    return (Optional<SortedSet<String>>)VALUES.get("test.osort2");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<String> ostr() {
    return (Optional<String>)VALUES.get("ostr");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<String> ostr1() {
    return (Optional<String>)VALUES.get("test.ostr1");
  }

  @Override
  @SuppressWarnings("unchecked")
  public Set<String> set() {
    return (Set<String>)VALUES.get("test.set");
  }

  @Override
  @SuppressWarnings("unchecked")
  public SortedSet<String> sort() {
    return (SortedSet<String>)VALUES.get("test.sort");
  }

  @Override
  public String str() {
    return (String)VALUES.get("test.string");
  }

  @Override
  public String testconverter() {
    return (String)VALUES.get("test.testconverter");
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
    if (!(o instanceof Config_TestConfig)) return false;
    Config_TestConfig other = (Config_TestConfig) o;
    return Objects.equals(aaa(), other.aaa()) && Objects.equals(dlist(), other.dlist()) && Objects.equals(dlist2(), other.dlist2()) && Objects.equals(dset(), other.dset()) && Objects.equals(dset2(), other.dset2()) && Objects.equals(dsort(), other.dsort()) && Objects.equals(dsort2(), other.dsort2()) && Objects.equals(dstr(), other.dstr()) && Objects.equals(dstr2(), other.dstr2()) && Objects.equals(duration(), other.duration()) && Objects.equals(list(), other.list()) && Objects.equals(olist(), other.olist()) && Objects.equals(olist2(), other.olist2()) && Objects.equals(oset(), other.oset()) && Objects.equals(oset2(), other.oset2()) && Objects.equals(osort(), other.osort()) && Objects.equals(osort2(), other.osort2()) && Objects.equals(ostr(), other.ostr()) && Objects.equals(ostr1(), other.ostr1()) && Objects.equals(set(), other.set()) && Objects.equals(sort(), other.sort()) && Objects.equals(str(), other.str()) && Objects.equals(testconverter(), other.testconverter());
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
}
