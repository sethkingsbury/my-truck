package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import models.Ingredient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * IngredientDBOperations provides functions to interact with
 * the Ingredient table
 */
public class IngredientDBOperations {

    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the INGREDIENT table
     */
    private Dao<Ingredient, Integer> ingredientDao;

    /**
     * Default constructor for IngredientDBOperations
     * @param connectionSource
     */
    public IngredientDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            ingredientDao = DaoManager.createDao(connectionSource, Ingredient.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert an ingredient to the table
     * @param ingredient
     * @throws SQLException
     */
    public void insertIngredient(Ingredient ingredient) throws SQLException {
        ingredientDao.create(ingredient);
    }

    /**
     * Insert an ingredient to the table
     * if the ingredient already exists in the table, update its value
     * @param ingredient
     * @throws SQLException
     */
    public void insertOrUpdateIngredient(Ingredient ingredient) throws SQLException {
        if (checkDuplicateIngredient(ingredient)) {
            updateIngredient(ingredient);
        } else {
            insertIngredient(ingredient);
        }
    }

    /**
     * Get an ingredient based on its ID
     * @param id ID of the ingredient
     * @return Ingredient found
     * @throws SQLException
     */
    public Ingredient getIngredientById(Integer id) throws SQLException {
        Ingredient i = ingredientDao.queryForId(id);
        return i;
    }

    /**
     * Update the properties of Ingredient object in the database
     * It needs to have the same code as the old Ingredient since it is the unique key of Ingredient
     * @param ingredient Updated ingredient object
     * @throws SQLException
     */
    public void updateIngredient(Ingredient ingredient) throws SQLException {
        UpdateBuilder<Ingredient, Integer> updateBuilder = ingredientDao.updateBuilder();
        updateBuilder.updateColumnValue("name", ingredient.getName());
        updateBuilder.where().eq("code", ingredient.getCode());

        updateBuilder.updateColumnValue("amount", ingredient.getAmount());
        updateBuilder.where().eq("code", ingredient.getCode());

        updateBuilder.updateColumnValue("price", ingredient.getPriceInt());
        updateBuilder.where().eq("code", ingredient.getCode());

        updateBuilder.updateColumnValue("quantityMeasuredIn", ingredient.getQuantityMeasuredIn());
        updateBuilder.where().eq("code", ingredient.getCode());

        updateBuilder.updateColumnValue("isGlutenFree", ingredient.getGlutenFree());
        updateBuilder.where().eq("code", ingredient.getCode());

        updateBuilder.updateColumnValue("isVegan", ingredient.getVegan());
        updateBuilder.where().eq("code", ingredient.getCode());

        updateBuilder.updateColumnValue("isVegetarian", ingredient.getVegetarian());
        updateBuilder.where().eq("code", ingredient.getCode());

        updateBuilder.update();
    }

    /**
     * Gets all the ingredients in the table
     * @return All the ingredients in table
     * @throws SQLException
     */
    public ArrayList<Ingredient> getAllIngredients() throws SQLException {
        ArrayList<Ingredient> ingredients = new ArrayList<>(ingredientDao.queryForAll());
        return ingredients;
    }

    /**
     * Get an ingredient based on its name
     * @param name Name of ingredient
     * @return Ingredient found
     * @throws SQLException
     */
    public Ingredient getIngredientsByName(String name) throws SQLException {
        List<Ingredient> ings = ingredientDao.queryForEq("name", name);
        if (ings.size() != 0)
            return ings.get(0);

        return null;
    }

    /**
     * Get an ingredient based on its code
     * @param code Code of ingredient
     * @return Ingredient found
     * @throws SQLException
     */
    public Ingredient getIngredientsByCode(String code) throws SQLException {
        List<Ingredient> ings = ingredientDao.queryForEq("code", code);
        if (ings.size() != 0)
            return ings.get(0);

        return null;
    }

    /**
     * Checks if Ingredient already existed in the database
     * @param ingredient Ingredient in question
     * @return True if duplicate, false otherwise
     * @throws SQLException
     */
    public boolean checkDuplicateIngredient(Ingredient ingredient) throws SQLException {
        ArrayList<Ingredient> similarIngredients = new ArrayList<>(ingredientDao.queryForEq("code", ingredient.getCode()));
        return similarIngredients.size() > 0;
    }

    /**
     * Deletes an existing ingredient
     * @param ingredientId Id of the ingredient
     * @throws SQLException
     */
    public void deleteExistingIngredient(Integer ingredientId) throws SQLException {
        ingredientDao.deleteById(ingredientId);
    }

    /**
     * Removes an Ingredient from the database based on its code
     * @param code Code of the ingredient
     * @throws SQLException
     */
    public void deleteExistingIngredientByCode(String code) throws SQLException {
        DeleteBuilder<Ingredient, Integer> ingDelDao = ingredientDao.deleteBuilder();
        ingDelDao.where().eq("code", code);
        ingredientDao.delete(ingDelDao.prepare());
    }

    /**
     * Updates the amount of ingredient in database
     * @param ingredient Ingredient in question
     * @param amount New ingredient amount
     * @throws SQLException
     */
    public void updateIngredientAmount(Ingredient ingredient, Integer amount) throws SQLException {
        //if (amount < 0) {
        //    throw new SQLException("Ingredient amount must be at least 0");
        //}
        UpdateBuilder<Ingredient, Integer> ingUpdater = ingredientDao.updateBuilder();
        ingUpdater.where().eq("code", ingredient.getCode());
        ingUpdater.updateColumnValue("amount", amount);
        ingUpdater.update();
    }

    /**
     * Returns an ArrayList list of ingredient codes
     * @throws SQLException
     */
    public ArrayList<String> getAllIngredientCodes() throws SQLException {
        ArrayList<Ingredient> results = new ArrayList(ingredientDao.queryBuilder()
                .distinct()
                .selectColumns("code").query());

        ArrayList<String> codes = new ArrayList<>();
        results.forEach( (result) -> codes.add(result.getCode()));
        return codes;
    }
}
