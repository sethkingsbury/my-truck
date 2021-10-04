package models;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model for Custom Menu Item
 */
public class CustomMenuItem extends MenuItem {



    /**
     * Ingredients removed from the item and the quantity
     */
    private HashMap<Ingredient, Integer> removedIngredients = new HashMap<>();

    /**
     * Ingredients added to the item and the quantity
     */
    private HashMap<Ingredient, Integer> addedIngredients = new HashMap<Ingredient, Integer>();

    private HashMap<Ingredient, Integer> customIngredients;

    public CustomMenuItem(String code, String name, Money price, String category, HashMap<Ingredient, Integer> ingredients) {
        super(code, name, price , category);
        super.setIngredients(ingredients);
        customIngredients = ingredients;
    }
    /**
     * Removes an ingredient from the custom menu item
     * @param ingredient to be removed from the custom menu item
     */
    public void removeIngredient(Ingredient ingredient) {
        if (customIngredients.get(ingredient) > 0) {
            Integer currentQuantity;
            if (!(addedIngredients.get(ingredient) == null)) {
                if (addedIngredients.get(ingredient) == 1) {
                    addedIngredients.remove(ingredient);
                } else {
                    currentQuantity = addedIngredients.get(ingredient);
                    addedIngredients.remove(ingredient);
                    addedIngredients.put(ingredient, (currentQuantity - 1));
                }
            } else {
                if (removedIngredients.get(ingredient) == null) {
                    removedIngredients.put(ingredient, 1);
                } else {
                    currentQuantity = removedIngredients.get(ingredient);
                    removedIngredients.remove(ingredient);
                    removedIngredients.put(ingredient, (currentQuantity + 1));
                }


            }
            currentQuantity = customIngredients.get(ingredient);
            customIngredients.remove(ingredient);
            customIngredients.put(ingredient, (currentQuantity - 1));
        }
    }

    /**
     * Adds an ingredient to the custom menu item
     * @param ingredient to be added to the custom menu item
     * @param quantity the amount of the ingredient to be added
     */
    public void addIngredient(Ingredient ingredient, int quantity) {
        Integer currentQuantity;
        if (!(removedIngredients.get(ingredient) == null)) {
            if (removedIngredients.get(ingredient) == 1) {
                removedIngredients.remove(ingredient);
            } else {
                currentQuantity = removedIngredients.get(ingredient);
                removedIngredients.remove(ingredient);
                removedIngredients.put(ingredient, (currentQuantity - 1));
            }
        } else {
            if (addedIngredients.containsKey(ingredient)) {
                currentQuantity = addedIngredients.get(ingredient);
                addedIngredients.remove(ingredient);
                addedIngredients.put(ingredient, quantity + currentQuantity);
            } else {
                addedIngredients.put(ingredient, quantity);
            }
        }
        currentQuantity = customIngredients.get(ingredient);
        if (currentQuantity == null) {
            currentQuantity = 0;
        }
        customIngredients.remove(ingredient);
        customIngredients.put(ingredient, (currentQuantity + 1));
    }

    public HashMap<Ingredient, Integer> getAddedIngredients() {
        return addedIngredients;
    }

    public HashMap<Ingredient, Integer> getRemovedIngredients() {
        return removedIngredients;
    }

    public HashMap<Ingredient, Integer> getCustomIngredients() {
        return customIngredients;
    }

    public Money getCustomPrice() {
        Money totalPrice = this.getPrice();
        for (Ingredient ingredient : addedIngredients.keySet()) {
            totalPrice.add(Money.multiply(ingredient.getPrice(), addedIngredients.get(ingredient)));
        }
        return totalPrice;
    }
}
