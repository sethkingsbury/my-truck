package javaSteps.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import managers.OrderManager;
import models.*;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class basketSteps {

    OrderManager orderManager = new OrderManager();
    Order order = orderManager.getOrder();
    MenuItem item;
    MenuItem item2;
    Ingredient ingredient;
    CustomMenuItem customItem;
    Ingredient bacon;

    @Given("An item burger is priced at ${int}")
    public void anItemBurgerIsPricedAt$(Integer int1) {
        item = new MenuItem("Burger", "Burger", new Money(int1), "Main");
    }

    @Given("The basket is empty")
    public void theBasketIsEmpty() {
        order = new Order();
    }

    @When("I add {int} burger to the basket")
    public void iAddBurgerToTheBasket(Integer int1) {
        order.addItem(item, int1);
    }

    @Then("The basket contains {int} burger")
    public void theBasketContainsBurger(Integer int1) {
        assertEquals(order.getItems().get(item), int1);
    }

    @Then("The total price is ${int}")
    public void theTotalPriceIs$(int int1) { assertEquals(order.getTotalAmount(), int1); }

    @Given("The basket contains {int} hotdog priced at ${int}")
    public void theBasketContainsAHotdogPricedAt$(Integer int1, Integer int2) {
        item2 = new MenuItem("Hotdog", "Hotdog", new Money(int2), "Main");
        order.addItem(item2, int1);
    }

    @Then("The order contains {int} burger and {int} hotdog")
    public void theOrderContainsBurgerAndHotdog(Integer int1, Integer int2) {
        assertEquals(order.getItems().get(item), int1);
        assertEquals(order.getItems().get(item2), int2);
    }

    @Given("The basket contains {int} burger priced at ${int}")
    public void theBasketContainsBurgerPricedAt$(Integer int1, Integer int2) {
        item = new MenuItem("Burger", "Burger", new Money(int2), "Main");
        order.addItem(item, int1);
    }

    @When("I remove {int} burger from the basket")
    public void iRemoveBurgerFromTheBasket(Integer int1) {
        order.removeItem(item);
    }

    @Then("The basket contains {int} items")
    public void theBasketContainsItems(Integer int1) {
        assertEquals((Integer) order.getItems().size(), int1);
    }

    @Then("The basket contains {int} hotdog")
    public void theBasketContainsHotdog(Integer int1) {
        assertEquals(order.getItems().get(item2), int1);
    }

    @When("I add bacon to the burger")
    public void iAddBaconToTheBurger() {
        Ingredient baseIngredient = new Ingredient("base", "Based", "count");
        HashMap<Ingredient, Integer> baseIngredients = new HashMap<>();
        baseIngredients.put(baseIngredient, 1);
        customItem = new CustomMenuItem(item.getCode(), item.getName(), item.getPrice(), item.getCategory(), baseIngredients);

        bacon = new Ingredient("Bacon", "Bacon", "count");
        customItem.addIngredient(bacon, 1);
    }

    @Then("The burger contains bacon")
    public void theBurgerContainsBacon() {
    int amount = customItem.getAddedIngredients().get(bacon);
    assertEquals(amount, 1);
    }

    @When("I confirm the order")
    public void iConfirmTheOrder() {
        orderManager.newOrder();
    }

    @Then("The sales screen resets")
    public void theSalesScreenResets() {
        assertEquals((Integer) orderManager.getOrder().getTotalAmount(), (Integer) 0);
    }

}
