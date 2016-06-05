package resource.handlers;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import util.RegexId;
import util.RegexManager;

/**
 * Created by kvukolov on 19.05.16.
 */
public class RegexManagerHandler extends Handler{
    private RegexManager object = new RegexManager();
    private String element;
    private static final String REGEX_TAG = "regex";
    private RegexId regexId;

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start parse regex manager config");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("End parse regex manager config");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        element = qName;

        switch (this.element)
        {
            case REGEX_TAG:
                regexId = RegexId.valueOf(attributes.getValue("id"));
                break;

            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        element = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.element == null)
        {
            return;
        }
        switch (this.element)
        {
            case REGEX_TAG:
                this.object.put(regexId, new String(ch, start, length));
                break;

            default:
                break;
        }
    }
}
