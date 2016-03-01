package account;

import util.ServerAnswer;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by kvukolov on 13.02.16.
 */

public class InmemoryAccountManager implements AccountManager {

    private static long currentId = 1;

    private Map<String, User> signedUpUsers = new HashMap<>();
    private Map<String, String> signedInUsers = new HashMap<>();

    public InmemoryAccountManager() {
    }

    public ServerAnswer addUser(String login, String email, String password)
    {
        User user = new User(login, email, password);
        for (User signuppedUser: signedUpUsers.values())
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
        return ServerAnswer.OK;
    }

    public ServerAnswer authenticate(String sessionId, String login, String password)
    {
        User signuppedUser = signedUpUsers.get(login);
        if (signuppedUser == null || !signuppedUser.getPassword().equals(password))
        {
            return ServerAnswer.WRONG_CREDENTIALS;
        }
        signedInUsers.put(sessionId, signuppedUser.getLogin());
        return ServerAnswer.OK;
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
    public User getUserBySession(String sessionId) {
        return signedUpUsers.get(signedInUsers.get(sessionId));
    }

    @Override
    public User getUserByLogin(String login) {
        return signedUpUsers.get(login);
    }

    @Override
    public User getUserById(long id) {
        for (User user: signedUpUsers.values())
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
