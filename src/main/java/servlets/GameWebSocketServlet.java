package servlets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by kvukolov on 19.05.16.
 */
public class GameWebSocketServlet extends WebSocketServlet {
    private static final int IDLE_TIME = 60 * 1000;

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);

    }
}
