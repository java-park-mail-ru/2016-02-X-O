package resource;

import org.xml.sax.SAXException;
import resource.handlers.Handler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Created by kvukolov on 19.05.16.
 */
public class Builder {
    String file;
    Handler handler;

    public Builder(String file, Handler handler) {
        this.file = file;
        this.handler = handler;
    }

    public Object build() throws SAXException, ParserConfigurationException, IOException {
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        final SAXParser parser = factory.newSAXParser();

        parser.parse(file, handler);
        return handler.getObject();
    }
}
