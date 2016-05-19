package resource.handlers;


import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by kvukolov on 19.05.16.
 */
public abstract class Handler extends DefaultHandler {
    public abstract Object getObject();
}
