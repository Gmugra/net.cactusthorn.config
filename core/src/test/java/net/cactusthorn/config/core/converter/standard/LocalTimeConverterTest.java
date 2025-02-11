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
package net.cactusthorn.config.core.converter.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

class LocalTimeConverterTest {

    private static final LocalTime TIME = LocalTime.of(8, 11, 33);
    private static final Converter<LocalTime> CONVERTER = new LocalTimeConverter();

    @Test void simple() {
        var result = CONVERTER.convert("08:11:33");
        assertEquals(TIME, result);
    }

    @Test void simpleNullParameters() {
        var result = CONVERTER.convert("08:11:33", null);
        assertEquals(TIME, result);
    }

    @Test void simpleEmptyParameters() {
        var result = CONVERTER.convert("08:11:33", new String[0]);
        assertEquals(TIME, result);
    }

    @Test void complex() {
        var result = CONVERTER.convert("08-11-33", new String[] {"HH:mm:ss", "HH'-'mm'-'ss"});
        assertEquals(TIME, result);
    }

    @Test void singleParam() {
        var result = CONVERTER.convert("08:11:33", new String[] {"HH:mm:ss"});
        assertEquals(TIME, result);
    }
}
