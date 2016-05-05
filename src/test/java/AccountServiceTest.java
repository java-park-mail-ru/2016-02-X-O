import account.AccountManager;
import account.MainAccountManager;
import account.User;
import junit.framework.TestCase;
import org.junit.Test;
import util.ServerAnswer;

/**
 * Created by kvukolov on 05.05.16.
 */
public class AccountServiceTest {
    AccountManager accountManager = MainAccountManager.getManager();
    final String login = "login";
    final String email = "email@email.com";
    final String password = "password";
    final String sessionId = "sessionId";
    final String otherLogin = "login1";

    @Test
    public void addUserTest(){

        ServerAnswer answer = accountManager.addUser(login, email, password);
        TestCase.assertEquals(answer, ServerAnswer.OK_ANSWER);

        answer = accountManager.addUser(login, email, password);
        TestCase.assertEquals(answer, ServerAnswer.LOGIN_IN_USE);

        answer = accountManager.addUser(otherLogin, email, password);
        TestCase.assertEquals(answer, ServerAnswer.EMAIL_IN_USE);

        accountManager.deleteUser(login);
    }

    @Test
    public void authenticateTest(){
        accountManager.addUser(login, email, password);
        ServerAnswer answer = accountManager.authenticate(sessionId, login, password);
        TestCase.assertEquals(answer, ServerAnswer.OK_ANSWER);

        answer = accountManager.authenticate(sessionId, otherLogin, password);
        TestCase.assertEquals(answer, ServerAnswer.WRONG_CREDENTIALS);

        boolean auth = accountManager.isAuthenticated(sessionId);
        TestCase.assertEquals(auth, true);

        final User user = accountManager.getUserBySession(sessionId);

        TestCase.assertEquals(user.getLogin(), login);
        TestCase.assertEquals(user.getEmail(), email);
        TestCase.assertEquals(user.getPassword(), password);

        accountManager.logout(sessionId);

        auth = accountManager.isAuthenticated(sessionId);
        TestCase.assertEquals(auth, false);

        accountManager.deleteUser(login);
    }

    @Test
    public void getUserTest(){
        accountManager.addUser(login, email, password);
        User user = accountManager.getUserByLogin(login);
        TestCase.assertEquals(login, user.getLogin());
        TestCase.assertEquals(email, user.getEmail());
        TestCase.assertEquals(password, user.getPassword());

        accountManager.deleteUser(login);
    }

}
