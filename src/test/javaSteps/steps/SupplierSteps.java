package javaSteps.steps;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.SupplierDBOperations;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Supplier;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SupplierSteps {


    private SupplierDBOperations sDBO;
    private ConnectionSource connection;
    private Supplier query;
    private Supplier s;

    public void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        sDBO = new SupplierDBOperations(connection);
    }

    @Given("There is a supplier {string} with contact number {string}")
    public void thereIsASupplierWithContactNumber(String string, String string2) {
        setupDBConnection();
        s = new Supplier();
        s.setName(string);
        s.setContact(string2);
    }

    @When("I add {string} to the supplier database")
    public void iAddToTheSupplierDatabase(String string) throws SQLException {
        sDBO.insertSupplier(s);
    }

    @Then("The supplier database contains {string}")
    public void theSupplierDatabaseContains(String string) throws SQLException {
        query = sDBO.getSupplier(string);
        assertTrue(query.getName().equals(string));
    }

    @When("I remove {string} from the suppliers database")
    public void iRemoveFromTheSuppliersDatabase(String string) throws SQLException {
        sDBO.removeSupplierByName(string);
    }

    @Then("The supplier database does not contain {string}")
    public void theSupplierDatabaseDoesNotContain(String string) throws SQLException {
        query = sDBO.getSupplier(string);
        assertNull(query);
    }

    @When("I change the contact number of {string} to {string}")
    public void iChangeTheContactNumberOfTo(String string, String string2) throws SQLException {
        sDBO.updateSupplierContactByName(string, string2);

        query = sDBO.getSupplier(string);
        assertTrue(query.getContact().equals(string2));
    }

}
