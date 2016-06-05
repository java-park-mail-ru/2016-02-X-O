package servlets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Created by kvukolov on 18.02.16.
 */
public final class ServletManager implements Iterable<ServletDefinition> {
    private List<ServletDefinition> definitionList = new ArrayList<>();

    @Override
    public Iterator<ServletDefinition> iterator() {
        return definitionList.iterator();
    }

    public void put(ServletDefinition definition)
    {
        definitionList.add(definition);
    }
}
