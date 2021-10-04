package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.Ingredient;
import models.MenuItem;
import models.MenuItemIngredient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MenuItemIngredientDBOperations provides functions to interact with
 * the RECIPEINGREDIENT table
 */
public class MenuItemIngredientDBOperations {

    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the RECIPEINGREDIENT table
     */
    private Dao<MenuItemIngredient, Integer> menuItemIngredientDao;

    /**
     * Interface that handles queries to the MENUITEM table
     */
    private Dao<MenuItem, Integer> menuItemDao;

    /**
     * Interface that handles queries to the INGREDIENTS table
     */
    private IngredientDBOperations ingredientDBO;

    /**
     * Default constructor for MenuItemIngredientDBOperations
     * @param connectionSource
     */
    public MenuItemIngredientDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            menuItemIngredientDao = DaoManager.createDao(connectionSource, MenuItemIngredient.class);
            menuItemDao = DaoManager.createDao(connectionSource, MenuItem.class);
            ingredientDBO = new IngredientDBOperations(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a single mapping of Ingredient to MenuItem
     * @param menuItem MenuItem
     * @param ingredient Ingredient
     * @param amount Amount of ingredient needed for menuItem
     * @throws SQLException
     */
    public void insertMenuItemIngredient(MenuItem menuItem, Ingredient ingredient, int amount) throws SQLException {
        MenuItemIngredient menuItemIngredient = new MenuItemIngredient(menuItem, ingredient, amount);
        menuItemIngredientDao.create(menuItemIngredient);
    }

    /**
     * Get all menuItems and their ingredients
     * @return ArrayList of MenuItemIngredients
     * @throws SQLException
     */
    public ArrayList<MenuItemIngredient> getAllMenuItemIngredients() throws SQLException {
        ArrayList<MenuItemIngredient> RIRelation = new ArrayList<>(menuItemIngredientDao.queryForAll());
        return RIRelation;
    }

    /**
     * Get a list of ingredients needed for a MenuItem
     * @param menuItem MenuItem
     * @return ArrayList of Ingredient
     * @throws SQLException
     */
    public HashMap<Ingredient, Integer> getMenuItemIngredients(MenuItem menuItem) throws SQLException {
        QueryBuilder<MenuItemIngredient, Integer> riQB = menuItemIngredientDao.queryBuilder();
        List<MenuItemIngredient> res = riQB.where().eq("menuItem_id", menuItem).query();
        HashMap<Ingredient, Integer> ingredients = new HashMap<>();
        for (MenuItemIngredient r : res) {
        	Ingredient i = ingredientDBO.getIngredientById(r.getIngredient().getId());
            ingredients.put(i, r.getAmount());
        }
        return ingredients;
    }

    /**
     * Given an Ingredient, return all the Menu Items in the database that requires this Ingredient
     * @param ingredient Ingredient in query
     * @return List of Menu Items that require the Ingredient
     * @throws SQLException
     */
    public ArrayList<MenuItem> getIngredientMenuItems(Ingredient ingredient) throws SQLException {
        QueryBuilder<MenuItemIngredient, Integer> riQB = menuItemIngredientDao.queryBuilder();
        List<MenuItemIngredient> res = riQB.where().eq("ingredient_id", ingredient).query();
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        for (MenuItemIngredient r : res) {
            menuItems.add(r.getMenuItem());
        }

        return menuItems;
    }

    /**
     * Given a Menu Item, delete all Ingredients that is associated with it
     * @param menuItem Menu Item in query
     * @throws SQLException
     */
    public void deleteMenuItemIngredients(MenuItem menuItem) throws SQLException {
        DeleteBuilder<MenuItemIngredient, Integer> miDel = menuItemIngredientDao.deleteBuilder();
        miDel.where().eq("menuItem_id", menuItem.getId());
        menuItemIngredientDao.delete(miDel.prepare());
    }

    /**
     * Updates the Ingredients of a Menu Item in the database
     * @param menuItem Menu Item in query
     * @param ingredients new set of Ingredients
     * @throws SQLException
     */
    public void updateMenuItemIngredients(MenuItem menuItem, HashMap<String, Integer> ingredients) throws SQLException {
        ArrayList<MenuItem> menus = new ArrayList<>(menuItemDao.queryForEq("code", menuItem.getCode()));
        MenuItem mQuery = null;
        if (menus.size() == 0)
        	return;

        mQuery = menus.get(0);
        deleteMenuItemIngredients(mQuery);
        for (Map.Entry<String, Integer> i : ingredients.entrySet()) {
            String ingCode = i.getKey();
            Integer amount = i.getValue();
            Ingredient ingredient = ingredientDBO.getIngredientsByCode(ingCode);
            insertMenuItemIngredient(mQuery, ingredient, amount);
        }
    }

    /**
     * Delete a MenuItemIngedient by its Id
     * @param id Id to be deleted
     * @throws SQLException
     */
    public void deleteById(Integer id) throws SQLException {
        menuItemIngredientDao.deleteById(id);
    }

    /**
     * Checks if MenuItemIngredient entry already exists in database
     * @param menuItem MenuItem
     * @param ingredient Ingredient
     * @return True if already exists, false otherwise
     * @throws SQLException
     */
    public boolean checkDuplicate(MenuItem menuItem, Ingredient ingredient) throws SQLException {
        ArrayList<MenuItemIngredient> menuItemIngredients = new ArrayList<>(menuItemIngredientDao.queryBuilder()
                .where()
                .eq("menuItem_id", menuItem.getId()).and()
                .eq("ingredient_id", ingredient.getId())
                .query());
        return menuItemIngredients.size() > 0;
    }

    /**
     * Delete all entries in the relationship containing this Ingredient
     * Effectively removing it from all the Menu Item that requires it
     * @param ingredientID Ingredient ID to be deleted
     * @throws SQLException
     */
    public void deleteByIngredientID(int ingredientID) throws SQLException {
        DeleteBuilder<MenuItemIngredient, Integer> miDel = menuItemIngredientDao.deleteBuilder();
        miDel.where().eq("ingredient_id", ingredientID);
        menuItemIngredientDao.delete(miDel.prepare());
    }

    /**
     * Delete all entries in the relationship containing this Ingredient
     * Effectively removing it from all the Menu Item that requires it
     * @param menuItemID Menu Item ID to be deleted
     * @throws SQLException
     */
    public void deleteByMenuItemID(int menuItemID) throws SQLException {
        DeleteBuilder<MenuItemIngredient, Integer> miDel = menuItemIngredientDao.deleteBuilder();
        miDel.where().eq("menuItem_id", menuItemID);
        menuItemIngredientDao.delete(miDel.prepare());
    }

}
