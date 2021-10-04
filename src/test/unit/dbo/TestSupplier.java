package unit.dbo;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.SupplierDBOperations;
import models.Supplier;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSupplier {

    private static ConnectionSource connection;
    private static SupplierDBOperations supDBO;

    @BeforeAll
    static void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        supDBO = new SupplierDBOperations(connection);
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
    }

    @ParameterizedTest
    @ValueSource(strings = {"The people down the road, I forgot their name but they have heaps of sauce", "Jono"})
    void testInsertSupplierToDB(String name) throws SQLException {
        Supplier s = new Supplier();
        s.setName(name);
        s.setContact(name);
        supDBO.insertSupplier(s); // should not throw exception

        s = supDBO.getSupplier(name);
        assertEquals(s.getName(), name);
        assertEquals(s.getContact(), name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "Pete", "Joe"})
    void testGetAllSuppliers(String name) throws SQLException {
        Supplier s = new Supplier();
        s.setName(name);
        s.setContact(name);
        supDBO.insertSupplier(s); // should not throw exception

        ArrayList<Supplier> sups = supDBO.getAllSuppliers();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> contacts = new ArrayList<String>();

        // Cool python lamba like function to change suppliers to their names and contacts
        sups.forEach( (sup) -> names.add(sup.getName()));
        sups.forEach( (sup) -> contacts.add(sup.getContact()));
        assertTrue(names.contains(name));
        assertTrue(contacts.contains(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"The people down the road, I forgot their name but they have heaps of saucy chickens", "JOJO"})
    void testGetAllSupplierNames(String name) throws SQLException {
        Supplier s = new Supplier();
        s.setName(name);
        s.setContact(name);
        supDBO.insertSupplier(s); // should not throw exception

        ArrayList<String> l = new ArrayList<>(supDBO.getAllSupplierNames());
        assertTrue(l.contains(name));
    }
}
