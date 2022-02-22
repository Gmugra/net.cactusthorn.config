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

import java.time.MonthDay;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class MonthDayConverterTest {

    private static final MonthDay MONTH_DAY = MonthDay.of(1, 15);
    private static final Converter<MonthDay> CONVERTER = new MonthDayConverter();

    @Test public void simple() {
        MonthDay result = CONVERTER.convert("--01-15");
        assertEquals(MONTH_DAY, result);
    }

    @Test public void simpleNullParameters() {
        MonthDay result = CONVERTER.convert("--01-15", null);
        assertEquals(MONTH_DAY, result);
    }

    @Test public void simpleEmptyParameters() {
        MonthDay result = CONVERTER.convert("--01-15", new String[0]);
        assertEquals(MONTH_DAY, result);
    }

    @Test public void complex() {
        MonthDay result = CONVERTER.convert("01|15", new String[] {"'x'MM|dd", "MM|dd"});
        assertEquals(MONTH_DAY, result);
    }

    @Test public void singleParam() {
        MonthDay result = CONVERTER.convert("01|15", new String[] {"MM|dd"});
        assertEquals(MONTH_DAY, result);
    }
}
