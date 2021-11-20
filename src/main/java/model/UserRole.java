package model;

import javax.persistence.*;

@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int             userRole;       // User Role ID

    @Column(unique = true)
    private  String    roleName;       // User Role Name
    private  boolean   canAddUsers;    // If can add users
    private  boolean   canAddSongs;    // If can add songs

    public UserRole() {
    }

    public UserRole(String roleName, boolean canAddUsers, boolean canAddSongs) {
        this.roleName = roleName;
        this.canAddUsers = canAddUsers;
        this.canAddSongs = canAddSongs;
    }

    public int getUserRole() {
        return userRole;
    }

    public String getRoleName() {
        return roleName;
    }

    public boolean isCanAddUsers() {
        return canAddUsers;
    }

    public boolean isCanAddSongs() {
        return canAddSongs;
    }
}
