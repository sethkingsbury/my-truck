package data.db;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import models.*;
import view.components.Theme;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database Operator handles all the initial setup of the database
 * including creating the connection and setting up the tables
 */
public class DatabaseOperator {

    /**
     * Name of the database used to store all the program data
     */
    private static String DATABASE_URL = "jdbc:sqlite:MyTruck.db";

    /**
     * For testing purposes only, it is used to check if the testing table
     * should be cleared
     */
    private boolean JUNIT_CLEAR_TABLE = false;

    /**
     * Only called for testing purposes, sets up a new table for testing
     * and clear the table every time
     */
    public void setDebugMode() {
        DATABASE_URL = "jdbc:sqlite:testing.db";
        JUNIT_CLEAR_TABLE = true;
    }

    /**
     * Establishes a new connection to the database
     * @return ConnectionSource object that handles connection to the database
     */
    public ConnectionSource establishConnection() {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            setupDatabase(connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connectionSource;
    }

    /**
     * Sets up the database, creates all the tables required for the program
     * @param connectionSource
     * @throws Exception
     */
    public void setupDatabase(ConnectionSource connectionSource) throws Exception {
        ArrayList<Class> cls = new ArrayList<>();

        cls.add(Ingredient.class);
        cls.add(MenuItem.class);
        cls.add(MenuItemIngredient.class);
        cls.add(Supplier.class);
        cls.add(User.class);
        cls.add(CashFloat.class);
        cls.add(Order.class);
        cls.add(Theme.class);

        for (Class c : cls) {
            TableUtils.createTableIfNotExists(connectionSource, c);
            if (JUNIT_CLEAR_TABLE) {
                TableUtils.clearTable(connectionSource, c);
            }
        }

    }

}
