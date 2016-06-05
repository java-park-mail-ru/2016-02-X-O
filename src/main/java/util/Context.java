package util;

/*
 * Created by kvukolov on 19.05.16.
 */
import java.util.HashMap;
import java.util.Map;

public final class Context {
    private Map<Class<?>, Object> context = new HashMap<>();
    private static Context instance = new Context();

    public static Context getInstance(){
        return instance;
    }

    private Context(){}

    public void add(Class<?> clazz, Object object)
    {
        if (context.get(clazz) == null)
        {
            context.put(clazz, object);
        }
    }

    public <T> T get(Class<T> clazz)
    {
        //noinspection unchecked
        return (T) context.get(clazz);
    }
}
