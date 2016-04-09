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

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    public UserDataset() {
    }

    public UserDataset(String name, String email, String password) {
        this.login = name;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.login = name;
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