package javax.servlet.http;

import account.AccountManager;
import account.MainAccountManager;
import util.ServerAnswer;
import util.Util;

import javax.servlet.ServletException;
import java.io.IOException;

/*
 * Created by kvukolov on 29.02.16.
 */
public class LoginRequiredPut extends HttpServletDecorator {
    AccountManager accountService = MainAccountManager.getManager();

    public LoginRequiredPut(HttpServlet servlet) {
        super(servlet);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!accountService.isAuthenticated(req.getSession().getId()))
        {
            Util.servletError(HttpServletResponse.SC_UNAUTHORIZED, ServerAnswer.LOGIN_REQUIRED, resp);
            return;
        }
        super.doPut(req, resp);
    }
}
