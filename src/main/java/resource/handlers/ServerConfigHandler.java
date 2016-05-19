package resource.handlers;

import main.ServerConfig;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import util.RegexId;

/**
 * Created by kvukolov on 19.05.16.
 */
public class ServerConfigHandler extends Handler {
    private static final String PORT_TAG = "port";

    ServerConfig serverConfig = new ServerConfig();
    String element;

    @Override
    public Object getObject() {
        return serverConfig;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start parse server config");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("End parse server config");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.element = qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.element = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.element == null)
        {
            return;
        }

        switch (this.element)
        {
            case PORT_TAG:
                this.serverConfig.setPort(Integer.parseInt(new String(ch, start, length)));
                break;
            default:
                break;
        }
    }
}
