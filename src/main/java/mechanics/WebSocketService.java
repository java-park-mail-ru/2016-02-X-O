package mechanics;

import account.User;
import util.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kvukolov on 25.05.16.
 */
public class WebSocketService {
    private Map<User, GameWebSocket> userGameWebSocketMap = new HashMap<>();

    public void addWebSocket(User user, GameWebSocket webSocket)
    {
        if (userGameWebSocketMap.containsKey(user))
        {
            return;
        }
        userGameWebSocketMap.put(user, webSocket);
        final GameMecahnics gameMecahnics = (GameMecahnics) Context.getInstance().get(GameMecahnics.class);
        gameMecahnics.newConnection(user);
    }

    public GameWebSocket getSocketByUser(User user)
    {
        return userGameWebSocketMap.get(user);
    }

    public void removeSocket(User user)
    {
        userGameWebSocketMap.remove(user);
        final GameMecahnics gameMecahnics = (GameMecahnics) Context.getInstance().get(GameMecahnics.class);
        gameMecahnics.disconnect(user);
    }

    public void closeSocket(User user){
        final GameWebSocket socket = userGameWebSocketMap.get(user);
        if (socket != null)
        {
            socket.close();
        }
    }
}
