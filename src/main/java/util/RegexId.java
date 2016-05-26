package util;

/*
 * Created by kvukolov on 17.02.16.
 */
public enum RegexId {
    LOGIN_REGEX(1),
    EMAIL_REGEX(2),
    PASSWORD_REGEX(3),
    ID_REGEX(4),
    WS_MESSAGE_REGEX(5);

    private int value;
    RegexId(int value)
    {
        this.value = value;
    }
    public int getValue()
    {
        return value;
    }
}
