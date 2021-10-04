package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Model for Ingredietns
 */
@DatabaseTable(tableName = "INGREDIENTS")
public class Ingredient implements Serializable, Comparable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    private String code;

    @DatabaseField()
    private String name;

    @DatabaseField()
    private String quantityMeasuredIn;

    @DatabaseField()
    private int amount;

    @DatabaseField()
    private Boolean isGlutenFree = false;

    @DatabaseField()
    private Boolean isVegetarian = false;

    @DatabaseField()
    private Boolean isVegan = false;

    @DatabaseField
    private int price;

    public Ingredient() {
    }

    public Ingredient(String code, int amount) {
        this.code = code;
        this.amount = amount;
    }

    /**
     * Default constructor for Ingredient
     * @param code Code of ingredient
     * @param name Name of ingredient
     * @param quantityMeasuredIn Measurement of ingredient
     */
    public Ingredient(String code, String name, String quantityMeasuredIn) {
        this.code = code;
        this.name = name;
        this.quantityMeasuredIn = quantityMeasuredIn;
        this.isGlutenFree = false;
        this.isVegetarian = false;
        this.isVegan = false;
        this.amount = 0;
    }

    /**
     * Alternate constructor for Ingredient
     * @param code Code of ingredient
     * @param name Name of ingredient
     * @param quantityMeasuredIn Measurement of ingredient
     * @param gf gluten free status
     * @param veg vegetarian status
     * @param vegan vegan status
     */
    public Ingredient(String code, String name, String quantityMeasuredIn, Boolean gf, Boolean veg, Boolean vegan, int amount, int price) {
        this.code = code;
        this.name = name;
        this.quantityMeasuredIn = quantityMeasuredIn;
        this.isGlutenFree = gf;
        this.isVegetarian = veg;
        this.isVegan = vegan;
        this.amount = amount;
        this.price = price;
    }

    public Ingredient(String code, String name, String quantityMeasuredIn, Boolean gf, Boolean veg, Boolean vegan) {
        this.code = code;
        this.name = name;
        this.quantityMeasuredIn = quantityMeasuredIn;
        this.isGlutenFree = gf;
        this.isVegetarian = veg;
        this.isVegan = vegan;
        this.amount = 0;
        this.price = 0;
    }

    /**
     * Sets the name of Ingredient
     * @param name Name of ingredient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the quantity ingredient is measured in
     * @param quantityMeasuredIn Quantity ingredient is measured in
     */
    public void setQuantityMeasuredIn(String quantityMeasuredIn) {
        this.quantityMeasuredIn = quantityMeasuredIn;
    }

    /**
     * Sets the unique code of ingredient
     * @param code Code of ingredient
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets if ingredient is gluten free
     * @param
     */
    public void setGlutenFree(Boolean glutenFree) {
        this.isGlutenFree = glutenFree;
    }
    public void setGlutenFree() {
        this.isGlutenFree = true;
    }

    /**
     * Sets if ingredient is vegetarian
     * @param
     */
    public void setVegetarian(Boolean vegetarian) {
        this.isVegetarian = vegetarian;
    }
    public void setVegetarian() {
        this.isVegetarian = true;
    }

    /**
     * Sets if ingredient is vegan
     * @param
     */
    public void setVegan(Boolean vegan) { this.isVegan = vegan; }
    public void setVegan() { this.isVegan = true; }

    /**
     * Get vegan status of ingredient
     * @return True if vegan, False otherwise
     */
    public boolean getVegan() {
        return isVegan;
    }

    /** Get vegetarian status of ingredient
     * @return True if vegetarian, False otherwise
     */
    public boolean getVegetarian() {
        return isVegetarian;
    }

    /**
     * Get gluten free status of ingredient
     * @return True if gluten free, False otherwise
     */
    public boolean getGlutenFree() {
        return isGlutenFree;
    }

    /**
     * Get the unique code of ingredient
     * @return Code of ingredient
     */
    public String getCode() {
        return code;
    }

    /**
     * Get measurement of ingredient
     * @return Measurement of ingredient
     */
    public String getQuantityMeasuredIn() {
        return quantityMeasuredIn;
    }

    /**
     * Get the name of ingredient
     * @return Name of ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * Get the row id of ingredient
     * @return Id of ingredient
     */
    public Integer getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void incrementAmount() {
        this.amount++;
    }

    public void decrementAmount() {
    	if (this.amount > 0)
            this.amount--;
    }

    /**
     * Checks if two ingredients are similar
     * @param other The other ingredient
     * @return True if similar, False otherwise
     */
    public boolean equals(Ingredient other) {
        return this.code.equals(other.code);
    }

    /**
     * toString method for Ingredient
     * @return String representation for Ingredient
     */
    public String toString() {
        String template = "";
        template += "\t" + this.code;
        template += "\t" + this.name;
        template += "\t" + this.quantityMeasuredIn;
        template += "\t" + this.isGlutenFree;
        template += "\t" + this.isVegetarian;
        template += "\t" + this.isVegan;
        template += "\t" + this.amount;
        return template;
    }

    @Override
    public int compareTo(Object o) {
        String compareName = ((Ingredient) o).getName();
        return this.name.compareTo(compareName);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public Money getPrice() {
        return new Money(price);
    }

    public int getPriceInt() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPrice(float price) {
        Money money = new Money(price);
        this.price = money.getAsCents();
    }

}
