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

import static net.cactusthorn.config.core.util.ApiMessages.*;
import static net.cactusthorn.config.core.util.ApiMessages.Key.*;

import java.math.BigDecimal;

import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.converter.bytesize.ByteSize;
import net.cactusthorn.config.core.converter.bytesize.ByteSizeUnit;
import net.cactusthorn.config.core.util.NumericAndCharSplitter;

/**
 * @author Stefan Freyr Stefansson, Alexei Khatskevich
 */
public class ByteSizeConverter implements Converter<ByteSize> {

    private static final NumericAndCharSplitter SPLITTER = new NumericAndCharSplitter();

    @Override public ByteSize convert(String input, String[] parameters) {
        String[] parts = SPLITTER.split(input);
        String value = parts[0];
        String unit = parts[1];

        BigDecimal bdValue = new BigDecimal(value);
        ByteSizeUnit bsuUnit = ByteSizeUnit.parse(unit);

        if (bsuUnit == null) {
            throw new IllegalArgumentException(msg(INVALID_UNIT_STRING, unit));
        }

        return new ByteSize(bdValue, bsuUnit);
    }
}
