package database.datasets;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserDataset
{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", unique=true)
    private String login;

    @Column(name = "email", unique=true)
    private String email;

    @Column(name = "password")
    private String password;

    public UserDataset(String name, String email, String password) {
        this.login = name;
        this.email = email;
        this.password = password;
    }

    public UserDataset(){}

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + id +
                ", name='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}