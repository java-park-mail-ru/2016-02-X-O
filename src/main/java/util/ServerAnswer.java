package util;

/*
 * Created by kvukolov on 17.02.16.
 */
public enum ServerAnswer {
    OK(0),


    BAD_INPUT_DATA(1),
    LOGIN_REQUIRED(2),


    LOGIN_IN_USE(101),
    EMAIL_IN_USE(102),
    BAD_LOGIN(103),
    BAD_EMAIL(104),
    BAD_PASSWORD(105),
    BAD_ID(106),
    WRONG_CREDENTIALS(107),

    NO_USER(108);


    private int value;
    ServerAnswer(int value)
    {
        this.value = value;
    }
    public int getValue()
    {
        return value;
    }
}
