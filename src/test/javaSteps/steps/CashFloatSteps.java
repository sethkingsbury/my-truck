package javaSteps.steps;

import com.j256.ormlite.support.ConnectionSource;
import data.db.CashFloatDBOperations;
import data.db.DatabaseOperator;
import data.xml.CashFloatXMLReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.assertEquals;
import managers.DataManager;
import models.CashFloat;
import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;
import java.util.ArrayList;

public class CashFloatSteps {

    private ConnectionSource connection;
    private CashFloatDBOperations cfDBO;

    void setupDBConnection() throws SQLException {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        cfDBO = new CashFloatDBOperations(connection);
        ArrayList<CashFloat> wad = new CashFloatXMLReader("src/main/resources/data/filler/Cash.xml").parseXML();
        cfDBO.addWadToCashFloat(wad);
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
    }

    @Given("There are {int} {int}c coins in the Cash Float")
    public void thereAreCCoinsInTheCashFloat(Integer int1, Integer int2) throws SQLException {
        setupDBConnection();
        cfDBO.setQuantity(String.valueOf(int2), int1);
    }

    @When("I receive {int} {int}c coins")
    public void iReceiveCCoins(Integer int1, Integer int2) throws SQLException {
        cfDBO.increaseQuantity("Coin", int2, int1);
    }

    @Then("There is {int} {int}c coins in the Cash Float")
    public void thereIsCCoinsInTheCashFloat(Integer int1, Integer int2) throws SQLException {
        assertEquals(cfDBO.getQuantity("Coin", int2), int1);
    }

    @Given("There are {int} ${int} Notes in the Cash Float")
    public void thereAre$NotesInTheCashFloat(Integer int1, Integer int2) throws SQLException {
        setupDBConnection();
        cfDBO.setQuantity(String.valueOf(int2), int1);
    }

    @When("I give a {int} ${int} Note in change")
    public void iGiveAOne$NoteInChange(Integer int1, Integer int2) throws SQLException {
        cfDBO.decreaseQuantity("Note", int2, 0 - int1);
    }

    @Then("There is {int} ${int} Notes in the Cash Float")
    public void thereIs$NotesInTheCashFloat(Integer int1, Integer int2) throws SQLException {
        assertEquals(cfDBO.getQuantity("Note", int2), int1);
    }

}
