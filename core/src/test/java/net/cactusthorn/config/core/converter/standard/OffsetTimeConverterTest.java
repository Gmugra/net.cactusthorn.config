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

import java.time.OffsetTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

class OffsetTimeConverterTest {

    private static final OffsetTime TIME = OffsetTime.of(1, 30, 0, 0, ZoneOffset.of("+02:00"));
    private static final Converter<OffsetTime> CONVERTER = new OffsetTimeConverter();

    @Test void simple() {
        var result = CONVERTER.convert("01:30:00+02:00");
        assertEquals(TIME, result);
    }

    @Test void simpleEmptyParameters() {
        var result = CONVERTER.convert("01:30:00+02:00", new String[0]);
        assertEquals(TIME, result);
    }

    @Test void simpleNullParameter() {
        var result = CONVERTER.convert("01:30:00+02:00", null);
        assertEquals(TIME, result);
    }

    @Test void complex() {
        var result = CONVERTER.convert("01:30:00+02:00", new String[] {"HH:mm:ssXXX", "HH'-'mm'-'ssXXX"});
        assertEquals(TIME, result);
    }

    @Test void singleParam() {
        var result = CONVERTER.convert("01-30-00+02:00", new String[] {"HH'-'mm'-'ssXXX"});
        assertEquals(TIME, result);
    }
}
