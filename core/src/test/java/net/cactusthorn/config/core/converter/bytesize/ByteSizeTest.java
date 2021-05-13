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
package net.cactusthorn.config.core.converter.bytesize;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class ByteSizeTest {

    @Test public void basics() {
        assertEquals(1, new ByteSize(1, ByteSizeUnit.BYTES).getBytesAsLong());

        BigInteger siBytes = BigInteger.valueOf(1000);
        BigInteger iecBytes = BigInteger.valueOf(1024);

        for (ByteSizeUnit bsu : ByteSizeUnit.values()) {
            if (bsu == ByteSizeUnit.BYTES) {
                assertEquals(1, new ByteSize(1, bsu).getBytesAsLong());
            } else if (bsu.isIEC()) {
                assertEquals(iecBytes, new ByteSize(1, bsu).getBytes());
                iecBytes = iecBytes.multiply(BigInteger.valueOf(1024));
            } else if (bsu.isSI()) {
                assertEquals(siBytes, new ByteSize(1, bsu).getBytes());
                siBytes = siBytes.multiply(BigInteger.valueOf(1000));
            }
        }
    }

    @Test public void conversion() {
        assertEquals(new ByteSize(0.5, ByteSizeUnit.GIGABYTES),
                new ByteSize(500, ByteSizeUnit.MEGABYTES).convertTo(ByteSizeUnit.GIGABYTES));
        assertEquals(new ByteSize(9.765625, ByteSizeUnit.KIBIBYTES),
                new ByteSize(10, ByteSizeUnit.KILOBYTES).convertTo(ByteSizeUnit.KIBIBYTES));
        assertEquals(new ByteSize(10, ByteSizeUnit.MEGABYTES), new ByteSize(10, ByteSizeUnit.MEGABYTES).convertTo(ByteSizeUnit.MEGABYTES));
        ByteSize bs = new ByteSize(1, ByteSizeUnit.BYTES).convertTo(ByteSizeUnit.ZETTABYTES);
        assertEquals(1, bs.getBytesAsLong());
        assertEquals(new ByteSize(1, ByteSizeUnit.BYTES), bs.convertTo(ByteSizeUnit.BYTES));
    }

    @Test public void equality() {
        assertEquals(new ByteSize(500, ByteSizeUnit.MEGABYTES), new ByteSize(0.5, ByteSizeUnit.GIGABYTES));
        assertEquals(new ByteSize(500, ByteSizeUnit.MEBIBYTES), new ByteSize("0.48828125", ByteSizeUnit.GIBIBYTES));
    }

    @Test public void toStr() {
        assertEquals("0.5 GB", new ByteSize(0.5, ByteSizeUnit.GIGABYTES).toString());
    }

    @Test public void getBytesAsInt() {
        assertEquals(500000000, new ByteSize(0.5, ByteSizeUnit.GIGABYTES).getBytesAsInt());
    }

    @Test public void hash() {
        assertEquals(new ByteSize(0.5, ByteSizeUnit.GIGABYTES).hashCode(), new ByteSize(0.5, ByteSizeUnit.GIGABYTES).hashCode());
    }

    @Test public void bytes() {
        assertEquals("1024 B", new ByteSize(1024).toString());
    }

    @Test public void eqSame() {
        ByteSize bs = new ByteSize(1024);
        assertTrue(bs.equals(bs));
    }

    @Test public void eqNull() {
        ByteSize bs = new ByteSize(1024);
        assertFalse(bs.equals(null));
    }

    @Test public void eqWrongObject() {
        ByteSize bs = new ByteSize(1024);
        assertFalse(bs.equals("wrong"));
    }

    @Test public void parse() {
        ByteSizeUnit unit = ByteSizeUnit.parse("k");
        assertEquals(ByteSizeUnit.KIBIBYTES, unit);
    }

    @Test public void toStringLongForm() {
        ByteSizeUnit unit = ByteSizeUnit.parse("k");
        assertEquals("kibibytes", unit.toStringLongForm());
    }

    @Test public void isSI() {
        ByteSizeUnit unit = ByteSizeUnit.parse("k");
        assertFalse(unit.isSI());
    }
}
