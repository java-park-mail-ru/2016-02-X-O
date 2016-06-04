package mechanics;

/**
 * Created by kvukolov on 26.05.16.
 */
public enum GameEvents {
    CONTINUE_GAME(0),
    YOU_WIN(1),
    OPPONENT_WIN(2),
    ID_REGEX(3),
    START_GAME(4),
    GAME_END(5),
    OPPONENT_DISCONNECT(6),
    DRAW(7),
    NEW_MESSAGE(8),

    ERROR_FIELD_BUSY(101),
    ERROR_WRONG_SQUARE(102),
    ERROR_WRONG_USER_TURN(103),
    ERROR_WRONG_DATA(103);

    private int value;
    GameEvents(int value)
    {
        this.value = value;
    }
    public int getValue()
    {
        return value;
    }
}
