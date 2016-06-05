package resource.handlers;

import database.DBService;
import org.hibernate.cfg.Configuration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Created by kvukolov on 19.05.16.
 */
public class DBServiceHandler extends Handler {
    private static final String PROPERTY_TAG = "property";

    DBService dbService;
    Configuration configuration = new Configuration();

    @Override
    public Object getObject() {
        return dbService;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start parse DBService config");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("End parse DBService config");
        dbService = new DBService(configuration);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName)
        {
            case PROPERTY_TAG:
                configuration.setProperty(attributes.getValue("name"), attributes.getValue("value"));
                break;
            default:
                break;
        }
    }
}
