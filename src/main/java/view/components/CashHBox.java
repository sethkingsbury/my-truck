package view.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.*;
import view.controllers.ChangeController;
import view.controllers.SalesController;
import view.interfaces.Selectable;

/**
 * Custom HBox for displaying menu items in order
 */
public class CashHBox extends HBox implements Selectable {

    /**
     * SalesController managing sales screen
     */
    private ChangeController manager;

    /**
     * Type of cash float
     */
    private String type;

    /**
     * Denomination of cash float
     */
    private Integer denomination;

    /**
     * ID of cash float in database
     */
    private int id;

    /**
     * Amount of cash float
     */
    private Label amountLabel;


    /**
     * Default Constructor for CashHBox
     * @param manager Change Controller object it was created from
     * @param type Type of cash float
     * @param denomination Denomination of cash float
     * @param id ID of cash float
     */
    public CashHBox(ChangeController manager, String type, Integer denomination, int id) {

        this.manager = manager;
        this.type = type;
        this.denomination = denomination;
        this.id = id;

        Label denom = new Label();
        if (id >= 3) {
            denom.setText("$" + denomination);
        } else {
            denom.setText(denomination + "c");
        }
        denom.getStyleClass().add("label1");
        denom.setMaxWidth(100);
        denom.setMinWidth(100);

        Button addButton = new Button();
        addButton.setText("+");
        addButton.setOnAction(manager::addCash);
        addButton.getStyleClass().add("adjustCashButton");
        addButton.setMaxWidth(40);
        addButton.setMinWidth(40);
        addButton.setMaxHeight(40);
        addButton.setMinHeight(40);

        amountLabel = new Label("0");
        amountLabel.getStyleClass().add("label1");

        Button removeButton = new Button();
        removeButton.setText("-");
        removeButton.setOnAction(manager::removeCash);
        removeButton.getStyleClass().add("adjustCashButton");
        removeButton.setMaxWidth(40);
        removeButton.setMinWidth(40);
        removeButton.setMaxHeight(40);
        removeButton.setMinHeight(40);

        Pane padding1 = new Pane();
        padding1.setMinWidth(80);
        padding1.setMaxWidth(80);
        padding1.setPrefWidth(80);

        Pane padding2 = new Pane();
        padding2.setMinWidth(10);
        padding2.setMaxWidth(10);
        padding2.setPrefWidth(10);

        Pane padding3 = new Pane();
        padding3.setMinWidth(10);
        padding3.setMaxWidth(10);
        padding3.setPrefWidth(10);

        this.getChildren().addAll(padding1, denom, addButton, padding2, amountLabel, padding3, removeButton);
        this.getStyleClass().add("cashButton");
    }

    /**
     * @return Cash ID of the cash float
     */
    public int getCashId() {
        return id;
    }

    /**
     * @return Denomination of the cash float
     */
    public Integer getDenomination() {
        return denomination;
    }

    /**
     * @return Type of the cash float
     */
    public String getCashType() {
        return type;
    }

    /**
     * Sets amount of the cash float
     * @param amount amount of cash float
     */
    public void setAmount(int amount) {
        amountLabel.setText(String.valueOf(amount));
    }

    /**
     * Get the label showing the cash float amount
     * @return
     */
    public Label getAmountLabel() {
        return amountLabel;
    }

    @Override
    public boolean requestSelection(boolean select) {
        return true;
    }

    @Override
    public void notifySelection(boolean select) {
        manager.addCash(type, denomination, id);
        amountLabel.setText(String.valueOf(Integer.valueOf(amountLabel.getText()) + 1));
    }
}
