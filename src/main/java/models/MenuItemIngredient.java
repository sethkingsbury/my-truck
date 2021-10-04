package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Model for relation between Menu Item and Ingredients
 */
@DatabaseTable(tableName = "MENUITEMINGREDIENT")
public class MenuItemIngredient {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(
            foreign = true,
            uniqueCombo = true,
            columnDefinition = "INTEGER REFERENCES INGREDIENTS (ID) ON DELETE CASCADE")
    private Ingredient ingredient;

    @DatabaseField(foreign = true, uniqueCombo = true)
    private MenuItem menuItem;

    @DatabaseField()
    private int amount;

    /**
     * Empty constructor to keep ORMLite happy.
     */
    public MenuItemIngredient() { } {

    }

    /**
     * Constructs a menuItem ingredient without an amount
     * @param menuItem that the ingredient is used in
     * @param ingredient that belongs to the menuItem
     */
    public MenuItemIngredient(MenuItem menuItem, Ingredient ingredient) {
        this.ingredient = ingredient;
        this.menuItem = menuItem;
        this.amount = 0;
    }

    /**
     * Constructs a menuItem ingredient with an amount
     * @param menuItem that the ingredient is used in
     * @param ingredient that belongs to the menuItem
     * @param amount of the ingredient to add to the menuItem
     */
    public MenuItemIngredient(MenuItem menuItem, Ingredient ingredient, int amount) {
        this.ingredient = ingredient;
        this.menuItem = menuItem;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String toString() {
        String template = "";
        template += "\t" + this.menuItem.getId();
        template += "\t" + this.ingredient.getId();
        template += "\t" + this.amount;
        return template;
    }
}
