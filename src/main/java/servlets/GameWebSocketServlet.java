package servlets;

import mechanics.GameWebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Created by kvukolov on 19.05.16.
 */
@WebServlet(name = "GameWebSocketServlet", urlPatterns = {"/game"})
public class GameWebSocketServlet extends WebSocketServlet {
    private static final int IDLE_TIME = 600 * 1000;

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator());
    }
}
