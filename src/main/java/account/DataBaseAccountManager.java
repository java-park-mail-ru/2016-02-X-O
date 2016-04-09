package account;

import database.DBService;
import database.datasets.UserDataset;
import servlets.Session;
import util.ServerAnswer;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by kvukolov on 09.04.16.
 */
public class DataBaseAccountManager implements AccountManager {
    private Map<String, String> signedInUsers = new HashMap<>();
    private DBService dbService = DBService.getService();

    @Override
    public ServerAnswer addUser(String login, String email, String password) {
        if (dbService.readByName(login) != null)
        {
            return ServerAnswer.LOGIN_IN_USE;
        }
        if (dbService.readByEmail(email) != null)
        {
            return ServerAnswer.EMAIL_IN_USE;
        }
        final UserDataset user = new UserDataset(login, email, password);
        dbService.save(user);
        return ServerAnswer.OK;
    }

    @Override
    public ServerAnswer authenticate(String sessionId, String login, String password) {
        final UserDataset user = dbService.readByName(login);
        if (user != null)
        {
            if (user.getPassword().equals(password))
            {
                signedInUsers.put(sessionId, user.getName());
                return ServerAnswer.OK;
            }
            return ServerAnswer.WRONG_CREDENTIALS;
        }
        return ServerAnswer.WRONG_CREDENTIALS;
    }

    @Override
    public void logout(String sessionId) {
        signedInUsers.remove(sessionId);
    }

    @Override
    public boolean isAuthenticated(String sessionId) {
        return !(signedInUsers.get(sessionId) == null);
    }

    @Override
    public User getUserBySession(String sessionId) {
        final UserDataset user = dbService.readByName(signedInUsers.get(sessionId));
        final User userr =  new User(user.getName(), user.getEmail(), user.getPassword());
        userr.setId(user.getId());
        return userr;
    }

    @Override
    public User getUserByLogin(String login) {
        final UserDataset user = dbService.readByName(login);
        final User userr =  new User(user.getName(), user.getEmail(), user.getPassword());
        userr.setId(user.getId());
        return userr;
    }

    @Override
    public User getUserById(long id) {
        final UserDataset user = dbService.readById(id);
        final User userr =  new User(user.getName(), user.getEmail(), user.getPassword());
        userr.setId(user.getId());
        return userr;
    }

    @Override
    public void deleteUser(String login) {
        dbService.deleteByLogin(login);
    }

    @Override
    public void updateUser(long id, User user) {

    }
}
