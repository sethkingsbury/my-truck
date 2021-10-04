package unit.xml;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.IngredientDBOperations;
import data.xml.IngredientXMLReader;
import models.Ingredient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestXMLIngredient {
    @Test
    void testReadIngredientXML() {
        IngredientXMLReader s = new IngredientXMLReader("src/main/resources/data/testing/testIngredients.xml");
        ArrayList<Ingredient> ingredients = s.parseXML();
        Ingredient i1 = ingredients.get(0);
        assertEquals(i1.getName(), "Burger Bun");
        assertEquals(i1.getCode(), "BBun");
    }

}
