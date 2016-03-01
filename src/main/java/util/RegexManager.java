package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/*
 * Created by kvukolov on 17.02.16.
 */
public class RegexManager {
    private Map<RegexId, Pattern> regexes = new HashMap<>();

    private static RegexManager ourInstance = new RegexManager();
    public static RegexManager getInstance() {
        return ourInstance;
    }
    private RegexManager() {
    }

    public void put(RegexId id, String regex)
    {
        regexes.put(id, Pattern.compile(regex));
    }

    public Pattern get(RegexId id)
    {
        return regexes.get(id);
    }
}
