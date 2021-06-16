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

import static net.cactusthorn.config.core.Disable.Feature.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import net.cactusthorn.config.core.converter.ConverterClass;

@Config @Prefix("test") public interface TestConfig extends Reloadable {

    @Disable(PREFIX) @Default("ddd") String aaa();

    @Key("string") String str();

    @Disable(PREFIX) Optional<String> ostr();

    Optional<String> ostr1();

    @Default("A") String dstr();

    @Default("B") String dstr2();

    List<String> list();

    Optional<List<String>> olist();

    Optional<List<String>> olist2();

    @Default("A,A") List<String> dlist();

    @Default("B,B") List<String> dlist2();

    Set<String> set();

    Optional<Set<String>> oset();

    Optional<Set<String>> oset2();

    @Default("A,A") Set<String> dset();

    @Default("B,B") Set<String> dset2();

    SortedSet<String> sort();

    Optional<SortedSet<String>> osort();

    Optional<SortedSet<String>> osort2();

    @Default("A,A") SortedSet<String> dsort();

    @Default("B,B") SortedSet<String> dsort2();

    Optional<Duration> duration();

    @ConverterClass(ToTestConverter.class) @Default("default") String testconverter();
}
