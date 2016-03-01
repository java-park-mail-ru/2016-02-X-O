package account;

/*
 * Created by kvukolov on 18.02.16.
 */
public class MainAccountManager {
    private static AccountManager ourInstance = new InmemoryAccountManager();

    public static AccountManager getManager() {
        return ourInstance;
    }

    private MainAccountManager() {
    }
}
