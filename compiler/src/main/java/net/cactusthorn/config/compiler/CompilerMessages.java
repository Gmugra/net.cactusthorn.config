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
package net.cactusthorn.config.compiler;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class CompilerMessages {

    private static final String BANDLE = CompilerMessages.class.getName();
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(BANDLE, Locale.getDefault());

    public enum Key {
        ONLY_INTERFACE,
        METHOD_MUST_EXIST,
        METHOD_WITHOUT_PARAMETERS,
        RETURN_VOID,
        RETURN_INTERFACES,
        RETURN_ABSTRACT,
        RETURN_INTERFACE_ARG_EMPTY,
        RETURN_INTERFACE_ARG_WILDCARD,
        RETURN_INTERFACE_ARG_INTERFACE,
        RETURN_STRING_CLASS,
        RETURN_OPTIONAL_ARG_EMPTY,
        RETURN_OPTIONAL_ARG_WILDCARD,
        RETURN_OPTIONAL_DEFAULT,
        RETURN_FACTORY_METHOD_CONFIG
    }

    private CompilerMessages() {
    }

    public static String msg(Key key) {
        return MESSAGES.getString(key.name());
    }

    public static String msg(Key key, Object argument) {
        return MessageFormat.format(MESSAGES.getString(key.name()), argument);
    }
}
