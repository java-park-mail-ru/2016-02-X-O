package mechanics;

import account.AccountManager;
import account.User;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import util.Context;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by kvukolov on 19.05.16.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        final String sessionId = req.getHttpServletRequest().getSession().getId();
        final AccountManager accountManager = (AccountManager)Context.getInstance().get(AccountManager.class);
        final User user = accountManager.getUserBySession(sessionId);
        if (user == null)
        {
            return null;
        }
        final String username = user.getLogin();
        return new GameWebSocket(username);
    }
}
