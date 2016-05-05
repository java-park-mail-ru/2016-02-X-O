package account;

import database.DBService;
import database.datasets.UserDataset;

/**
 * Created by kvukolov on 05.05.16.
 */
public class DataSetToUserAdapter implements User {
    UserDataset userDataset;

    public DataSetToUserAdapter(UserDataset userDataset) {
        this.userDataset = userDataset;
    }

    @Override
    public long getId() {
        return userDataset.getId();
    }

    @Override
    public void setId(long id) {
    }

    @Override
    public void setLogin(String login) {
        userDataset.setLogin(login);
    }

    @Override
    public void setEmail(String email) {
        userDataset.setEmail(email);
    }

    @Override
    public void setPassword(String password) {
        userDataset.setPassword(password);
    }

    @Override
    public String getLogin() {
        return userDataset.getName();
    }

    @Override
    public String getEmail() {
        return userDataset.getEmail();
    }

    @Override
    public String getPassword() {
        return userDataset.getPassword();
    }

    @Override
    public void save(){
        DBService.getService().save(userDataset);
    }
}
