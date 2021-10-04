package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.Ingredient;
import models.MenuItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * IngredientDBOperations provides functions to interact with
 * the Ingredient table
 */
public class MenuItemDBOperations {

    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the MENUITEM table
     */
    private Dao<MenuItem, Integer> menuItemDao;

    /**
     * Interface that handles queries to the MENUITEMINGREDIENT table
     */
    private MenuItemIngredientDBOperations menuItemIngredientDBO;

    /**
     * Interface that handles queries to the INGREDIENT table
     */
    private IngredientDBOperations ingredientDBO;

    /**
     * Default constructor for MenuItemDBOperations
     * @param connectionSource
     */
    public MenuItemDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            menuItemDao = DaoManager.createDao(connectionSource, MenuItem.class);
            menuItemIngredientDBO = new MenuItemIngredientDBOperations(connectionSource);
            ingredientDBO = new IngredientDBOperations(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all MenuItem(s) from the database
     * @return ArrayList of all the MenuItems in database
     * @throws SQLException
     */
    public ArrayList<MenuItem> getAllMenuItems() throws SQLException {
        ArrayList<MenuItem> menuItems = new ArrayList<>(menuItemDao.queryForAll());
        for (MenuItem m : menuItems) {
            HashMap<Ingredient, Integer> ingredients = menuItemIngredientDBO.getMenuItemIngredients(m);
            m.setIngredients(ingredients);
        }
        return menuItems;
    }

    /**
     * Get a MenuItem from DB with matching name
     * @param name Name of MenuItem
     * @return MenuItem found in database
     * @throws SQLException
     */
    public MenuItem getMenuItemByName(String name) throws SQLException {
        ArrayList<MenuItem> menus = new ArrayList<>(menuItemDao.queryForEq("name", name));
        if (menus.size() != 0)
            return menus.get(0);

        return null;
    }

    /**
     * Get a MenuItem from DB with matching Id
     * @param id Id of MenuItem in database
     * @return MenuItem found in database
     * @throws SQLException
     */
    public MenuItem getMenuItemById(Integer id) throws SQLException {
        return menuItemDao.queryForId(id);
    }

    /**
     * Inserts or updates Menu Item in the database depending if
     * the Menu Item exists or not
     * @param m Menu Item to be inserted or updated
     * @throws SQLException
     */
    public void insertOrUpdateMenuItem(MenuItem m) throws SQLException {
        if (checkDuplicateMenuItem(m)) {
            updateMenuItem(m);
        } else {
            insertMenuItem(m);
        }
    }

    /**
     * Insert a new MenuItem to database
     * @param m MenuItem to be inserted
     * @throws SQLException
     */
    public void insertMenuItem(MenuItem m) throws SQLException {
        menuItemDao.create(m);
        HashMap<String, Integer> menuItemIngredients = m.getIngredientsByCode();
        for (Map.Entry<String, Integer> ingredient : menuItemIngredients.entrySet()) {
        	String ingredientCode = ingredient.getKey();
        	Integer amount = ingredient.getValue();
        	Ingredient i = new Ingredient();
        	i.setName(ingredientCode);
        	i.setCode(ingredientCode);
        	i.setQuantityMeasuredIn("amount");
        	try {
                ingredientDBO.insertIngredient(i);
            } catch (SQLException e) {
        	    // Do nothing here
            };

        	MenuItem mQuery = getMenuItemByCode(m.getCode());
        	Ingredient iQuery = ingredientDBO.getIngredientsByCode(ingredientCode);
        	menuItemIngredientDBO.insertMenuItemIngredient(mQuery, iQuery, amount);
        }
    }

    /**
     * Deletes an existing Menu Item in the Database by Menu Item's code
     * @param code Code of Menu Item to be deleted
     * @throws SQLException
     */
    public void deleteExistingMenuItemByCode(String code) throws SQLException {
        DeleteBuilder<MenuItem, Integer> miDel = menuItemDao.deleteBuilder();
        miDel.where().eq("code", code);
        menuItemDao.delete(miDel.prepare());
    }

    /**
     * Updates a Menu Item in the database
     * The new Menu Item needs to have the same code with the old one
     * @param menuItem Updated Menu Item object with the same code
     * @throws SQLException
     */
    public void updateMenuItem(MenuItem menuItem) throws SQLException {
        UpdateBuilder<MenuItem, Integer> updateBuilder = menuItemDao.updateBuilder();

        updateBuilder.updateColumnValue("name", menuItem.getName());
        updateBuilder.where().eq("code", menuItem.getCode());

        updateBuilder.updateColumnValue("category", menuItem.getCategory());
        updateBuilder.where().eq("code", menuItem.getCode());

        updateBuilder.updateColumnValue("price", menuItem.getPriceinCents());
        updateBuilder.where().eq("code", menuItem.getCode());

        updateBuilder.updateColumnValue("isGF", menuItem.getisGF());
        updateBuilder.where().eq("code", menuItem.getCode());

        updateBuilder.updateColumnValue("isVege", menuItem.getisVege());
        updateBuilder.where().eq("code", menuItem.getCode());

        updateBuilder.updateColumnValue("isVegan", menuItem.getisVegan());
        updateBuilder.where().eq("code", menuItem.getCode());

        updateBuilder.updateColumnValue("imagePath", menuItem.getImagePath());
        updateBuilder.where().eq("code", menuItem.getCode());

        updateBuilder.update();

        MenuItem mInDB = getMenuItemByCode(menuItem.getCode());
        menuItemIngredientDBO.updateMenuItemIngredients(mInDB, menuItem.getIngredientsByCode());
    }

    /**
     * Checks if MenuItem is already in database
     * @param menuItem MenuItem in question
     * @return True if in database, false otherwise
     * @throws SQLException
     */
    public boolean checkDuplicateMenuItem(MenuItem menuItem) throws SQLException {
        ArrayList<MenuItem> similarMenuItem = new ArrayList<>(menuItemDao.queryForEq("code", menuItem.getCode()));
        return similarMenuItem.size() > 0;
    }

    /**
     * Get the categories of all menus
     * @return ArrayList of categories of menus
     * @throws SQLException
     */
    public ArrayList<String> getMenuCategories() throws SQLException {
        ArrayList<MenuItem> results = new ArrayList(menuItemDao.queryBuilder()
                                            .distinct()
                                            .selectColumns("category").query());
        ArrayList<String> categories = new ArrayList<>();
        results.forEach( (result) -> categories.add(result.getCategory()) );
        return categories;
    }
    /**
     *
     * Updates the category of MenuItem
     * @param menuItem MenuItem to be changed
     * @param category New category
     * @throws SQLException
     */
    public void updateMenuItemCategory(MenuItem menuItem, String category) throws SQLException {
        UpdateBuilder<MenuItem, Integer> menuItemUpdater = menuItemDao.updateBuilder();
        menuItemUpdater.where().eq("code", menuItem.getCode());
        menuItemUpdater.updateColumnValue("category", category);
        menuItemUpdater.update();
    }

    /**
     * Updates a Menu Item's serving amount
     * @param menuItem Menu Item to be updated
     * @param servings Updated serving amount
     * @throws SQLException
     */
    public void updateMenuItemServings(MenuItem menuItem, Integer servings) throws SQLException {
        UpdateBuilder<MenuItem, Integer> menuItemUpdater = menuItemDao.updateBuilder();
        menuItemUpdater.where().eq("code", menuItem.getCode());
        menuItemUpdater.updateColumnValue("servings", servings);
        menuItemUpdater.update();
    }

    /**
     * Updates the price of MenuItem
     * @param menuItem MenuItem to be changed
     * @param price New price
     * @throws SQLException
     */
    public void updatePriceAmount(MenuItem menuItem, Integer price) throws SQLException {
        if (price < 0) {
            throw new SQLException("Ingredient amount must be at least 0");
        }
        UpdateBuilder<MenuItem, Integer> menuItemUpdater = menuItemDao.updateBuilder();
        menuItemUpdater.where().eq("code", menuItem.getCode());
        menuItemUpdater.updateColumnValue("price", price);
        menuItemUpdater.update();
    }

    /**
     * Get all the codes for Menu Items
     * @return List of all the codes for Menu Items in the database
     * @throws SQLException
     */
    public ArrayList<String> getAllMenuItemCodes() throws SQLException {
        ArrayList<MenuItem> results = new ArrayList(menuItemDao.queryBuilder()
                .distinct()
                .selectColumns("code").query());

        ArrayList<String> codes = new ArrayList<>();
        results.forEach( (result) -> codes.add(result.getCode()));
        return codes;
    }

    /**
     * Get Menu Item by code
     * @param code Code of the Menu Item
     * @return Menu Item in the database with the same code
     * @throws SQLException
     */
    public MenuItem getMenuItemByCode(String code) throws SQLException {
        ArrayList<MenuItem> menus = new ArrayList<>(menuItemDao.queryForEq("code", code));
        if (menus.size() != 0)
            return menus.get(0);

        return null;
    }
}
