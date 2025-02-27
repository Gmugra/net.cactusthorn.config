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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * This class is mainly based on OWNER implementation:
 * https://github.com/lviggiano/owner/blob/master/owner/src/main/java/org/aeonbits/owner/loaders/XMLLoader.java
 *
 * @author Luigi R. Viggiano, Alexei Khatskevich
 */
public final class XMLToMapHandler extends DefaultHandler2 {

    private static final String PROPS_DTD_URI = "http://java.sun.com/dtd/properties.dtd";
    private static final String PROPS_DTD;
    static {
        try (var is = XMLToMapHandler.class.getClassLoader().getResourceAsStream("properties.dtd");
             var reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            PROPS_DTD = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean isJavaPropertiesFormat = false;
    private final Deque<String> paths = new ArrayDeque<String>();
    private final Deque<StringBuilder> value = new ArrayDeque<StringBuilder>();

    private final Map<String, String> properties = new HashMap<>();

    @Override public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        if (!systemId.equals(PROPS_DTD_URI)) {
            return null;
        }
        isJavaPropertiesFormat = true;
        var inputSource = new InputSource(new StringReader(PROPS_DTD));
        inputSource.setSystemId(PROPS_DTD_URI);
        return inputSource;
    }

    private static final String ENTRY = "entry";
    private static final String KEY = "key";

    @Override public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        value.addFirst(new StringBuilder());

        if (isJavaPropertiesFormat) {
            if (ENTRY.equals(qName)) {
                paths.addFirst(attributes.getValue(KEY));
            } else {
                paths.addFirst(qName);
            }
        } else {
            var path = paths.size() == 0 ? qName : paths.peekFirst() + '.' + qName;
            paths.addFirst(path);
            for (int i = 0; i < attributes.getLength(); i++) {
                var attrName = attributes.getQName(i);
                var attrValue = attributes.getValue(i);
                properties.put(path + '.' + attrName, attrValue);
            }
        }
    }

    @Override public void characters(char[] ch, int start, int length) throws SAXException {
        value.peekFirst().append(new String(ch, start, length));
    }

    private static final String COMMENT = "comment";

    @Override public void endElement(String uri, String localName, String qName) throws SAXException {
        var key = paths.peekFirst();
        var propertyValue = this.value.peekFirst().toString().trim();
        if (!propertyValue.isEmpty() && !(isJavaPropertiesFormat && COMMENT.equals(key))) {
            properties.put(key, propertyValue);
        }
        value.removeFirst();
        paths.removeFirst();
    }

    @Override public void error(SAXParseException e) throws SAXException {
        if (isJavaPropertiesFormat) {
            throw e;
        }
    }

    public Map<String, String> properties() {
        return properties;
    }
}
