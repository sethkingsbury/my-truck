package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import managers.DataManager;
import managers.MenuItemManager;
import managers.OrderManager;
import models.CashFloat;
import models.Money;
import models.Order;
import view.ThemeManager;
import view.components.CashHBox;
import view.eventHandlers.SelectionHandler;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Create Change Float Controller implements logic that runs the Change screen after
 * an order is confirmed
 */
public class ChangeController extends MasterController implements Initializable {
    private OrderManager orderManager = OrderManager.getOrderManager();
    private DataManager dataManager = DataManager.getDataManager();
    private MenuItemManager menuItemManager = MenuItemManager.getMenuItemManager();

    private ThemeManager themeManager = ThemeManager.getThemeManager();

    @FXML
    private Pane mainPane;

    @FXML
    private VBox currentChange;

    private ArrayList<CashFloat> cashwad;
    {
        try {
            cashwad = dataManager.getAllDenom();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private GridPane changeGrid;

    @FXML
    private Button returnSales;

    @FXML
    private Button confirmSale;

    @FXML
    private Button eftposButton;

    @FXML
    private Button getChangeButton;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label totalChangeLabel;

    @FXML
    private Label amountReceived;

    @FXML
    private VBox changeText;

    private Money received;
    private Money changeToGive;
    private Integer totalCents;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eftposButton.setDisable(false);

        themeManager.setToCurrentTheme(mainPane);

        createCashButtons();

        confirmSale.setDisable(true);
        Order order = orderManager.getOrder();
        Money total = order.getTotalPrice();
        totalCents = total.getAsCents();
        changeToGive = new Money(0, 0);
        totalPriceLabel.setText(total.toString());
        received = new Money(0, 0);

        amountReceived.setText(received.toString());
        totalChangeLabel.setText(changeToGive.toString());

        for (CashFloat change : cashwad) {
            change.setQuantity(0);
        }
    }

    /**
     * createChangeLabels() is a function that looks through an arraylist of money, and checks if change needs to be returned
     * (totalcents <0). if it is we use CalculateChange on every item, displaying the correct change
     */
    public void createChangeLabels() {
        changeGrid.setDisable(true);
        confirmSale.setDisable(false);
        returnSales.setDisable(true);
        currentChange.setDisable(true);
        getChangeButton.setDisable(true);
        ArrayList<CashFloat> cashwadCopy = new ArrayList<>(cashwad);
        Collections.reverse(cashwadCopy);
        if ((received.getAsCents() == 0) || (changeToGive.getAsCents() == 0) || ((totalCents < received.getAsCents()) && (totalCents >= 0))) {
            confirmSale.setDisable(true);
            changeToGive = new Money(0, 0);
            returnSales.setDisable(false);
            currentChange.setDisable(false);
            getChangeButton.setDisable(false);

        }

        if (totalCents < 0){
            for (CashFloat cashfloat: cashwadCopy) {
                String text1 = calculateChange(cashfloat.getType(), cashfloat.getDenomination());
                Label label = new Label(text1);
                label.getStyleClass().add("label1");
                changeText.getChildren().add(label);
            }
        }

        if (changeToGive.getAsCents() > 0) {
            totalChangeLabel.setText("Alert Manager, we do not have the ability to return " + changeToGive.toString());
            confirmSale.setDisable(false);
            returnSales.setDisable(false);
        }


    }

    /**
     * createCashButtons() makes the +- buttons for recieving cash.
     */
    private void createCashButtons() {
        changeGrid.add(new CashHBox(this, "Coin", 10, 0), 0, 0);
        changeGrid.add(new CashHBox(this, "Coin", 20, 1), 0, 1);
        changeGrid.add(new CashHBox(this, "Coin", 50, 2), 0, 2);
        changeGrid.add(new CashHBox(this, "Coin", 1, 3), 0, 3);
        changeGrid.add(new CashHBox(this, "Coin", 2, 4), 0, 4);
        changeGrid.add(new CashHBox(this, "Note", 5, 5), 1, 0);
        changeGrid.add(new CashHBox(this, "Note", 10, 6), 1, 1);
        changeGrid.add(new CashHBox(this, "Note", 20, 7), 1, 2);
        changeGrid.add(new CashHBox(this, "Note", 50, 8), 1, 3);
        changeGrid.add(new CashHBox(this, "Note", 100, 9), 1, 4);

        SelectionHandler selectionHandler = new SelectionHandler(changeGrid);
        changeGrid.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMouseClickedHandler());
    }

    /**
     * function for receiving 1 incrementation of a denomination
     */
    public void addCash(ActionEvent event) {
        Node source = (Node) event.getSource();
        CashHBox sourceBox = (CashHBox) source.getParent();
        try {
            dataManager.increaseQuantity(sourceBox.getCashType(), sourceBox.getDenomination(), 1);
            increment(sourceBox.getCashId());
            changeHelper(sourceBox.getCashType(), sourceBox.getDenomination());
            Label amountLabel = sourceBox.getAmountLabel();
            amountLabel.setText(String.valueOf(Integer.valueOf(amountLabel.getText()) + 1));
        } catch (SQLException e) {
            System.out.println("Could not add cash");
        }
    }

    /**
     * function for receiving 1 incrementation of a denomination
     * @param type type of money (coin/note)
     * @param denomination value of type in cents
     * @param id
     */
    public void addCash(String type, Integer denomination, int id) {
        try {
            dataManager.increaseQuantity(type, denomination, 1);
            increment(id);
            changeHelper(type, denomination);
        } catch (SQLException e) {
            System.out.println("Could not add cash");
        }
    }

    /**
     * uses removeCashFloat to -1
     * @param event
     */
    public void removeCash(ActionEvent event) {
        removeCashFloat(event);
    }

    /*
    This is used to go to a reset sales screen when we have confirmed the sale and are done with the transaction
     */
    @FXML
    public void confirmChangeToSales(ActionEvent event) throws Exception {
        orderManager.insertOrder(); // add the current order to the database
        orderManager.newOrder();
        menuItemManager.refreshMenuItems();
        changeScene("/gui/scenes/sales.fxml", event);
    }
    /*
    backToOrder is used when we want to go from the change screen back to the old order (without resetting) to edit the order
     */
    @FXML
    public void backToOrder(ActionEvent event) {
        changeScene("/gui/scenes/sales.fxml", event);
    }

    /*
    changeHelper is used adjust the total (cents) by whatever button is clicked. so this function is in every button,
    eg when the 10c button is pressed, getFloatCents recognizes it is a 10cents, and takes 10 cents off total.
     */
    private void changeHelper(String type, Integer denomination) throws SQLException { //THIS IS INCREMENTING
        totalCents -= dataManager.getFloatCents(type, denomination);

        Money toAdd = new Money(0, 0);
        toAdd.setAmountInt(dataManager.getFloatCents(type, denomination));
        received.add(toAdd);
        amountReceived.setText(received.toString());
        if (received.getAsCents() > 0) {
            returnSales.setDisable(true);
            getChangeButton.setDisable(true);
        }
        if (received.getAsCents() > totalCents) {
            getChangeButton.setDisable(false);
            confirmSale.setDisable(true);
        }
        if (totalCents > 0) {
            getChangeButton.setDisable(true);
        }
        if ((totalCents == 0) || (received.getAsCents() == 0)) {

            changeToGive = new Money(0, 0);
            confirmSale.setDisable(false);
            currentChange.setDisable(true);
            //changeGrid.setDisable(true);
            returnSales.setDisable(true);
            totalChangeLabel.setText("No Change required - transaction complete");
            getChangeButton.setDisable(true);
        }
        else if (totalCents < 0) {
            //changeGrid.setDisable(true);
            changeToGive = new Money(totalCents * -1);
            totalChangeLabel.setText(changeToGive.toString());
        }
    }
    /*
    increment is used to increase the quantity of denominations  that
    are handed over the counter to the person pushing the 'money recieved' buttons. this shows as a counter on the list of inputs
    on the RHS of the change screen
     */
    private void increment(int id) {
        int currentQuantity = cashwad.get(id).getQuantity();
        cashwad.get(id).setQuantity(currentQuantity + 1);


    }

    /*
    this is the small buttons on the RHS of the table that decrement the count of various denominations
    that the customer has given the worker. because every time something is added, the database is automatically adjusted,
    this means that whenever we reduce we must also reduce the database.

    perhaps in the future make it read the tallies and update the database all at once, on the confirm?
     */
    private void removeCashFloat(ActionEvent event) { //THIS IS THE DECREMENTING
        CashHBox source = (CashHBox) ((Node) event.getSource()).getParent();
        int currentQuantity = Integer.valueOf(source.getAmountLabel().getText());
        String type = source.getCashType();
        Integer denomination = source.getDenomination();

        if (currentQuantity > 0) {
            try {
                Label amountLabel = source.getAmountLabel();
                amountLabel.setText(String.valueOf(Integer.valueOf(amountLabel.getText()) - 1));
                dataManager.decreaseQuantity(type, denomination, -1);
                totalCents += dataManager.getFloatCents(type, denomination);
                Money toRemove = new Money(0, 0);
                toRemove.setAmountInt(dataManager.getFloatCents(type, denomination));
                received.subtract(toRemove);
                changeToGive.subtract(new Money(dataManager.getFloatCents(type, denomination)));

                amountReceived.setText(received.toString());
                totalChangeLabel.setText(changeToGive.toString());
                if (received.getAsCents() == 0) {
                    returnSales.setDisable(false);
                    getChangeButton.setDisable(true);
                }
                if (totalCents > 0) {
                    //changeGrid.setDisable(false);
                    changeToGive = new Money(0, 0);
                    totalChangeLabel.setText(changeToGive.toString());
                    getChangeButton.setDisable(true);
                    confirmSale.setDisable(true);
                } else if (totalCents == 0) { //here is the condition of finding the correct amount of change by giving the customer back their coins
                    //before having to calculate change. same as getting the perfect amount to begin with
                    changeToGive.subtract(new Money(dataManager.getFloatCents(type, denomination)));
                    //changeGrid.setDisable(true);
                    getChangeButton.setDisable(true);
                    confirmSale.setDisable(false);
                    totalChangeLabel.setText("No Change required - transaction complete");
                    //confirmSale.setDisable(false);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }


    /**
     * this function calculates what change to give, it iterates through each denomination checking if its available
     * and how many to return
     * @param type note/coin
     * @param denomination value of said type
     * @return
     */
    private String calculateChange(String type, Integer denomination) {
        int count = 0;
        Money value = null;
        try {
            value = new Money(dataManager.getFloatCents(type, denomination));
            int amount = dataManager.getDenomQuantity(type, denomination);
            while (changeToGive.getAsCents() >= value.getAsCents() && amount > 0) {
                changeToGive.subtract(value);
                dataManager.decreaseQuantity(type, denomination, -1);
                count++;
            }
        } catch (SQLException exception) {
            System.out.println("Could not calculate change");
        }

        String finalString = "";
        if (count > 0) {
            finalString += String.format("%dx %s %s", count, value.toString(), type);
        }

        eftposButton.setDisable(true);
        return finalString;
    }
}