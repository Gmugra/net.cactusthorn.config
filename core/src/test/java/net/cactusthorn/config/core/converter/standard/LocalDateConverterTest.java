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

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import net.cactusthorn.config.core.converter.Converter;

public class LocalDateConverterTest {

    static final LocalDate DATE = LocalDate.of(2016, 3, 10);
    static final Converter<LocalDate> CONVERTER = new LocalDateConverter();

    @Test public void simple() {
        LocalDate result = CONVERTER.convert("2016-03-10");
        assertEquals(DATE, result);
    }

    @Test public void simpleNullParameters() {
        LocalDate result = CONVERTER.convert("2016-03-10", null);
        assertEquals(DATE, result);
    }

    @Test public void simpleEmptyParameters() {
        LocalDate result = CONVERTER.convert("2016-03-10", new String[0]);
        assertEquals(DATE, result);
    }

    @Test public void complex() {
        LocalDate result = CONVERTER.convert("10.03.2016", new String[] { "yyyy-MM-dd", "dd.MM.yyyy" });
        assertEquals(DATE, result);
    }

    @Test public void singleParam() {
        LocalDate result = CONVERTER.convert("10.03.2016", new String[] { "dd.MM.yyyy" });
        assertEquals(DATE, result);
    }
}
