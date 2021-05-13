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
package net.cactusthorn.config.core.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class ApiMessages {

    private static final String BANDLE = ApiMessages.class.getName();
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(BANDLE, Locale.getDefault());

    public enum Key {
        IS_NULL, IS_EMPTY, CANT_LOAD_RESOURCE, VALUE_NOT_FOUND, LOADER_NOT_FOUND, CANT_INVOKE_CONFIGBUILDER, CANT_FIND_CONFIGBUILDER,
        WRONG_SOURCE_PARAM, DURATION_NO_NUMBER, DURATION_WRONG_TIME_UNIT, PERIOD_NO_NUMBER, PERIOD_WRONG_TIME_UNIT, MANIFEST_NOT_FOUND_1,
        MANIFEST_NOT_FOUND_2
    }

    private ApiMessages() {
    }

    public static String msg(Key key, Object argument) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument);
    }

    public static String msg(Key key, Object argument1, Object argument2) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument1, argument2);
    }

    public static String isNull(Object argument) {
        return MessageFormat.format(MESSAGES.getString(Key.IS_NULL.name()), argument);
    }

    public static String isEmpty(Object argument) {
        return MessageFormat.format(MESSAGES.getString(Key.IS_EMPTY.name()), argument);
    }
}
