package managers;

import com.j256.ormlite.support.ConnectionSource;
import data.db.*;
import data.xml.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * DataManager is a static Singleton class
 * that provides support for accessing data throughout
 * the data view controller.
 */
public class DataManager {

    private DatabaseOperator db = new DatabaseOperator();
    private ConnectionSource connectionSource = db.establishConnection();

    private static final DataManager dataManager = new DataManager();

    private IngredientManager ingredientManager = IngredientManager.getIngredientManager();
    private MenuItemManager menuItemManager = MenuItemManager.getMenuItemManager();
    private UserManager userManager = UserManager.getUserManager();

    // This should be converted to the factory pattern at some point
    private SupplierDBOperations supplierDBO = new SupplierDBOperations(connectionSource);
    private CashFloatDBOperations cashFloatDBO = new CashFloatDBOperations(connectionSource);

    private ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    private ObservableList<CashFloat> cashFloats = FXCollections.observableArrayList();

    /**
     * Default constructor for DataManager
     */
    public DataManager() {
        try {
            // TODO: Remove this once uploading to the database is implemented.
            //TEMP_populateDatabaseWithDebugData();

            ingredientManager.refreshIngredients();
            menuItemManager.refreshMenuItems();
            userManager.refreshUsers();
            refreshCashFloat();

            linkIngredientsToMenuItems();
            suppliers.addAll(supplierDBO.getAllSuppliers());

            ArrayList<CashFloat> wad = new ArrayList<>();
            wad.add(new CashFloat("Coin", 10, 10, 0));
            wad.add(new CashFloat("Coin", 20, 20, 0));
            wad.add(new CashFloat("Coin", 50, 50, 0));
            wad.add(new CashFloat("Coin", 1, 100, 0));
            wad.add(new CashFloat("Coin", 2, 200, 0));
            wad.add(new CashFloat("Note", 5, 500, 0));
            wad.add(new CashFloat("Note", 10, 1000, 0));
            wad.add(new CashFloat("Note", 20, 2000, 0));
            wad.add(new CashFloat("Note", 50, 5000, 0));
            wad.add(new CashFloat("Note", 100, 10000, 0));
            cashFloatDBO.addWadToCashFloat(wad);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            updateAllMenuItemServings(menuItemManager.getMenuItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Default constructor for DataManager
     */
    public static DataManager getDataManager() {
        return dataManager;
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    public boolean isRunningUsingJarfile() {
        final File jarFile = new File(getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath());

        return jarFile.isFile();
    }


    // TODO: Remove this once uploading to the database is implemented.
    public void TEMP_populateDatabaseWithDebugData() {
        String dataRootPath = "src/main/resources/";
        if (isRunningUsingJarfile()) {
            dataRootPath = "";
        }

        ArrayList<Ingredient> ingredients = new IngredientXMLReader(dataRootPath + "data/filler/Ingredients.xml").parseXML();
        ingredientManager.addIngredients(ingredients);

        ArrayList<MenuItem> menuItemsNew = new MenuItemXMLReader(dataRootPath + "data/filler/Menus.xml").parseXML();
        linkIngredientsToMenuItemsFromCodes(menuItemsNew);
        menuItemManager.addMenuItems(menuItemsNew);

        ArrayList<Supplier> suppliers = new SupplierXMLReader(dataRootPath + "data/filler/Suppliers.xml").parseXML();
        addSuppliers(suppliers);

        ArrayList<User> users = new UserXMLReader(dataRootPath + "data/filler/Users.xml").parseXML();
        userManager.createNewUsers(users);

        ArrayList<CashFloat> wad = new CashFloatXMLReader(dataRootPath + "data/filler/Cash.xml").parseXML();

        try {
            cashFloatDBO.addWadToCashFloat(wad);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Links MenuItemIngredients to menuItem by creating a ingredient map in menuItem.
     */
    public void linkIngredientsToMenuItems() {
        for (MenuItem menuItem : menuItemManager.getMenuItems()) {

            HashMap<Ingredient, Integer> ingredientsMap = null;
            try {
                ingredientsMap = menuItemManager.getMenuItemIngredients(menuItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            menuItem.setIngredients(ingredientsMap);
        }
    }

    /**
     * Links MenuItemIngredients to menuItem by creating a ingredient map in menuItem.
     */
    public void linkIngredientsToMenuItemsFromCodes(ArrayList<MenuItem> menuItemsNew) {
        for (MenuItem menuItem : menuItemsNew) {
            HashMap<String, Integer> ingredientsByCode = menuItem.getIngredientsByCode();
            HashMap<Ingredient, Integer> ingredientsMap = new HashMap<>();
            for (String ingredientCode : ingredientsByCode.keySet()) {
                ingredientsMap.put(ingredientManager.getIngredientByCode(ingredientCode),
                        ingredientsByCode.get(ingredientCode));
            }
            menuItem.setIngredients(ingredientsMap);
            try {
                menuItemManager.updateMenuItemIngredients(menuItem, ingredientsMap);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==== Supplier function calls ====

    /**
     * Deletes the Supplier from the database
     * @param supplier the supplier to be deleted
     */
    public void deleteExistingSupplier(Supplier supplier) {
        try {
            supplierDBO.deleteSupplier(supplier.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a supplier to the database.
     * @param supplier the supplier to be added
     */
    public void addSupplier(Supplier supplier) {
        try {
            // Do not add duplicate ingredients to the database
            supplierDBO.insertOrUpdateSupplier(supplier);
            // Database error
        } catch (SQLException e) { }
    }

    /**
     * Adds a list of supplier to the database.
     * @param suppliers list of the suppliers to be added
     */
    public void addSuppliers(ArrayList<Supplier> suppliers) {
        for (Supplier s : suppliers) {
            addSupplier(s);
        }
    }

    /**
     * @return List of suppliers, useful for Javafx tables
     */
    public ObservableList<Supplier> getSuppliers() {
        return suppliers;
    }

    /**
     * Refreshes the list of Suppliers in DataManager with the Suppliers in the database.
     */
    public void refreshSuppliers() throws SQLException {
        suppliers.setAll(supplierDBO.getAllSuppliers());
    }

    /**
     * Gets ArrayList of all suppliers names
     * @return
     * @throws SQLException
     */
    public ArrayList<String> getAllSupplierNames() throws SQLException {
        ArrayList<String> codes = supplierDBO.getAllSupplierNames();
        return codes;
    }

    /**
     * Calculates the total servings a Menu Item can have
     * @param menuItem
     * @return
     */
    public int calculateMenuItemServings(MenuItem menuItem) {
    	return calculateMenuItemServings(menuItem.getIngredients());
    }

    /**
     * Helper function to calculate Menu Item servings
     * @param ingredients Ingredients of the menu items
     * @return Number of possible servings given a set of Ingredients
     */
    public int calculateMenuItemServings(HashMap<Ingredient, Integer> ingredients) {
        int minimum = 10000;

        for(Ingredient ingredient : ingredients.keySet()) {
            Ingredient stockIngredient = null;
            stockIngredient = ingredientManager.getIngredientByCode(ingredient.getCode());
            if (ingredients.get(ingredient) > 0) {
                if (minimum > stockIngredient.getAmount() / ingredients.get(ingredient)) {

                    minimum = stockIngredient.getAmount() / ingredients.get(ingredient);
                }
            }
        }
        if (ingredients.isEmpty()) {
            minimum = 0;
        }
        return minimum;
    }

    /**
     * Goes through all the menu items specified and updates their possible servings
     * @param menuItems List of Menu Items
     * @throws SQLException
     */
    public void updateAllMenuItemServings(ObservableList<MenuItem> menuItems) throws SQLException {
        for (MenuItem item : menuItems) {
            Integer itemServings = calculateMenuItemServings(item.getIngredients());
            menuItemManager.updateMenuItemServings(item, itemServings);
        }
    }

    /*
    cashfloat functions
     */

    /**
     * gets the amount of the denomination of cash.
     * @param type If the cashfloat in question is a Note or Coin
     * @param denomination The denomination of the cashFloat.
     * @return the amount of the cashFloat w have.
     */
    public Integer getDenomQuantity(String type, Integer denomination) throws SQLException {
        return cashFloatDBO.getQuantity(type, denomination);
    }

    /**
     * Increase the amount of the cashFloat by an amount.
     * @param type If the cashfloat in question is a Note or Coin
     * @param denomination The denomination of the cashFloat.
     * @param quantityInc The amount to increase by.
     */
    public void increaseQuantity(String type, Integer denomination, Integer quantityInc) throws SQLException {
        cashFloatDBO.increaseQuantity(type, denomination, quantityInc);
    }

    /**
     * Decrease the amount of the cashFloat by an amount.
     * @param type If the cashfloat in question is a Note or Coin
     * @param denomination The denomination of the cashFloat.
     * @param quantityDec The amount to decrease by.
     */
    public void decreaseQuantity(String type, Integer denomination, Integer quantityDec) throws SQLException {
        cashFloatDBO.decreaseQuantity(type, denomination, quantityDec);
    }

    /**
     * Gets the cashfloat in cents.
     * @param type If the cashfloat in question is a Note or Coin
     * @param denomination The denomination of the cashFloat.
     * @return the cashFlaot in cents.
     */
    public Integer getFloatCents(String type, Integer denomination) throws SQLException {
        return cashFloatDBO.getFloatCents(type, denomination);
    }

    /**
     * Gets all Denominations of the cahsFloat database.
     * @return A list of all denominations
     */
    public ArrayList<CashFloat> getAllDenom() throws SQLException {
        return cashFloatDBO.getAllDenom();
    }

    /**
     * Set the amount of cash float
     * @param cash Cash float to be modified
     * @param quantity Updated quantity
     */
    public void setCashFloat(CashFloat cash, int quantity) {
        try {
            cashFloatDBO.setQuantity(String.valueOf(cash.getValueInCents()), quantity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Syncs the list of cash float from the database
     */
    public void refreshCashFloat() {
        try {
            cashFloats.setAll(cashFloatDBO.getAllDenom());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return List of cash floats, useful for Javafx table
     */
    public ObservableList<CashFloat> getCashFloats() {
        return cashFloats;
    }
}
