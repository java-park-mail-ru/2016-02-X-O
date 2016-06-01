package mechanics;

import account.AccountManager;
import account.User;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import util.Context;

import java.io.IOException;

/**
 * Created by kvukolov on 19.05.16.
 */
@WebSocket
public class GameWebSocket {
    User user;
    private Session session;
    Context context = Context.getInstance();

    public GameWebSocket(String login) {
        final AccountManager accountManager = (AccountManager)Context.getInstance().get(AccountManager.class);
        this.user = accountManager.getUserByLogin(login);
    }

    @OnWebSocketConnect
    public void onOpen(Session remoteSession)
    {
        this.session = remoteSession;
        System.out.println("Web socket connect " + this.user.getLogin());
        final WebSocketService webSocketService = (WebSocketService) context.get(WebSocketService.class);
        webSocketService.addWebSocket(this.user, this);
    }

    @OnWebSocketMessage
    public void onMessage(String data)
    {
        System.out.println("Web socket message " + this.user.getLogin());
        final GameMecahnics gameMecahnics = context.get(GameMecahnics.class);
        gameMecahnics.processMessage(user, data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        final WebSocketService webSocketService = (WebSocketService) context.get(WebSocketService.class);
        webSocketService.removeSocket(user);
    }

    public void close()
    {
        session.close();
    }

    public void send(String message) throws IOException
    {
        session.getRemote().sendString(message);
    }
}
