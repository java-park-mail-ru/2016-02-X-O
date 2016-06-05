package account;

/**
 * Created by kvukolov on 05.05.16.
 */
public interface User {
    long getId();

    void setId(long id);

    void setLogin(String login);

    void setEmail(String email);

    void setPassword(String password);

    String getLogin();

    String getEmail();

    String getPassword();

    void save();

    Integer getScore();
    void setScore(Integer score);
}
