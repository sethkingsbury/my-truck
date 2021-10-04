package managers;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.IngredientDBOperations;
import data.db.MenuItemIngredientDBOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Ingredient;
import models.MenuItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Ingredient Manager handles all the operations related to Ingredients table
 */
public class IngredientManager {

    private DatabaseOperator db = new DatabaseOperator();
    private ConnectionSource connectionSource = db.establishConnection();

    /**
     * Creates a new static IngredientManager
     */
    private static final IngredientManager ingredientManager = new IngredientManager();

    /**
     * Handles all the operations to interact with INGREDIENT table
     */
    private IngredientDBOperations ingredientDBO = new IngredientDBOperations(connectionSource);

    /**
     * Handles all the operations to interact with MENUITEMINGREDIENT table
     */
    private MenuItemIngredientDBOperations menuItemIngDBO = new MenuItemIngredientDBOperations(connectionSource);

    /**
     * List of Ingredients to be shown on Javafx table
     */
    private ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

    /**
     * Default constructor for IngredientManager
     */
    public IngredientManager() {
        try {
            ingredients.addAll(ingredientDBO.getAllIngredients());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Static function to get the existing Ingredient Manager
     * @return Existing Ingredient Manager
     */
    public static IngredientManager getIngredientManager() {
        return ingredientManager;
    }

    /**
     * Adds an ingredient to database
     * @param ingredient The new ingredient to be added
     */
    public void addIngredient(Ingredient ingredient) {
        try {
            // Do not add duplicate ingredients to the database
            ingredientDBO.insertOrUpdateIngredient(ingredient);
            // Database error
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches the database for an ingredient by its ingredient id.
     * @param id the ingredient id to search the database with
     * @return ingredient The ingredient that has id as its ingredient id.
     */
    public Ingredient getIngredient(Integer id) {
        Ingredient i = null;
        try {
            i = ingredientDBO.getIngredientById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * Retrieves an Ingredient the database with the same code
     * @param code Code of Ingredient
     * @return Ingredient in database
     */
    public Ingredient getIngredientByCode(String code) {
        Ingredient ingredient = null;
        try {
            ingredient = ingredientDBO.getIngredientsByCode(code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredient;
    }

    /**
     * Adds a list of ingredients to the database.
     * @param ingredients list of ingredients to be added
     */
    public void addIngredients(Iterable<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
        }
    }

    /**
     * Updates the ingredient.
     * @param ingredient the ingredient with new values to update.
     */
    public void updateIngredient(Ingredient ingredient) {
        try {
            ingredientDBO.updateIngredient(ingredient);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the ingredient from the database
     * @param ingredient the Ingredient to be deleted
     */
    public void deleteIngredient(Ingredient ingredient) {
        try {
        	int ingredientID = ingredient.getId();
            menuItemIngDBO.deleteByIngredientID(ingredientID);
            ingredientDBO.deleteExistingIngredient(ingredientID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return List of ingredients in the database, used for tables in Javafx
     */
    public ObservableList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Refreshes the list of ingredients in DataManager with the ingredients in the database.
     */
    public void refreshIngredients() throws SQLException {
        ingredients.setAll(ingredientDBO.getAllIngredients());
    }

    /**
     * Updates ingredient stock amount by a quantity of menu items
     * @param item Menu item of recipe.
     * @param quantity quantity of menu item to change stock by
     * @throws SQLException
     */
    public void refreshStockIngredientAmounts(MenuItem item, int quantity) throws SQLException {
        for (Ingredient ingredient : item.getIngredients().keySet()) {
            Ingredient stockIngredient = ingredientDBO.getIngredientsByCode(ingredient.getCode());

            if (stockIngredient.getAmount() + (quantity * item.getIngredients().get(ingredient)) > 0) {
                ingredientDBO.updateIngredientAmount(stockIngredient, (stockIngredient.getAmount() + (quantity * item.getIngredients().get(ingredient))));
            } else {
                ingredientDBO.updateIngredientAmount(stockIngredient, 0);
            }
        }
    }

    /**
     * Updates the amount of ingredients in the database with the new updated ones
     * @param ingredients Updated ingredients
     * @throws SQLException
     */
    public void refreshStockIngredientAmountsbyIngredients(HashMap<Ingredient, Integer> ingredients) throws SQLException {
        for (Ingredient ingredient :ingredients.keySet()) {
            Ingredient stockIngredient = ingredientDBO.getIngredientsByCode(ingredient.getCode());

            if (stockIngredient.getAmount() + (ingredients.get(ingredient)) > 0) {
                ingredientDBO.updateIngredientAmount(stockIngredient, (stockIngredient.getAmount() + (ingredients.get(ingredient))));
            } else {
                ingredientDBO.updateIngredientAmount(stockIngredient, 0);
            }
        }
    }

    /**
     * Update single ingredient stock amount
     * @param ingredient ingredient to update stock amount
     * @param quantity amount to change stock amount by
     * @throws SQLException
     */
    public void updateStockAmount(Ingredient ingredient, int quantity) throws SQLException {
        ingredientDBO.updateIngredientAmount(ingredient, ingredientDBO.getIngredientsByCode(ingredient.getCode()).getAmount() + quantity);
    }

    /**
     * Get all Menu Items that are associated with an Ingredient
     * @param ingredient Ingredient in question
     * @return List of Menu Items associated
     * @throws SQLException
     */
    public ArrayList<MenuItem> getAllIngredientMenuItems(Ingredient ingredient) throws SQLException {
        Ingredient i = getIngredientByCode(ingredient.getCode());
        return menuItemIngDBO.getIngredientMenuItems(i);
    }

    /**
     * Returns all ingredients in the database
     * @return List of all ingredients in the DB
     * @throws SQLException
     */
    public ArrayList<Ingredient> getAllIngredients() throws SQLException {
        return ingredientDBO.getAllIngredients();
    }
}
