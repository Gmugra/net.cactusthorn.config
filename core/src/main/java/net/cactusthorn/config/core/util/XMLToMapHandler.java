package net.cactusthorn.config.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
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
        PROPS_DTD = new BufferedReader(new InputStreamReader(XMLToMapHandler.class.getClassLoader().getResourceAsStream("properties.dtd")))
                .lines().collect(Collectors.joining("\n"));
    }

    private boolean isJavaPropertiesFormat = false;
    private final Stack<String> paths = new Stack<String>();
    private final Stack<StringBuilder> value = new Stack<StringBuilder>();

    private final Map<String, String> properties = new HashMap<>();

    @Override public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
            throws SAXException, IOException {
        InputSource inputSource = null;
        if (systemId.equals(PROPS_DTD_URI)) {
            isJavaPropertiesFormat = true;
            inputSource = new InputSource(new StringReader(PROPS_DTD));
            inputSource.setSystemId(PROPS_DTD_URI);
        }
        return inputSource;
    }

    private static final String ENTRY = "entry";
    private static final String KEY = "key";

    @Override public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        value.push(new StringBuilder());

        if (isJavaPropertiesFormat) {
            if (ENTRY.equals(qName)) {
                paths.push(attributes.getValue(KEY));
            } else {
                paths.push(qName);
            }
        } else {
            String path = paths.size() == 0 ? qName : paths.peek() + '.' + qName;
            paths.push(path);
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrName = attributes.getQName(i);
                String attrValue = attributes.getValue(i);
                properties.put(path + '.' + attrName, attrValue);
            }
        }
    }

    @Override public void characters(char[] ch, int start, int length) throws SAXException {
        value.peek().append(new String(ch, start, length));
    }

    private static final String COMMENT = "comment";

    @Override public void endElement(String uri, String localName, String qName) throws SAXException {
        String key = paths.peek();
        String propertyValue = this.value.peek().toString().trim();
        if (!propertyValue.isEmpty() && !(isJavaPropertiesFormat && COMMENT.equals(key))) {
            properties.put(key, propertyValue);
        }
        value.pop();
        paths.pop();
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
