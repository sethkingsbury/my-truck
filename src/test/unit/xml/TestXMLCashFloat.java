package unit.xml;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.xml.CashFloatXMLReader;
import models.CashFloat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestXMLCashFloat {
    @Test
    void testReadIngredientXML() {
        CashFloatXMLReader c = new CashFloatXMLReader("src/main/resources/data/testing/testCashFloat.xml");
        ArrayList<CashFloat> cashFloat = c.parseXML();
        CashFloat c1 = cashFloat.get(0);
        assertEquals(c1.getType(), "Coin");
        assertEquals(c1.getDenomination(), 10);
        assertEquals(c1.getValueInCents(), 10);
    }

}
