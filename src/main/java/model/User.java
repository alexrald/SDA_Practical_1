package model;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int                 idUser;                 // User ID

    @Column(unique = true)
    private String              userName;               // User Name
    private int                 passwordHash;           // User Password Hash generated from user-entered hash using hashCode()
    private String              eMail;                  // User e-mail to handle subscriptions
    private Date                date_of_registry;       // User date of registry

    @ManyToOne
    @JoinColumn(name = "userRole")
    private UserRole    userRole;

    public User() {
    }

    public User(String userName, String password, String eMail, UserRole userRole)
    {
        this.userName = userName;
        this.passwordHash = password.hashCode();
        this.eMail = eMail;
        this.userRole = userRole;
        this.date_of_registry = new Date(System.currentTimeMillis());
    }

    public boolean authUser(String password)
    {
        return (password.hashCode() == this.passwordHash);
    }

    public int getIdUser() {
        return idUser;
    }

    public String getUserName() {
        return userName;
    }

    public String geteMail() {
        return eMail;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Date getDate_of_registry() {
        return date_of_registry;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public int getPasswordHash() {
//        return passwordHash;
//    }

    public void seteMail(String eMail)
    {
        this.eMail = eMail;
    }

    public void setPasswordHash(String password)
    {
        this.passwordHash = password.hashCode();
    }

    public void setPasswordHash(int passwordHash) {this.passwordHash = passwordHash;}

    public void setUserRole(UserRole userRole)
    {
        this.userRole = userRole;
    }

    public void displayUserData() {
        System.out.println("");
        System.out.println("User name:        " + userName);
        System.out.println("User ID:          " + idUser);
        System.out.println("E-mail:           " + eMail);
        System.out.println("Date of registry: " + date_of_registry);
        System.out.println("User role:        " + userRole.getRoleName());
    }
}
