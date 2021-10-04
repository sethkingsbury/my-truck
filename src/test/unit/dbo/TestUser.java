package unit.dbo;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.IngredientDBOperations;
import data.db.SupplierDBOperations;
import data.db.UserDBOperations;
import models.Ingredient;
import models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUser {

    private static ConnectionSource connection;
    private static UserDBOperations userDBO;
    private static IngredientDBOperations ingDBO;

    @BeforeAll
    static void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        userDBO = new UserDBOperations(connection);
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Admin", "Jono"})
    public void testInsertUserToDB(String name) throws SQLException {
        User u = new User();
        u.setUsername(name);
        u.setPassword(name);
        userDBO.insertUser(u); // should not throw exception

        u = userDBO.getUserByUsername(name).get();
        assertEquals(u.getUsername(), name);
        assertNotEquals(u.getPassword(), name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "Pete", "Joe"})
    public void testGetAllUsers(String name) throws SQLException {
        User u = new User();
        u.setUsername(name);
        u.setPassword(name);
        userDBO.insertUser(u); // should not throw exception

        List<User> users = userDBO.getAllUsers().get();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();

        // Cool python lamba like function to change suppliers to their names and contacts
        users.forEach( (use) -> names.add(use.getUsername()));
        users.forEach( (use) -> passwords.add(use.getPassword()));
        assertTrue(names.contains(name));
        assertFalse(passwords.contains(name));
    }

    @Test
    public void testGetAllOwners() throws SQLException {
        User user1 = new User();
        user1.setUsername("Owner");
        user1.setPassword("Owner");
        user1.setAccountType(3);
        userDBO.insertUser(user1); // should not throw exception

        User user2 = new User();
        user2.setUsername("Employee");
        user2.setPassword("Employee");
        user2.setAccountType(1);
        userDBO.insertUser(user2); // should not throw exception

        List<User> users = userDBO.getAllOwners().get();
        ArrayList<String> usernames = new ArrayList<String>();

        // Cool python lamba like function to change suppliers to their names and contacts
        users.forEach( (user) -> usernames.add(user.getUsername()));

        assertTrue(usernames.contains("Owner"));
        assertFalse(usernames.contains("Employee"));
    }

    @Test
    public void testDeleteUser() throws SQLException {
        User user1 = new User();
        user1.setUsername("ilovetesting");
        user1.setPassword("Owner");
        user1.setAccountType(1);
        userDBO.insertUser(user1);

        User user2 = new User();
        user2.setUsername("testingisawesomeandcontributesalottowarddevelopmentandisntawasteoftime");
        user2.setPassword("Employee");
        user2.setAccountType(1);
        userDBO.insertUser(user2);

        userDBO.deleteUser(user2.getId());

        Optional<List<User>> users = Optional.empty();
        if (userDBO.getAllUsers().isPresent()) {
            users = userDBO.getAllUsers();
        }
        ArrayList<String> names = new ArrayList<String>();

        users.get().forEach( (user) -> names.add(user.getUsername()));
        assertFalse(names.contains("testingisawesomeandcontributesalottowarddevelopmentandisntawasteoftime"));
        assertTrue(names.contains("ilovetesting"));

    }
}
