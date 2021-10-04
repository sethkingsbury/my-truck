package managers;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.UserDBOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Manager provides support for accessing data throughout
 * the User controller.
 */
public class UserManager {

    private DatabaseOperator db = new DatabaseOperator();
    private ConnectionSource connectionSource = db.establishConnection();
    private UserDBOperations userDBO = new UserDBOperations(connectionSource);

    private static final UserManager userManager = new UserManager();

    private User currentlyLoggedInUser;
    private ObservableList<User> users = FXCollections.observableArrayList();

    /**
     * Authenticates a user given a username and a plaintext representation of their password.
     * @param username for the user
     * @param plaintext password for the user
     * @return success or failure
     */
    public Boolean authenticate(String username, String plaintext) {
        try {
           Optional<User> user = userDBO.authenticateUser(username, plaintext);
            if (user.isPresent()) {
                this.currentlyLoggedInUser = user.get();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.currentlyLoggedInUser != null;
    }

    /**
     * Creates a new user as the most basic user account type.
     * @param username of the user
     * @param plaintext password
     */
    public void createNewUser(String username, String plaintext) {
        createNewUser(username, plaintext, 0);
    }

    /**
     * Creates a new user with a given account type.
     * @param username of the user
     * @param plaintext password
     * @param accountType the level of permissions
     */
    public void createNewUser(String username, String plaintext, Integer accountType) {
        User user = new User(username, plaintext, accountType);
        try {
            userDBO.insertUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new user given a user object.
     * @param user the user object
     */
    public void createNewUser(User user) {
        try {
            userDBO.insertUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a list of ingredients to the database.
     * @param users list of users to be added
     */
    public void createNewUsers(Iterable<User> users) {
        for (User user : users) {
            createNewUser(user);
        }
    }

    /**
     * Creates a new user or updates an existing user given a user object.
     * @param user the user object
     */
    public void createOrUpdateNewUser(User user) {
        try {
            userDBO.insertOrUpdateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Edits/updates a user or given a user object.
     * @param user the user object
     */
    public void updateUser(User user) {
        try {
            userDBO.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all users with owner permission level
     */
    public List<User> getAllOwners() {
        try {
            Optional<List<User>> owners = userDBO.getAllOwners();
            if (owners.isPresent()) {
                return owners.get();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        try {
            Optional<List<User>> users = userDBO.getAllUsers();
            if (users.isPresent()) {
                return users.get();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all usernames for users
     */
    public List<String> getAllUserUsernames() {
        return getAllUsers().stream().map(User::getUsername).collect(Collectors.toList());
    }


    /**
     * Get user given their username
     */
    public User getUser(String username) {
        try {
            Optional<User> user = userDBO.getUserByUsername(username);
            if (user.isPresent()) {
                return user.get();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Deletes a user by their unique username.
     * @param username of the user
     * @throws SQLException
     */
    public void deleteUser(String username) {
        try {
            userDBO.deleteUser(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a user by their id.
     * @param id of the user
     * @throws SQLException
     */
    public void deleteUser(Integer id) {
        try {
            userDBO.deleteUser(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the list of users with the users in the database.
     */
    public void refreshUsers() throws SQLException {
        userDBO.getAllUsers().ifPresent(users1 -> users.setAll(users1));
    }

    /**
     * Logs the current user out.
     */
    public void logout() {
        this.currentlyLoggedInUser = null;
    }

    public static UserManager getUserManager() { return userManager; }

    public User getCurrentlyLoggedInUser() { return currentlyLoggedInUser; }

    public ObservableList<User> getUsers() { return users; }
}
