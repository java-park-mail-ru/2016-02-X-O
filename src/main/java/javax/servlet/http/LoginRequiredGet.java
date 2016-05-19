package javax.servlet.http;

import account.AccountManager;
import util.Context;
import util.ServerAnswer;
import util.Util;

import javax.servlet.ServletException;
import java.io.IOException;

/*
 * Created by kvukolov on 17.02.16.
 */
public class LoginRequiredGet extends HttpServletDecorator
{
    Context context = Context.getInstance();
    AccountManager accountService = (AccountManager) this.context.get(AccountManager.class);

    public LoginRequiredGet(HttpServlet servlet) {
        super(servlet);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!accountService.isAuthenticated(req.getSession().getId()))
        {
            Util.servletError(HttpServletResponse.SC_UNAUTHORIZED, ServerAnswer.LOGIN_REQUIRED, resp);
            return;
        }
        super.doGet(req, resp);
    }
}
