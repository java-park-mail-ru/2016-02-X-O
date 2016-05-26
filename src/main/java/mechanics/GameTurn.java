package mechanics;

import account.User;

/**
 * Created by kvukolov on 25.05.16.
 */
public class GameTurn {
    User user;
    Integer id1;
    Integer id2;

    public GameTurn(User user, Integer id1, Integer id2) {
        this.user = user;
        this.id1 = id1;
        this.id2 = id2;
    }

    public User getUser() {
        return user;
    }

    public Integer getId1() {
        return id1;
    }

    public Integer getId2() {
        return id2;
    }
}
