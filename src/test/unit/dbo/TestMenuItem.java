package unit.dbo;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.IngredientDBOperations;
import data.db.MenuItemDBOperations;
import managers.MenuItemManager;
import models.Ingredient;
import models.MenuItem;
import models.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class TestMenuItem {

    private static ConnectionSource connection;
    private static MenuItemDBOperations menuItemDBO;

    private static MenuItemManager menuItemManager;

    @BeforeAll
    static void setupDBConnection() {
        DatabaseOperator dbOperator = new DatabaseOperator();
        dbOperator.setDebugMode();
        connection = dbOperator.establishConnection();
        menuItemDBO = new MenuItemDBOperations(connection);
        menuItemManager = new MenuItemManager();
        System.setProperty("com.j256.ormlite.logger.level", "ERROR");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ReallyLongLongLongLongLongMenuName", "menuCarrot"})
    void testInsertMenuItemsToDBCustomName(String name) throws SQLException {
        MenuItem i = new MenuItem();
        i.setName(name);
        i.setCode(name);
        i.setPrice(new Money(10));
        i.setCategory(name);

        menuItemDBO.insertMenuItem(i); // should not throw exception

        i = menuItemDBO.getMenuItemByName(name);
        assertTrue(i.getName().equals(name));
        assertTrue(i.getCode().equals(name));
        assertTrue(i.getCategory().equals(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ChickpeasMenu", "HummusMenu", "BreadMenu"})
    void testGetAllMenuItems(String name) throws SQLException {
        MenuItem i = new MenuItem();
        i.setName(name);
        i.setCode(name);
        i.setPrice(new Money(10));
        i.setCategory(name);
        menuItemDBO.insertMenuItem(i);

        ArrayList<MenuItem> ings = menuItemDBO.getAllMenuItems();
        ArrayList<String> names = new ArrayList<String>();

        // Cool python lamba like function to change ingredients to their names
        ings.forEach( (ing) -> names.add(ing.getName()));
        assertTrue(names.contains(name));
    }

    @Test
    void testQueryNonExistingMenuItemShouldReturnNull() throws SQLException {
        MenuItem m = menuItemDBO.getMenuItemByName("IDIOTMENU");
        assertNull(m);
    }

    @Test
    void testQueryMenuItemsByIdAndName() throws SQLException {
        MenuItem i = new MenuItem();
        i.setName("menu1");
        i.setCode("MENU1");

        i.setPrice(new Money(10));
        i.setCategory("Main Course");
        menuItemDBO.insertMenuItem(i);

        i = menuItemDBO.getMenuItemByName("menu1");
        i = menuItemDBO.getMenuItemById(i.getId());

        assertEquals(i.getName(), "menu1");
        assertEquals(i.getCode(), "MENU1");
    }

    @Test
    void testCheckDuplicateMenuItemsWithSameMenuItems() throws SQLException {
        MenuItem i1 = new MenuItem();
        i1.setName("dinner");
        i1.setCode("DINNER");

        i1.setPrice(new Money(10));
        i1.setCategory("Main Course");

        MenuItem i2 = new MenuItem();
        i2.setName("dinner");
        i2.setCode("DINNER");
        i2.setPrice(new Money(10));
        i2.setCategory("Main Course");

        menuItemDBO.insertMenuItem(i1);

        assertTrue(menuItemDBO.checkDuplicateMenuItem(i2));
    }

    @Test
    void testCheckDuplicateMenuItemsWithDifferentMenuItems() throws SQLException {
        MenuItem i1 = new MenuItem();
        i1.setName("sakura");
        i1.setCode("SAKURA");
        i1.setPrice(new Money(10));
        i1.setCategory("Main Course");

        MenuItem i2 = new MenuItem();
        i2.setName("cherryblossom");
        i2.setCode("CHERRYBLOSSOM");
        i2.setPrice(new Money(10));
        i2.setCategory("Dessert");

        menuItemDBO.insertMenuItem(i1);

        assertFalse(menuItemDBO.checkDuplicateMenuItem(i2));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, Integer.MAX_VALUE})
    void updateMenuItemCostToGreaterThanZero(int price) throws SQLException {
        MenuItem i = new MenuItem();
        i.setName("menu1" + price);
        i.setCode("MENU1" + price);
        i.setPrice(new Money(10));
        i.setCategory("Main Course" + price);

        menuItemDBO.insertMenuItem(i);
        i = menuItemDBO.getMenuItemByName("menu1" + price);

        menuItemDBO.updatePriceAmount(i, 10);
        i = menuItemDBO.getMenuItemByName("menu1" + price);

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, Integer.MIN_VALUE})
    void updateIngredientCostToBelowZero(int amount) throws SQLException {
        MenuItem i = new MenuItem();
        i.setName("Rabbit" + -1 * amount);
        i.setCode("RABBIT" + -1 * amount);
        i.setCategory("RABBIT" + -1 * amount);
        menuItemDBO.insertMenuItem(i);
        try {
            menuItemDBO.updatePriceAmount(i, amount);
            fail("Should have thrown exception");
        } catch (SQLException e) {} // do nothing
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "", "Really Really Long Name", "\uD83E\uDD14"})
    void testDeleteIngredient(String name) throws SQLException { MenuItem i = new MenuItem(); i.setName(name);
        i.setCode(name);
        i.setCategory(name);

        menuItemDBO.insertMenuItem(i);
        i = menuItemDBO.getMenuItemByName(name);
        menuItemDBO.deleteExistingMenuItemByCode(i.getCode());
        ArrayList<MenuItem> menuItems = menuItemDBO.getAllMenuItems();
        ArrayList<String> names = new ArrayList<String>();

        menuItems.forEach( (ing) -> names.add(ing.getName()));
        assertFalse(names.contains(name));
    }

    @Test
    void testGettingGlutenFreeStatusShouldReturnPositive() throws SQLException {
        MenuItem m1 = new MenuItem();
        m1.setName("glutenFreeFriedRice");
        m1.setCode("glutenFreeFriedRice");
        m1.setPrice(new Money(10));
        m1.setCategory("Main Course");

        Ingredient ing1 = new Ingredient();
        ing1.setName("ING1");
        ing1.setCode("ING1");
        ing1.setGlutenFree(true);

        Ingredient ing2 = new Ingredient();
        ing2.setName("ING2");
        ing2.setCode("ING2");
        ing2.setGlutenFree(true);

        HashMap<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(ing1, 1);
        ingredients.put(ing2, 1);

        m1.setIngredients(ingredients);

        menuItemDBO.insertMenuItem(m1);

        assertTrue(menuItemManager.getMenuItemDietaryStatus(m1.getIngredients()).get(0).equals("true"));
    }

    @Test
    void testGettingGlutenFreeStatusShouldReturnNegative() throws SQLException {
        MenuItem m1 = new MenuItem();
        m1.setName("notGlutenFreeFriedRice");
        m1.setCode("notGlutenFreeFriedRice");
        m1.setPrice(new Money(10));
        m1.setCategory("Main Course");

        Ingredient ing1 = new Ingredient();
        ing1.setName("MAY1");
        ing1.setCode("MAY1");
        ing1.setGlutenFree(false);

        Ingredient ing2 = new Ingredient();
        ing2.setName("MAY2");
        ing2.setCode("MAY2");
        ing2.setGlutenFree(true);

        HashMap<Ingredient, Integer> ingredients = new HashMap<>();
        ingredients.put(ing1, 1);
        ingredients.put(ing2, 1);

        m1.setIngredients(ingredients);

        menuItemDBO.insertMenuItem(m1);

        assertFalse(menuItemManager.getMenuItemDietaryStatus(m1.getIngredients()).get(0).equals("true"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Sides", "Burger"})
    void testGetAllMenuItemCategories(String name) throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setCode(name);
        menuItem.setCategory(name);
        menuItemDBO.insertMenuItem(menuItem);

        ArrayList<String> categories = menuItemDBO.getMenuCategories();
        assertTrue(categories.contains(name));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5, 10, Integer.MAX_VALUE})
    void testUpdateMenuItemServings(int newAmount) throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Egg" + newAmount);
        menuItem.setCode("Egg" + newAmount);
        menuItem.setServings(0);
        menuItem.setCategory("EGG" + newAmount);

        menuItemDBO.insertMenuItem(menuItem);

        menuItemDBO.updateMenuItemServings(menuItem, newAmount);

        assertTrue((menuItemDBO.getMenuItemByCode("Egg" + newAmount)).getServings() == newAmount);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Burger", "Drink", "Chicken"})
    void testUpdateMenuItemServings(String newCategory) throws SQLException {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Egg");
        menuItem.setCode("Egg" + newCategory);
        menuItem.setCategory("none");

        menuItemDBO.insertMenuItem(menuItem);

        menuItemDBO.updateMenuItemCategory(menuItem, newCategory);

        assertTrue((menuItemDBO.getMenuItemByCode("Egg" + newCategory)).getCategory().equals(newCategory));
    }
}
