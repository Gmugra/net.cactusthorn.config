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
package net.cactusthorn.config.tests.myconfig;

import static net.cactusthorn.config.core.Disable.Feature.PREFIX;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;

import net.cactusthorn.config.core.Config;
import net.cactusthorn.config.core.Default;
import net.cactusthorn.config.core.Disable;
import net.cactusthorn.config.core.Key;
import net.cactusthorn.config.core.Prefix;
import net.cactusthorn.config.core.Split;
import net.cactusthorn.config.core.converter.LocalDateParser;

@Config @Prefix("app") public interface MyConfig {

    @Default("unknown") String val();

    @Key("number") int intVal();

    URI uri();

    @Disable(PREFIX) Optional<List<UUID>> ids();

    @Split("[,:;]") @Default("DAYS:HOURS") Set<TimeUnit> units();

    @LocalDateParser({ "dd.MM.yyyy", "yyyy-MM-dd" }) LocalDate date();
}
