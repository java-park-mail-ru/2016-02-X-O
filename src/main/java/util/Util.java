package util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/*
 * Created by kvukolov on 17.02.16.
 */
public class Util {
    public static void servletError(int responseStatus, ServerAnswer answer, HttpServletResponse response) throws IOException
    {
        response.setStatus(responseStatus);
        final ResponseJson responseJson = new ResponseJson();
        responseJson.loadError(answer.getValue());
        response.getWriter().println(responseJson);
    }

    public static boolean notNullObjects(Object... params)
    {
        for (Object param: params)
        {
            if (param == null)
            {
                return false;
            }
        }
        return true;
    }

    public static ServerAnswer checkParamersByRegex(RegexCheckedParameter[] parameters)
    {
        final RegexManager regexes = RegexManager.getInstance();

        for (RegexCheckedParameter parameter: parameters)
        {
            final Pattern pattern = regexes.get(parameter.getRegexId());
            if (!(pattern == null) && !pattern.matcher(parameter.getValue()).matches())
            {
                return parameter.getAnswer();
            }
        }

        return ServerAnswer.OK_ANSWER;
    }
}
