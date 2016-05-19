package resource.handlers;

import org.hibernate.metamodel.source.annotations.ReflectionHelper;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import servlets.ServletDefinition;
import servlets.ServletManager;
import javax.servlet.http.HttpServlet;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by kvukolov on 19.05.16.
 */
public class ServletManagerHandler extends Handler {
    private static final String SERVLET_TAG = "servlet";
    private static final String DECORATOR_TAG = "decorator";

    private ServletManager servletManager = new ServletManager();
    private ServletDefinition servletDefinition;
    private String element;

    @Override
    public Object getObject() {
        return servletManager;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start parse servletManager config");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("End parse servletManager config");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName)
        {
            case SERVLET_TAG:
                final String className = attributes.getValue("class");
                final String url = attributes.getValue("url");
                try{
                    final HttpServlet servlet = (HttpServlet)Class.forName(className).newInstance();
                    servletDefinition = new ServletDefinition(servlet, url);
                }
                catch (ClassNotFoundException | IllegalAccessException | InstantiationException e)
                {
                    e.printStackTrace();
                }
                break;

            case DECORATOR_TAG:
                final String decoratorClassName = attributes.getValue("class");
                try{
                    final HttpServlet decorator = (HttpServlet)Class.forName(decoratorClassName).getConstructor(HttpServlet.class).newInstance(servletDefinition.getServlet());
                    servletDefinition.setServlet(decorator);
                }
                catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName)
        {
            case SERVLET_TAG:
                servletManager.put(servletDefinition);
                break;

            default:
                break;
        }
    }
}
