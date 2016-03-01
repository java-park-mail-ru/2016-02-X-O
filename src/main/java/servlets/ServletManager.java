package servlets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Created by kvukolov on 18.02.16.
 */
public class ServletManager implements Iterable<ServletDefinition> {
    private List<ServletDefinition> definitionList = new ArrayList<>();

    private static ServletManager ourInstance = new ServletManager();
    public static ServletManager getManager() {
        return ourInstance;
    }
    private ServletManager() {
    }

    @Override
    public Iterator<ServletDefinition> iterator() {
        return definitionList.iterator();
    }

    public void put(ServletDefinition definition)
    {
        definitionList.add(definition);
    }
}
