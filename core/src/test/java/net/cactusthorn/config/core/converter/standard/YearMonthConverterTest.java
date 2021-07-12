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

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class YearMonthConverterTest {

    private static final YearMonth YEAR_MONTH = YearMonth.of(2016, 11);
    private static final Converter<YearMonth> CONVERTER = new YearMonthConverter();

    @Test public void simple() {
        YearMonth result = CONVERTER.convert("2016-11");
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void simpleNullParameters() {
        YearMonth result = CONVERTER.convert("2016-11", null);
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void simpleEmptyParameters() {
        YearMonth result = CONVERTER.convert("2016-11", new String[0]);
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void complex() {
        YearMonth result = CONVERTER.convert("2016:11", new String[] {"yyyy-MM", "yyyy:MM"});
        assertEquals(YEAR_MONTH, result);
    }

    @Test public void singleParam() {
        YearMonth result = CONVERTER.convert("2016:11", new String[] {"yyyy:MM"});
        assertEquals(YEAR_MONTH, result);
    }
}
