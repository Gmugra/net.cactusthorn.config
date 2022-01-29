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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.util.ConfigInitializer;

@Generated(
    value = "net.cactusthorn.config.compiler.ConfigProcessor",
    comments = "https://github.com/Gmugra/net.cactusthorn.config"
)
public final class ConfigInitializer_DisabledAutoReload extends ConfigInitializer {
  private static final String[] URIS = new String[] {""};

  ConfigInitializer_DisabledAutoReload(final Loaders loaders) {
    super(loaders);
  }

  @Override
  public Map<String, Object> initialize() {
    ConfigHolder ch = loaders().load(Config_DisabledAutoReload.class.getClassLoader(), LoadStrategy.UNKNOWN, URIS);
    Map<String,Object> values = new HashMap<>();
    values.put("aaa", ch.get(s -> s, expandKey(globalPrefix("aaa"))));
    return values;
  }
}
