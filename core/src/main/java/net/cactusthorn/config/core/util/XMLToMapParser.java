package net.cactusthorn.config.core.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XMLToMapParser {

    private static final SAXParserFactory FACTORY;
    static {
        FACTORY = SAXParserFactory.newInstance();
        FACTORY.setValidating(true);
        FACTORY.setNamespaceAware(true);
    }

    private static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";

    public Map<String, String> parse(Reader reader) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = FACTORY.newSAXParser();
        XMLToMapHandler handler = new XMLToMapHandler();
        parser.setProperty(LEXICAL_HANDLER, handler);
        parser.parse(new InputSource(reader), handler);
        return handler.properties();
    }
}
