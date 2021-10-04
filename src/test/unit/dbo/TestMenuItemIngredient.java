package unit.dbo;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.IngredientDBOperations;
import data.db.MenuItemDBOperations;
import data.db.MenuItemIngredientDBOperations;
import managers.DataManager;
import managers.IngredientManager;
import managers.MenuItemManager;
import models.Ingredient;
import models.MenuItem;
import models.MenuItemIngredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMenuItemIngredient {

    private static ConnectionSource connection;
    private static MenuItemDBOperations menuItemDBO;
    private static MenuItemManager menuItemManager;
    private static IngredientDBOperations ingredientDBO;
    private static IngredientManager ingredientManager;
    private static MenuItemIngredientDBOperations menuItemIngredientDBO;
    private static DataManager dataManager;

    @BeforeAll
    static void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        menuItemIngredientDBO = new MenuItemIngredientDBOperations(connection);
        ingredientDBO = new IngredientDBOperations(connection);
        menuItemDBO = new MenuItemDBOperations(connection);
        menuItemManager = new MenuItemManager();
        ingredientManager = new IngredientManager();
        dataManager = new DataManager();
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
    }

    @Test
    public void testInsertMenuItemIngredienttoDB() throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("item");
        menuItem.setCode("item");
        menuItem.setCategory("category1");
        menuItemDBO.insertMenuItem(menuItem);

        Ingredient ingredient = new Ingredient();
        ingredient.setName("ingredient");
        ingredient.setCode("ingredient");
        ingredient.setQuantityMeasuredIn("count");

        ingredientDBO.insertIngredient(ingredient);

        menuItemIngredientDBO.insertMenuItemIngredient(menuItem, ingredient, 2);

        ArrayList<MenuItemIngredient> menuItemIngredients = menuItemIngredientDBO.getAllMenuItemIngredients();
        System.out.println(menuItemIngredients.get(0).getMenuItem().getId());
        assertNotNull(menuItemIngredients.get(0).getMenuItem());
    }

    @Test
    public void testGetMenuItemIngredients() throws SQLException {
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setName("item1");
        menuItem1.setCode("item1");
        menuItem1.setCategory("category1");

        menuItemDBO.insertMenuItem(menuItem1);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("ingredient1");
        ingredient1.setCode("ingredient1");
        ingredient1.setQuantityMeasuredIn("count");
        ingredientDBO.insertIngredient(ingredient1);

        MenuItem menuItem2 = new MenuItem();
        menuItem2.setName("item2");
        menuItem2.setCode("item2");
        menuItem2.setCategory("category2");
        menuItemDBO.insertMenuItem(menuItem2);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("ingredient2");
        ingredient2.setCode("ingredient2");
        ingredient2.setQuantityMeasuredIn("count");
        ingredientDBO.insertIngredient(ingredient2);

        menuItemIngredientDBO.insertMenuItemIngredient(menuItem1, ingredient1, 2);
        menuItemIngredientDBO.insertMenuItemIngredient(menuItem2, ingredient2, 2);

        HashMap<Ingredient, Integer> specificmenuItemIngredients = menuItemIngredientDBO.getMenuItemIngredients(menuItem1);
        ArrayList<String> ingredientNames = new ArrayList<String>();
        specificmenuItemIngredients.keySet().forEach( (ing) -> ingredientNames.add(ing.getName()));

        assertTrue(ingredientNames.contains("ingredient1"));
        assertFalse(ingredientNames.contains("ingredient2"));

    }



}

