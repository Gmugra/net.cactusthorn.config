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
package net.cactusthorn.config.extras.jasypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

class PBEConverterTest {

    private static Converter<String> converter = new PBEConverter();

    @Test void correct() {
        System.setProperty("bpe-pass", "megapass");
        assertEquals("postgres", converter.convert("U79blAyCnFylcjX5wpCl/TVDHmy+MSSw", new String[] {"bpe-pass"}));
    }

    @Test void passNameNull() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("U79blAyCnFylcjX5wpCl/TVDHmy+MSSw", null));
    }

    @Test void passNameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("U79blAyCnFylcjX5wpCl/TVDHmy+MSSw", Converter.EMPTY));
    }

    @Test void passNameZeroSize() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("U79blAyCnFylcjX5wpCl/TVDHmy+MSSw", new String[0]));
    }

}
