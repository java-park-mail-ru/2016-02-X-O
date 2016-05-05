package account;

/*
 * Created by kvukolov on 18.02.16.
 */
public final class MainAccountManager {
    private static AccountManager ourInstance = new DataBaseAccountManager();

    public static AccountManager getManager() {
        return ourInstance;
    }

    private MainAccountManager() {
    }
}
