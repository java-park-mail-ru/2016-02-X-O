package mechanics;

import account.User;
import util.RegexCheckedParameter;
import util.RegexId;
import util.ServerAnswer;
import util.Util;

import java.util.*;

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
        RegexCheckedParameter[] regexCheckedParameters = {new RegexCheckedParameter(message, RegexId.WS_MESSAGE_REGEX, ServerAnswer.BAD_INPUT_DATA)};
        if (Util.checkParamersByRegex(regexCheckedParameters) != ServerAnswer.OK_ANSWER)
        {
            return;
        }

        System.out.println(user.getLogin() + ' ' + message);

        final String[] ids = message.split("\\.");
        final Integer id1 = Integer.parseInt(ids[0]);
        final Integer id2 = Integer.parseInt(ids[1]);
        final GameTurn turn = new GameTurn(user, id1, id2);
        gameMap.get(user).processTurn(turn);
    }
}
