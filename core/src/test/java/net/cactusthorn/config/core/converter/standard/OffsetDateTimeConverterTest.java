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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class OffsetDateTimeConverterTest {

    private static final OffsetDateTime DATETIME = OffsetDateTime.of(2011, 11, 12, 1, 30, 0, 0, ZoneOffset.of("+02:00"));
    private static final Converter<OffsetDateTime> CONVERTER = new OffsetDateTimeConverter();

    @Test public void simple() {
        OffsetDateTime result = CONVERTER.convert("2011-11-12T01:30:00+02:00");
        assertEquals(DATETIME, result);
    }

    @Test public void simpleEmptyParameters() {
        OffsetDateTime result = CONVERTER.convert("2011-11-12T01:30:00+02:00", new String[0]);
        assertEquals(DATETIME, result);
    }

    @Test public void simpleNullParameter() {
        OffsetDateTime result = CONVERTER.convert("2011-11-12T01:30:00+02:00", null);
        assertEquals(DATETIME, result);
    }

    @Test public void complex() {
        OffsetDateTime result = CONVERTER.convert("12.11.2011T01:30:00+02:00",
                new String[] {"yyyy-MM-dd'T'HH:mm:ssXXX", "dd.MM.yyyy'T'HH:mm:ssXXX"});
        assertEquals(DATETIME, result);
    }

    @Test public void singleParam() {
        OffsetDateTime result = CONVERTER.convert("12.11.2011T01:30:00+02:00", new String[] {"dd.MM.yyyy'T'HH:mm:ssXXX"});
        assertEquals(DATETIME, result);
    }
}
