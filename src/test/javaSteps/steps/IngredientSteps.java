package javaSteps.steps;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.IngredientDBOperations;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.Ingredient;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class IngredientSteps {

    private IngredientDBOperations ingDBO;
    private ConnectionSource connection;
    private Ingredient i;
    private Ingredient query;

    public void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        ingDBO = new IngredientDBOperations(connection);
    }

    @Given("There is an ingredient {string}")
    public void thereIsAnIngredient(String string) throws SQLException {
        setupDBConnection();
        i = new Ingredient();
        i.setName(string);
        i.setCode(string);
        i.setQuantityMeasuredIn(string);
    }

    @When("I add {string} to the ingredient database")
    public void iAddToTheIngredientDatabase(String string) throws SQLException {
        ingDBO.insertIngredient(i);
    }

    @Then("The ingredient database contains {string}")
    public void theIngredientDatabaseContains(String string) throws SQLException {
        query = ingDBO.getIngredientsByName(string);
        assertTrue(query.getName().equals(string));
    }

    @When("I remove {string} from the ingredient database")
    public void iRemoveFromTheIngredientDatabase(String string) throws SQLException {
        ingDBO.deleteExistingIngredientByCode(string);
    }

    @Then("The ingredient database no longer contains {string}")
    public void theIngredientDatabaseNoLongerContains(String string) throws SQLException {
        ArrayList<Ingredient> ingredients = ingDBO.getAllIngredients();
        for (Ingredient i : ingredients) {
            if (i.getName().equals(string)) {
                fail("Should not reach here");
            }
        }
    }

    @Given("There is an ingredient {string} with {int} units")
    public void thereIsAnIngredientWithUnits(String string, Integer int1) {
        setupDBConnection();
        i = new Ingredient();
        i.setName(string);
        i.setCode(string);
        i.setQuantityMeasuredIn(string);
        i.setAmount(int1);
    }

    @When("I add {int} units of {string}")
    public void iAddUnitsOf(Integer int1, String string) throws SQLException {
        query = ingDBO.getIngredientsByName(string);
        ingDBO.updateIngredientAmount(query, query.getAmount() + int1);
    }

    @Then("There is {int} units of {string}")
    public void thereIsUnitsOf(Integer int1, String string) throws SQLException {
        query = ingDBO.getIngredientsByName(string);
        assertTrue(query.getAmount() == int1);
    }

    @When("I remove {int} units of {string}")
    public void iRemoveUnitsOf(Integer int1, String string) throws SQLException {
        query = ingDBO.getIngredientsByName(string);
        ingDBO.updateIngredientAmount(query, query.getAmount() - int1);
    }

}
