package javaSteps.steps;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.MenuItemDBOperations;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.MenuItem;
import models.Money;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuItemSteps {

    private MenuItem m;
    private MenuItemDBOperations menuDBO;
    private ConnectionSource connection;
    private MenuItem query;

    public void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        menuDBO = new MenuItemDBOperations(connection);
    }

    @Given("There is a MenuItem {string} with code {string} of category {string} that costs ${int}")
    public void thereIsAMenuItemWithCodeOfCategoryThatCosts$(String string, String string2, String string3, Integer int1) {
        setupDBConnection();
        m = new MenuItem();
        m.setName(string);
        m.setCode(string2);
        m.setCategory(string3);
        m.setPrice(new Money(int1));
    }

    @When("I add {string} to the MenuItem database")
    public void iAddToTheMenuItemDatabase(String string) throws SQLException {
        menuDBO.insertMenuItem(m);
    }

    @Then("The MenuItem database contains {string}")
    public void theMenuItemDatabaseContains(String string) throws SQLException {
        query = menuDBO.getMenuItemByName(string);
        assertTrue(query.getName().equals(string));
    }

    @When("I remove {string} from the MenuItem database")
    public void iRemoveFromTheMenuItemDatabase(String string) throws SQLException {
        menuDBO.deleteExistingMenuItemByCode(string);
    }

    @Then("The MenuItem database no longer contains {string}")
    public void theMenuItemDatabaseNoLongerContains(String string) throws SQLException {
        query = menuDBO.getMenuItemByCode(string);
        assertNull(query);
    }

    @When("I change the price of {string} to ${int}")
    public void iChangeThePriceOfTo$(String string, Integer int1) throws SQLException {
        query = menuDBO.getMenuItemByName(string);
        menuDBO.updatePriceAmount(query, int1);
    }

    @When("I change the category of {string} to {string}")
    public void iChangeTheCategoryOfTo(String string, String string2) throws SQLException {
        query = menuDBO.getMenuItemByName(string);
        menuDBO.updateMenuItemCategory(query, string2);
    }

    @When("I change the code of {string} to {string}")
    public void iChangeTheCodeOfTo(String string, String string2) throws SQLException {
        query = menuDBO.getMenuItemByName(string);
        query.setCode(string2);
        menuDBO.updateMenuItem(query);
    }

    @When("I change the name of {string} to {string}")
    public void iChangeTheNameOfTo(String string, String string2) throws SQLException {
        query = menuDBO.getMenuItemByName(string);
        query.setName(string2);
        menuDBO.updateMenuItem(query);
    }

    @Then("There is a MenuItem {string} with code {string} of type {string} that costs ${int}")
    public void thereIsAMenuItemWithCodeOfTypeThatCosts$(String string, String string2, String string3, Integer int1) throws SQLException {
        query = menuDBO.getMenuItemByName(string);
        assertTrue(query.getName().equals(string));
        assertTrue(query.getCode().equals(string2));
        assertTrue(query.getCategory().equals(string3));
    }

}
