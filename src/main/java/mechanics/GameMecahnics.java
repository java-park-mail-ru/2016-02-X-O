package mechanics;

import account.User;
import org.json.JSONException;
import org.json.JSONObject;
import util.*;

import java.util.*;
import java.util.function.ObjDoubleConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kvukolov on 25.05.16.
 */
public class GameMecahnics {
    private List<User> waitList = new ArrayList<>();
    private Map<User, GameSession> gameMap = new HashMap<>();

    public void newConnection(User user){
        if (waitList.contains(user) || gameMap.containsKey(user))
        {
            return;
        }
        waitList.add(user);
        if (waitList.size() >= 2)
        {
            final User firstPlayer = waitList.get(0);
            final User secondPlayer = waitList.get(1);

            final GameSession session = new GameSession(firstPlayer, secondPlayer);
            gameMap.put(firstPlayer, session);
            gameMap.put(secondPlayer, session);

            waitList.remove(firstPlayer);
            waitList.remove(secondPlayer);
        }
    }

    public void disconnect(User user)
    {
        final GameSession session = gameMap.get(user);
        if (session != null)
        {
            session.disconnect(user);
        }
        waitList.remove(user);
    }

    public void endSession(User user)
    {
        gameMap.remove(user);
    }

    public void processMessage(User user, String message)
    {
        final Logger logger = Logger.getLogger("WebSocketInfo");
        final RegexCheckedParameter[] regexCheckedParameters = {new RegexCheckedParameter(message, RegexId.WS_MESSAGE_REGEX, ServerAnswer.BAD_INPUT_DATA)};
        if (Util.checkParamersByRegex(regexCheckedParameters) != ServerAnswer.OK_ANSWER)
        {
            System.out.println("Bad ws regex");
            return;
        }

        try
        {
            final JSONObject jsonObject = new JSONObject(message);
            final String sendMessage = (String)jsonObject.get("message");
            final GameSession gameSession = gameMap.get(user);
            gameSession.sendMessage(user, sendMessage);
            logger.log(Level.INFO, "{0}:Message \"{1}\" from user {2}", new Object[] {
                gameSession.hashCode(), sendMessage, user.getLogin()
            });
        }
        catch (JSONException e)
        {
            GameSession gameSession = gameMap.get(user);
            logger.log(Level.INFO, "{0}:Turn {1} from user {2}", new Object[] {
                    gameSession.hashCode(), message, user.getLogin()
            });
            final String[] ids = message.split("\\.");
            final Integer id1 = Integer.parseInt(ids[0]);
            final Integer id2 = Integer.parseInt(ids[1]);
            final GameTurn turn = new GameTurn(user, id1, id2);
            gameSession.processTurn(turn);
        }
    }
}
