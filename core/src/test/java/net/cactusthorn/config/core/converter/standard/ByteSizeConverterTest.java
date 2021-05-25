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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.converter.bytesize.ByteSize;
import net.cactusthorn.config.core.converter.bytesize.ByteSizeUnit;

public class ByteSizeConverterTest {

    private static Converter<ByteSize> converter = new ByteSizeConverter();

    @ParameterizedTest //
    @ValueSource(strings = { "10 bytes", "10byte", "10 byte" }) //
    public void bytes(String value) {
        assertEquals(new ByteSize(10, ByteSizeUnit.BYTES), converter.convert(value));
    }

    @ParameterizedTest //
    @ValueSource(strings = { "10m", "10mi", "10mib" }) //
    public void mebibytes(String value) {
        assertEquals(new ByteSize(10, ByteSizeUnit.MEBIBYTES), converter.convert(value));
    }

    @Test public void invalidUnit() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("10 sillybyte"));
    }

    @Test public void invalidNumber() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert("megabyte"));
    }
}
