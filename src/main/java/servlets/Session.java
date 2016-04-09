package servlets;

import account.*;
import util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Created by kvukolov on 29.02.16.
 */
public class Session extends HttpServlet {

    AccountManager accountManager = MainAccountManager.getManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final account.User user = accountManager.getUserBySession(req.getSession().getId());

        resp.setStatus(HttpServletResponse.SC_OK);
        final ResponseJson responseJson = new ResponseJson();
        responseJson.put("id", user.getId());
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        if (!Util.notNullObjects(login, password))
        {
            Util.servletError(HttpServletResponse.SC_BAD_REQUEST, ServerAnswer.BAD_INPUT_DATA, resp);
            return;
        }

        final RegexCheckedParameter[] regexCheckedParameters = {
                new RegexCheckedParameter(login, RegexId.LOGIN_REGEX, ServerAnswer.BAD_LOGIN),
                new RegexCheckedParameter(password, RegexId.PASSWORD_REGEX, ServerAnswer.BAD_PASSWORD)
        };

        ServerAnswer answer = Util.checkParamersByRegex(regexCheckedParameters);
        if (!(answer == ServerAnswer.OK))
        {
            Util.servletError(HttpServletResponse.SC_BAD_REQUEST, answer, resp);
            return;
        }

        answer = accountManager.authenticate(req.getSession().getId(), login, password);
        if (!(answer == ServerAnswer.OK))
        {
            Util.servletError(HttpServletResponse.SC_BAD_REQUEST, answer, resp);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        final ResponseJson responseJson = new ResponseJson();
        responseJson.put("id", accountManager.getUserByLogin(login).getId());
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        accountManager.logout(req.getSession().getId());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
