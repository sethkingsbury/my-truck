package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.User;
import utils.Password;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * UserDBOperations provides functions to interact with
 * the USER table
 */
public class UserDBOperations {

    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the USER table
     */
    private Dao<User, Integer> userDao;

    /**
     * Default constructor for UserDBOperations
     *
     * @param connectionSource
     */
    public UserDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            userDao = DaoManager.createDao(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a single user into database
     *
     * @param user User
     * @throws SQLException
     */
    public void insertUser(User user) throws SQLException {
        if (!checkDuplicateUser(user.getUsername())) {
            userDao.create(user);
        }
    }

    /**
     * Inserts a single user if they do not exist already. If the user
     * account already exists, update it.
     * @param user object to insert or update
     * @throws SQLException
     */
    public void insertOrUpdateUser(User user) throws SQLException {
        if (checkDuplicateUser(user.getId())) {
            updateUser(user);
        } else {
            insertUser(user);
        }
    }

    /**
     * Updates a user given a new user object
     * @param user new object to update the old one with
     * @throws SQLException
     */
    public void updateUser(User user) throws SQLException {
        UpdateBuilder<User, Integer> updateBuilder = userDao.updateBuilder();
        updateBuilder.updateColumnValue("username", user.getUsername());
        updateBuilder.updateColumnValue("password", user.getPassword());
        updateBuilder.updateColumnValue("accountType", user.getAccountType());
        updateBuilder.where().eq("id", user.getId());
        updateBuilder.update();
    }

    /**
     * Deletes a user by their unique username.
     *
     * @param username of the user
     * @throws SQLException
     */
    public void deleteUser(String username) throws SQLException {
        userDao.deleteBuilder().where().eq("username", username);
        userDao.deleteBuilder().delete();
    }

    /**
     * Deletes a user by their id.
     *
     * @param id of the user
     * @throws SQLException
     */
    public void deleteUser(Integer id) throws SQLException {
        userDao.deleteById(id);
    }

    /**
     * Get all users in database
     *
     * @return a collection of all users
     * @throws SQLException
     */
    public Optional<List<User>> getAllUsers() throws SQLException {
        return Optional.ofNullable(userDao.queryForAll());
    }

    /**
     * Get all users with the owner permission level
     *
     * @return a collection of owners
     * @throws SQLException
     */
    public Optional<List<User>> getAllOwners() throws SQLException {
        List<User> user = userDao.queryBuilder().where().eq("accountType", 3).query();
        return Optional.ofNullable(user);
    }

    /**
     * Get a User by their name
     *
     * @param username Username of user
     * @return User found
     * @throws SQLException
     */
    public Optional<User> getUserByUsername(String username) throws SQLException {
        User user = userDao.queryBuilder()
                .where()
                .eq("username", username)
                .queryForFirst();
        return Optional.ofNullable(user);
    }

    /**
     * Checks if user already exists in the database
     *
     * @param username string to check if already exists
     * @return True if duplicate, false otherwise
     * @throws SQLException
     */
    public boolean checkDuplicateUser(String username) throws SQLException {
        List<User> existingUsers = userDao.queryForEq("username", username);
        return existingUsers.size() > 0;
    }

    /**
     * Checks if user already exists in the database given their user id
     *
     * @param id string to check if already exists
     * @return True if duplicate, false otherwise
     * @throws SQLException
     */
    public boolean checkDuplicateUser(Integer id) throws SQLException {
        return userDao.queryForId(id) != null;
    }

    /**
     * Checks if username and password matches a user in database
     *
     * @param username  Username
     * @param plaintext representation of the password
     * @return the user object if a match is made.
     * @throws SQLException
     */
    public Optional<User> authenticateUser(String username, String plaintext) throws SQLException {
        Optional<User> user = getUserByUsername(username);
        if (user.isPresent()) {
            if (Password.checkPassword(plaintext, user.get().getPassword())) {
                return user;
            }
        }
        return Optional.empty();
    }
}
