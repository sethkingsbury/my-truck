package unit.dbo;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.IngredientDBOperations;
import models.Ingredient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class TestIngredient {

    private static ConnectionSource connection;
    private static IngredientDBOperations ingDBO;

    @BeforeAll
    static void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        ingDBO = new IngredientDBOperations(connection);
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ReallyLongLongLongLongLongName", "Carrot"})
    void testInsertIngredientToDBCustomName(String name) throws SQLException {
        Ingredient i = new Ingredient();
        i.setName(name);
        i.setCode(name);
        i.setQuantityMeasuredIn("count");
        ingDBO.insertIngredient(i); // should not throw exception

        i = ingDBO.getIngredientsByName(name);
        assertTrue(i.getName().equals(name));
        assertTrue(i.getCode().equals(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Chickpeas", "Hummus", "Bread"})
    void testGetAllIngredients(String name) throws SQLException {
        Ingredient i = new Ingredient();
        i.setName(name);
        i.setCode(name);
        i.setQuantityMeasuredIn("gr");
        ingDBO.insertIngredient(i);

        ArrayList<Ingredient> ings = ingDBO.getAllIngredients();
        ArrayList<String> names = new ArrayList<String>();

        // Cool python lamba like function to change ingredients to their names
        ings.forEach( (ing) -> names.add(ing.getName()));
        assertTrue(names.contains(name));
    }

    @Test
    void testQueryIngredientByIdAndName() throws SQLException {
        Ingredient i = new Ingredient();
        i.setName("Beans");
        i.setCode("BEANS");
        i.setQuantityMeasuredIn("gr");
        ingDBO.insertIngredient(i);
        i = ingDBO.getIngredientsByName("Beans");
        i = ingDBO.getIngredientById(i.getId());
        assertTrue(i.getName().equals("Beans"));
        assertTrue(i.getCode().equals("BEANS"));
    }

    @Test
    void testCheckDuplicateIngredientsWithSameIngredient() throws SQLException {
        Ingredient i1 = new Ingredient();
        i1.setName("Beef");
        i1.setCode("BEEF");
        i1.setQuantityMeasuredIn("gr");

        Ingredient i2 = new Ingredient();
        i2.setName("Beef");
        i2.setCode("BEEF");
        i2.setQuantityMeasuredIn("gr");

        ingDBO.insertIngredient(i1);

        assertTrue(ingDBO.checkDuplicateIngredient(i2));
    }

    @Test
    void testCheckDuplicateIngredientsWithDifferentIngredient() throws SQLException {
        Ingredient i1 = new Ingredient();
        i1.setName("Chicken");
        i1.setCode("CHICKEN");
        i1.setQuantityMeasuredIn("gr");

        Ingredient i2 = new Ingredient();
        i2.setName("Chicken Grounded");
        i2.setCode("CHICKENG");
        i2.setQuantityMeasuredIn("gr");

        ingDBO.insertIngredient(i1);

        assertFalse(ingDBO.checkDuplicateIngredient(i2));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, Integer.MAX_VALUE})
    void updateIngredientAmountToGreaterThanZero(int amount) throws SQLException {
        Ingredient i = new Ingredient();
        i.setName("Rice" + amount);
        i.setCode("RICE" + amount);
        i.setQuantityMeasuredIn("gr");

        ingDBO.insertIngredient(i);
        i = ingDBO.getIngredientsByName("Rice" + amount);
        assertTrue(i.getAmount() == 0);

        ingDBO.updateIngredientAmount(i, 10);
        i = ingDBO.getIngredientsByName("Rice" + amount);
        assertTrue(i.getAmount() == 10);
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "", "Really Really Long Lon LOOOOOOOOONG Long Name", "\uD83E\uDD14"})
    void testDeleteIngredient(String name) throws SQLException {
        Ingredient i = new Ingredient();
        i.setName(name);
        i.setCode(name);
        i.setQuantityMeasuredIn(name);

        ingDBO.insertIngredient(i);
        i = ingDBO.getIngredientsByName(name);
        ingDBO.deleteExistingIngredient(i.getId());
        ArrayList<Ingredient> ingredients = ingDBO.getAllIngredients();
        ArrayList<String> names = new ArrayList<String>();

        ingredients.forEach( (ing) -> names.add(ing.getName()));
        assertFalse(names.contains(name));
    }

    @Test
    void testUpdateIngredient() throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Egg");
        ingredient.setCode("Egg");
        ingredient.setAmount(100);
        ingDBO.insertIngredient(ingredient);

        Ingredient updatedIngredient = new Ingredient();
        updatedIngredient.setName("UpdatedEgg");
        updatedIngredient.setCode("Egg");
        updatedIngredient.setAmount(200);
        ingDBO.updateIngredient(updatedIngredient);
        Ingredient newIngredient = ingDBO.getIngredientsByCode("Egg");
        assertTrue(newIngredient.getName().equals("UpdatedEgg"));
        assertTrue(newIngredient.getAmount() == 200);
    }


}
