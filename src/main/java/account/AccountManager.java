package account;

import util.ServerAnswer;

/**
 * Created by kvukolov on 18.02.16.
 */
public interface AccountManager {
    ServerAnswer addUser(String login, String email, String password);
    ServerAnswer authenticate(String sessionId, String login, String password);
    void logout(String sessionId);
    boolean isAuthenticated(String sessionId);
    User getUserBySession(String sessionId);
    User getUserByLogin(String login);
    User getUserById(long id);
    void deleteUser(String login);
}
