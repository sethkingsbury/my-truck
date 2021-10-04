package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Model for Menu Item
 */
@DatabaseTable(tableName = "MENUITEM")
public class MenuItem implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    private String code;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField()
    private int servings;

    @DatabaseField()
    private int price;

    @DatabaseField(canBeNull = false)
    private String category;

    @DatabaseField(defaultValue = "images/itemPlaceholder.png")
    private String imagePath = "images/itemPlaceholder.png";

    @DatabaseField()
    private boolean isGF;

    @DatabaseField()
    private boolean isVege;

    @DatabaseField()
    private boolean isVegan;

    private HashMap<String, Integer> ingredientsByCode = new HashMap<>();
    private HashMap<Ingredient, Integer> ingredients = new HashMap<>();

    /**
     * Empty constructor to keep ORMLite happy.
     */
    public MenuItem() {
    }

    /**
     * Constructor for the MenuItem class
     * @param code of the menu item
     * @param name of the menu item
     * @param price of the menu item
     * @param category that the menu item is in
     */
    public MenuItem(String code, String name, int price, String category) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.category = category;
        this.servings = 0;
    }

    /**
     * Constructor for the MenuItem class
     * @param code Code of menu item
     * @param name Name of menu item
     * @param price Money object of menu item
     * @param category Menu item category
     */
    public MenuItem(String code, String name, Money price, String category) {
        this.code = code;
        this.name = name;
        this.price = price.getAsCents();
        this.category = category;
        this.servings = 0;
    }

    /**
     * Constructor for the MenuItem class
     * @param code of the menu item
     * @param name of the menu item
     * @param price of the menu item
     * @param category that the menu item is in
     */
    public MenuItem(String code, String name, int price, String category, int servings) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.category = category;
        this.servings = servings;
    }

    /**
     * Constructor for the MenuItem class
     * @param code of the menu item
     * @param name of the menu item
     * @param price of the menu item
     * @param category that the menu item is in
     */
    public MenuItem(String code, String name, Money price, String category, boolean gf, boolean vege, boolean vegan) {
        this.code = code;
        this.name = name;
        this.price = price.getAsCents();
        this.category = category;
        this.servings = 0;
        this.isGF = gf;
        this.isVege = vege;
        this.isVegan = vegan;
    }

    /**
     * Constructor for Menu Item
     * @param code of the menu item
     * @param name of the menu item
     * @param price of the menu item
     * @param category that the menu item is in
     * @param servings total possible servings for this menu item
     */
    public MenuItem(String code, String name, Money price, String category, int servings) {
        this.code = code;
        this.name = name;
        this.price = price.getAsCents();
        this.category = category;
        this.servings = servings;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public Money getPrice() {
        return new Money(price);
    }

    public int getPriceinCents() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price.getAsCents();
    }

    public void setPrice(float price) {
        Money money = new Money(price);
        this.price = money.getAsCents();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public HashMap<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<Ingredient, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public HashMap<String, Integer> getIngredientsByCode() {
        return ingredientsByCode;
    }

    public void setIngredientsByCode(HashMap<String, Integer> ingredientsByCode) {
        this.ingredientsByCode = ingredientsByCode;
    }

    public ArrayList<Ingredient> getIngredientsAsList() {
        return new ArrayList<>(ingredients.keySet());
    }

    public boolean getisGF() {
        return isGF;
    }

    public void setGF(boolean GF) {
        isGF = GF;
    }

    public boolean getisVege() {
        return isVege;
    }

    public void setVege(boolean vege) {
        isVege = vege;
    }

    public boolean getisVegan() {
        return isVegan;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public void setIngredientsToIngredientByCode() {
        for (Ingredient i : ingredients.keySet()) {
            ingredientsByCode.put(i.getCode(), ingredients.get(i));
        }
    }

    /**
     * toString method for MenuItem
     * @return String representation for MenuItem
     */
    public String toString() {
        String template = "";
        template += "Menu name: " + this.name + "\n";
        template += "Menu code: " + this.code + "\n";
        template += "Menu price: " + this.price + "\n";
        template += "   Ingredients:\n";
        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            template += "       " + entry.getValue() + " x " + entry.getKey() + "\n";
        }
        return template;
    }

}
