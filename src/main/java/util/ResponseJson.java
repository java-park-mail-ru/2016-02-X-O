package util;

import org.json.JSONObject;

import java.util.Iterator;

public class ResponseJson extends JSONObject {
    public void loadError(int code)
    {
        this.clear();
        this.put("error", code);
    }

    public void clear()
    {
        Iterator<String> iterator = this.keys();
        while (iterator.hasNext())
        {
            iterator.next();
            iterator.remove();
        }
    }

}
