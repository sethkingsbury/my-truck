package view.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.*;
import view.controllers.SalesController;
import view.interfaces.Selectable;

/**
 * Custom VBox for displaying menu items in order
 */
public class OrderItemVBox extends VBox implements Selectable {

    /**
     * Current order item is from
     */
    private Order order;
    /**
     * Item to display
     */
    private MenuItem item;
    /**
     * HBox at top of OrderItemVBox
     */
    private HBox title;
    /**
     * SalesController managing sales screen
     */
    private SalesController manager;

    private String style;

    /**
     * Creator for the VBox
     *
     * @param item MenuItem
     * @param order Order
     * @param manager SalesController
     */
    public OrderItemVBox(MenuItem item, Order order, SalesController manager, String style) {

        this.item = item;
        this.order = order;
        this.manager = manager;
        this.style = style;

        createTitleLine();

        if (item instanceof CustomMenuItem) {
            for (Ingredient addedIngredient : ((CustomMenuItem) item).getAddedIngredients().keySet()) {
                createSubLine(addedIngredient,
                        ((CustomMenuItem) item).getAddedIngredients().get(addedIngredient), true);
            }
            for (Ingredient removedIngredient : ((CustomMenuItem) item).getRemovedIngredients().keySet()) {
                createSubLine(removedIngredient, ((CustomMenuItem) item).getRemovedIngredients().get(removedIngredient), false);
            }
        }
    }

    /**
     * Creates top line of VBox containing item name, quantity and price
     */
    private void createTitleLine() {
        HBox container = new HBox();

        Pane padding = new Pane();
        padding.setMinWidth(5);
        padding.setMaxWidth(5);
        padding.setPrefWidth(5);

        Label name = new Label(item.getName());
        name.setMaxWidth(140);
        name.setMinWidth(140);

        Label quantity = new Label(order.getItems().get(item).toString());
        quantity.setMinWidth(50);

        Label price = new Label(Money.multiply(item.getPrice(), order.getItems().get(item)).toString());
        price.setMinWidth(50);

        container.getChildren().addAll(padding, name, quantity, price);
        super.getChildren().add(container);

        title = container;
    }

    /**
     * Creates line for an edited ingredient
     *
     * @param ingredient Ingredient added or removed
     * @param quantity int quantity of ingredient
     * @param add boolean true if ingredient added
     */
    private void createSubLine(Ingredient ingredient, int quantity, boolean add) {
        HBox container = new HBox();

        Pane padding = new Pane();
        padding.setMinWidth(10);
        padding.setMaxWidth(10);
        padding.setPrefWidth(10);

        String plusOrMinusText = "+";
        if (!(add)) {
            plusOrMinusText = "-";
        }
        Label plusOrMinusLabel = new Label(plusOrMinusText);
        plusOrMinusLabel.setMinWidth(15);
        plusOrMinusLabel.setMaxWidth(15);

        Label name = new Label(ingredient.getName());
        name.setMaxWidth(120);
        name.setMinWidth(120);

        Label quantityLabel = new Label(String.valueOf(quantity));
        quantityLabel.setMinWidth(50);

        if (add) {
            Label priceLabel = new Label(ingredient.getPrice().toString());
            container.getChildren().addAll(padding, plusOrMinusLabel, name, quantityLabel, priceLabel);
        } else {
            container.getChildren().addAll(padding, plusOrMinusLabel, name, quantityLabel);
        }
        super.getChildren().add(container);
    }

    /**
     * @return top HBox of the VBox containing item description
     *
     */
    public HBox getTitle() {
        return title;
    }

    /**
     * @return MenuItem displayed
     */
    public MenuItem getItem() {
        return item;
    }

    @Override
    public boolean requestSelection(boolean select) {
        return true;
    }

    @Override
    public void notifySelection(boolean select) {
        manager.selectOrderItem(item);
    }


}
