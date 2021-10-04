package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import utils.Password;

/**
 * Model for User
 */
@DatabaseTable(tableName = "USER")
public class User {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(unique = true, canBeNull = false)
    private String username;

    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(canBeNull = false)
    private int accountType;

    /**
     * Empty constructor to keep ORMLite happy.
     */
    public User() { }

    /**
     * Constructor for the User class that creates a normal non admin, non analyst user.
     * @param username of the user
     * @param plaintext for the user's account password
     */
    public User(String username, String plaintext) {
        this.username = username;
        this.password = Password.hashPassword(plaintext);
        this.accountType = 0;
    }

    /**
     * Constructor for the User class that assigns an account type
     * @param username of the user
     * @param plaintext for the user's account password
     * @param accountType flag
     */
    public User(String username, String plaintext, int accountType) {
        this.username = username;
        this.password = Password.hashPassword(plaintext);
        this.accountType = accountType;
    }

    /**
     * Constructor for the User class that assigns an account type and a user Id.
     * @param id of the user
     * @param username of the user
     * @param plaintext for the user's account password
     * @param accountType flag
     */
    public User(Integer id, String username, String plaintext, int accountType) {
        this.id = id;
        this.username = username;
        this.password = Password.hashPassword(plaintext);
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() { return password; }

    public void setPassword(String plaintext) { this.password = Password.hashPassword(plaintext); }

    public void setPasswordHash(String hash) { this.password = hash; }

    public int getAccountType() { return accountType; }

    public void setAccountType(int accountType) { this.accountType = accountType; }

    /**
     * toString method for User
     * @return String representation for User
     */
    public String toString() {
        String template = "";
        template += "Username: " + this.username + "\n";
        template += "Password: " + this.password + "\n";
        template += "Account Type: " + this.accountType + "\n";
        return template;
    }

}
