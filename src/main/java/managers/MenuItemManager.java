package managers;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.MenuItemDBOperations;
import data.db.MenuItemIngredientDBOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Ingredient;
import models.MenuItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Menu Item Manager handles all the operations related to MENUITEM table
 */
public class MenuItemManager {

    private static final MenuItemManager menuItemManager = new MenuItemManager();

    private DatabaseOperator db = new DatabaseOperator();
    private ConnectionSource connectionSource = db.establishConnection();

    private MenuItemDBOperations menuItemDBO = new MenuItemDBOperations(connectionSource);
    private MenuItemIngredientDBOperations menuItemIngredientDBO = new MenuItemIngredientDBOperations(connectionSource);

    private ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
    private ObservableList<String> categories = FXCollections.observableArrayList();

    /**
     * Default constructor for MenuItemManager
     */
    public MenuItemManager() {
        try {
            menuItems.addAll(menuItemDBO.getAllMenuItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Static function to get the existing MenuItemManager
     * @return Existing MenuItem Manager
     */
    public static MenuItemManager getMenuItemManager() {
        return menuItemManager;
    }

    /**
     * Adds a list of Menu Items to the database.
     * @param menuItems list of menu items to be added
     */
    public void addMenuItems(Iterable<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            addMenuItem(menuItem);
        }
    }

    /**
     * Adds a menu Item to the database.
     * @param menuItem Menu Item to be added
     */
    public void addMenuItem(MenuItem menuItem) {
        try {
            // Do not add duplicate ingredients to the database
            menuItemDBO.insertOrUpdateMenuItem(menuItem);
            // Database error
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return List of Menu Item, useful for Javafx tables
     */
    public ObservableList<MenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * @return List of Menu Item Categories, useful for Javafx tables
     */
    public ObservableList<String> getMenuItemCategories() {
        return categories;
    }

    /**
     * Sync the categories for Javafx tables with categories from the DB
     */
    public void setMenuItemCategories() {
        try {
            categories = FXCollections.observableArrayList(menuItemDBO.getMenuCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the list of Menu Items in DataManager with the Menu Items in the database.
     */
    public void refreshMenuItems() throws SQLException {
        menuItems.setAll(menuItemDBO.getAllMenuItems());
    }

    /**
     * Updates the Menu Item.
     * @param menuItem the Menu Item with new values to update.
     */
    public void updateMenuItem(MenuItem menuItem) {
        try {
            menuItemDBO.updateMenuItem(menuItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets a Menu Item from the database by Name
     * @param name the name of the Menu Item to find.
     */
    public MenuItem getMenuItemByName(String name) throws SQLException {
        MenuItem menuItem = menuItemDBO.getMenuItemByName(name);
        return menuItem;
    }

    /**
     * Add Ingredients to a MenuItem
     * @param menuItem Menu Item
     * @param addedIngredients Ingredients to be added
     * @throws SQLException
     */
    public void addMenuItemIngredients(MenuItem menuItem, HashMap<Ingredient, Integer> addedIngredients) {
        menuItem.setIngredients(addedIngredients);
    }

    /**
     * Updates a Menu Item's set of Ingredients
     * @param currentMenuItem Menu Item to be updated
     * @param updatedIngredients New set of Ingredients
     * @throws SQLException
     */
    public void updateMenuItemIngredients(MenuItem currentMenuItem, HashMap<Ingredient, Integer> updatedIngredients) throws SQLException {
        addMenuItemIngredients(currentMenuItem, updatedIngredients);
        currentMenuItem.setIngredients(updatedIngredients);
        currentMenuItem.setIngredientsToIngredientByCode();
        menuItemIngredientDBO.updateMenuItemIngredients(currentMenuItem, currentMenuItem.getIngredientsByCode());
    }

    /**
     * Returns list of Ingredients associated with a Menu Item from the database
     * @param menuItem Menu Item in question
     * @return List of Ingredients associated with the Menu Item
     * @throws SQLException
     */
    public HashMap<Ingredient, Integer> getMenuItemIngredients(MenuItem menuItem) throws SQLException {
        return menuItemIngredientDBO.getMenuItemIngredients(menuItem);
    }

    public MenuItem getMenuItemByCode(String code) throws SQLException {
        return menuItemDBO.getMenuItemByCode(code);
    }

    /**
     * Deletes the Menu Item from the database
     * @param menuItem the Menu Item to be deleted
     */
    public void deleteMenuItem(MenuItem menuItem) {
        try {
            int menuItemID = menuItem.getId();
            menuItemDBO.deleteExistingMenuItemByCode(menuItem.getCode());
            menuItemIngredientDBO.deleteByMenuItemID(menuItemID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the servings amounts of a Menu Item
     * @param item Menu Item to be updated
     * @param itemServings New Menu Item amount
     * @throws SQLException
     */
    public void updateMenuItemServings(MenuItem item, Integer itemServings) throws SQLException {
            menuItemDBO.updateMenuItemServings(item, itemServings);
    }

    /**
     * @return List of all Menu Item in the Database
     * @throws SQLException
     */
    public ArrayList<MenuItem> getAllMenuItems() throws SQLException {
        return menuItemDBO.getAllMenuItems();
    }

    /**
     * @return Menu Item in the DB with associated ID
     * @throws SQLException
     */
    public MenuItem getMenuItemById(Integer id) throws SQLException {
        return menuItemDBO.getMenuItemById(id);
    }

    /**
     * Checks a MenuItem's dietary status whether it is gluten free, vegetarian or vegan
     * @param ingredients list of ingredients to calculate dietary options.
     * @return True if dietary status is fulfilled, False otherwise
     * @throws SQLException
     */
    public ArrayList<String> getMenuItemDietaryStatus(HashMap<Ingredient, Integer> ingredients) throws SQLException {
        ArrayList<String> finalList = new ArrayList<>();
        boolean isGF = true;
        boolean isVege = true;
        boolean isVegan = true;
        for (Ingredient i : ingredients.keySet()) {
            if (ingredients.get(i) != 0) {
                if (i.getGlutenFree() == false) {
                    isGF = false;
                }
                if (i.getVegetarian() == false) {
                    isVege = false;
                }
                if (i.getVegan() == false) {
                    isVegan = false;
                }
            }

        }
        if (isGF == true) {
            finalList.add("true");
        } else {
            finalList.add("false");
        }
        if (isVege == true) {
            finalList.add("true");
        } else {
            finalList.add("false");
        }
        if (isVegan == true) {
            finalList.add("true");
        } else {
            finalList.add("false");
        }
        return finalList;
    }

}
