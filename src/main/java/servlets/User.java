package servlets;

import account.AccountManager;
import util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Created by kvukolov on 29.02.16.
 */
public class User extends HttpServlet {
    Context context = Context.getInstance();
    AccountManager accountManager = (AccountManager) this.context.get(AccountManager.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String id = req.getParameter("id");

        if (!Util.notNullObjects(id))
        {
            Util.servletError(HttpServletResponse.SC_BAD_REQUEST, ServerAnswer.BAD_INPUT_DATA, resp);
            return;
        }

        final RegexCheckedParameter[] regexCheckedParameters = {
                new RegexCheckedParameter(id, RegexId.ID_REGEX, ServerAnswer.BAD_ID)
        };

        final ServerAnswer answer = Util.checkParamersByRegex(regexCheckedParameters);
        if (answer != ServerAnswer.OK_ANSWER)
        {
            Util.servletError(HttpServletResponse.SC_FORBIDDEN, answer, resp);
            return;
        }

        final long numId = Long.parseLong(id);
        final account.User user = accountManager.getUserById(numId);
        if (user == null)
        {
            Util.servletError(HttpServletResponse.SC_UNAUTHORIZED, ServerAnswer.NO_USER, resp);
            return;
        }

        final ResponseJson responseJson = new ResponseJson();
        responseJson.put("id", id);
        responseJson.put("login", user.getLogin());
        responseJson.put("email", user.getEmail());
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String login = req.getParameter("login");
        final String email = req.getParameter("email");
        final String password = req.getParameter("password");

        if (!Util.notNullObjects(login, email, password))
        {
            Util.servletError(HttpServletResponse.SC_BAD_REQUEST, ServerAnswer.BAD_INPUT_DATA, resp);
            return;
        }

        final RegexCheckedParameter[] regexCheckedParameters = {
                new RegexCheckedParameter(login, RegexId.LOGIN_REGEX, ServerAnswer.BAD_LOGIN),
                new RegexCheckedParameter(email, RegexId.EMAIL_REGEX, ServerAnswer.BAD_EMAIL),
                new RegexCheckedParameter(password, RegexId.PASSWORD_REGEX, ServerAnswer.BAD_PASSWORD)
        };

        final ServerAnswer answer = Util.checkParamersByRegex(regexCheckedParameters);
        if (answer != ServerAnswer.OK_ANSWER)
        {
            Util.servletError(HttpServletResponse.SC_FORBIDDEN, answer, resp);
            return;
        }

        final account.User user = accountManager.getUserBySession(req.getSession().getId());
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);
        user.save();

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String login = req.getParameter("login");
        final String email = req.getParameter("email");
        final String password = req.getParameter("password");

        if (!Util.notNullObjects(login, email, password))
        {
            Util.servletError(HttpServletResponse.SC_BAD_REQUEST, ServerAnswer.BAD_INPUT_DATA, resp);
            return;
        }

        final RegexCheckedParameter[] regexCheckedParameters = {
                new RegexCheckedParameter(login, RegexId.LOGIN_REGEX, ServerAnswer.BAD_LOGIN),
                new RegexCheckedParameter(email, RegexId.EMAIL_REGEX, ServerAnswer.BAD_EMAIL),
                new RegexCheckedParameter(password, RegexId.PASSWORD_REGEX, ServerAnswer.BAD_PASSWORD)
        };

        ServerAnswer answer = Util.checkParamersByRegex(regexCheckedParameters);
        if (answer != ServerAnswer.OK_ANSWER)
        {
            Util.servletError(HttpServletResponse.SC_FORBIDDEN, answer, resp);
            return;
        }

        answer = accountManager.addUser(login, email, password);
        if (answer != ServerAnswer.OK_ANSWER)
        {
            Util.servletError(HttpServletResponse.SC_FORBIDDEN, answer, resp);
            return;
        }


        resp.setStatus(HttpServletResponse.SC_OK);
        final ResponseJson responseJson = new ResponseJson();
        responseJson.put("id", accountManager.getUserByLogin(login).getId());
        resp.getWriter().println(responseJson);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String sessionId = req.getSession().getId();
        final account.User user = accountManager.getUserBySession(sessionId);
        accountManager.logout(sessionId);
        accountManager.deleteUser(user.getLogin());
    }
}
