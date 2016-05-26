package mechanics;

/**
 * Created by kvukolov on 26.05.16.
 */
public class GameException extends Exception {
    GameEvents reason;

    public GameException(GameEvents reason) {
        this.reason = reason;
    }

    public GameEvents getReason() {
        return reason;
    }
}
