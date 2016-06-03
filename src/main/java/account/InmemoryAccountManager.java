package account;

import util.ServerAnswer;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by kvukolov on 13.02.16.
 */

public class InmemoryAccountManager implements AccountManager {

    private static long currentId = 1;

    private Map<String, UserImpl> signedUpUsers = new HashMap<>();
    private Map<String, String> signedInUsers = new HashMap<>();

    @Override
    public ServerAnswer addUser(String login, String email, String password)
    {
        final UserImpl user = new UserImpl(login, email, password);
        for (UserImpl signuppedUser: signedUpUsers.values())
        {
            if (signuppedUser.getLogin().equals(user.getLogin()))
            {
                return ServerAnswer.LOGIN_IN_USE;
            }
            if (signuppedUser.getEmail().equals(user.getEmail()))
            {
                return ServerAnswer.EMAIL_IN_USE;
            }
        }
        signedUpUsers.put(user.getLogin(), user);
        user.setId(currentId);
        currentId++;
        return ServerAnswer.OK_ANSWER;
    }

    @Override
    public ServerAnswer authenticate(String sessionId, String login, String password)
    {
        final UserImpl signuppedUser = signedUpUsers.get(login);
        if (signuppedUser == null || !signuppedUser.getPassword().equals(password))
        {
            return ServerAnswer.WRONG_CREDENTIALS;
        }
        signedInUsers.put(sessionId, signuppedUser.getLogin());
        return ServerAnswer.OK_ANSWER;
    }

    @Override
    public void logout(String sessionId) {
        signedInUsers.remove(sessionId);
    }

    @Override
    public boolean isAuthenticated(String sessionId)
    {
        return !(signedInUsers.get(sessionId) == null);
    }

    @Override
    public UserImpl getUserBySession(String sessionId) {
        return signedUpUsers.get(signedInUsers.get(sessionId));
    }

    @Override
    public UserImpl getUserByLogin(String login) {
        return signedUpUsers.get(login);
    }

    @Override
    public UserImpl getUserById(long id) {
        for (UserImpl user: signedUpUsers.values())
        {
            if (user.getId() == id)
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(String login) {
        signedUpUsers.remove(login);
    }
}
