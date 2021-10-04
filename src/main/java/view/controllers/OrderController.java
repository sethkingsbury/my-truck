package view.controllers;

import com.j256.ormlite.support.ConnectionSource;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import managers.OrderManager;
import models.*;
import models.MenuItem;
import view.MainApplication;
import view.ThemeManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

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
import java.util.ResourceBundle;

/**
 * Order Controller implements logic that runs behind the order screen in the program
 */
public class OrderController extends MasterController implements Initializable {
    private DataManager dataManager = DataManager.getDataManager();

    // MenuItem table
    @FXML
    private TableView<Order> orderTable;

    private ArrayList<CashFloat> cashwad;
    {
        try {
            cashwad = dataManager.getAllDenom();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TableColumn<Order, Integer> orderNum;

    @FXML
    private TableColumn<Order, String> orderCost;

    @FXML
    private TableColumn<Order, String> orderAmount;

    @FXML
    private TableColumn<Order, String> orderItem;

    @FXML
    private TableColumn<Order, String> orderStatus;

    @FXML
    private TableColumn<Order, String> orderTime;

    @FXML
    private VBox refundBox;

    public Integer refundAmount;

    private OrderManager orderManager = OrderManager.getOrderManager();
    private ConnectionSource c = MainApplication.connectionSource;

    private ThemeManager themeManager = ThemeManager.getThemeManager();

    @FXML
    private GridPane mainPane;

    private HashMap<Button, Order> removeButtonMap = new HashMap<Button, Order>();

    @FXML
    public void refundOrder(ActionEvent event) {
        Order currentOrder = orderTable.getSelectionModel().getSelectedItem();
        if (currentOrder.getStatus().isEmpty()) {
            String confirmString = "Are you sure you want to refund order?";
            try {
                openConfirm(event, confirmString, OrderController.class.getMethod("doRefund"));
            } catch (IOException | NoSuchMethodException e) {
            }
        }
    }

    /**
     * doRefund() retrieves the cost of the order from the Order model. it then uses the function calculateRefund() to calculate change
     */
    public void doRefund() {
        Order currentOrder = orderTable.getSelectionModel().getSelectedItem();
        refundAmount = currentOrder.getTotalAmount();
        calculateRefund();
    }

    /**
     * calculateRefund() works the same way as calculating the change. it iterates through the Arraylist refundwad, finding
     * the correct available change.
     */
    @FXML
    private void calculateRefund() { //refund and edit row top say its been refunded!
        Order currentOrder = orderTable.getSelectionModel().getSelectedItem();
        try {
            orderManager.refundOrder(currentOrder);
            refreshOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<CashFloat> refundWad = new ArrayList<>(cashwad);
        Collections.reverse(refundWad);
        for (CashFloat cashfloat: refundWad) {
            String text1 = calculateChange(cashfloat.getType(), cashfloat.getDenomination());
            Label label = new Label(text1);
            label.getStyleClass().add("label1");
            refundBox.getChildren().add(label);
        }
    }

    /**
     * refreshOrders() gets the orders and repopulates them onto the table
     */
    private void refreshOrders() {
        ObservableList<Order> orders = null;

        try {
            orders = orderManager.getOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createOrders(orders);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeManager.setToCurrentTheme(mainPane);
        ObservableList<Order> orders = null;
        try {
            orders = orderManager.getOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createOrders(orders);

        orderNum.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        orderCost.setCellValueFactory(cell -> {
            int price = cell.getValue().getTotalAmount();
            Money newprice = new Money(price);
            return new SimpleStringProperty(newprice.toString());
        });

        orderAmount.setCellValueFactory(cell -> {
            String values = "";
            for (Integer value : cell.getValue().getItems().values()) {
                values += String.valueOf(value) + "\n";
            }
            return new SimpleStringProperty(values);
        });

        orderItem.setCellValueFactory(cell -> {
            String items = "";
            for (MenuItem menuItem : cell.getValue().getItems().keySet()) {
                items += menuItem.getName() + "\n";
            }
            return new SimpleStringProperty(items);
        });

        orderTime.setCellValueFactory(cell -> {
            return new SimpleStringProperty(new Date(cell.getValue().getDate()).toString());
        });

        orderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * createOrders() sets the ordertable with the orders passed into it
     * @param orders are the orders that need to be populated into the table
     */
    private void createOrders(ObservableList<Order> orders) {
        orderTable.setItems(orders);
    }

    public void removeOrder(ActionEvent e) {
        Button sourceButton = (Button) e.getSource();
        removeButtonMap.remove(removeButtonMap.get(sourceButton));
        try {
            orderManager.getOrders().remove(removeButtonMap.get(sourceButton));
            createOrders(orderManager.getOrders());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * calculateChange() is the function that the refund uses to find out what denominations are available, and what ones
     * to return if there is a refund
     * @param type type of money, coin or note
     * @param denomination the value of said type (in cents)
     * @return returns each line of each denomination, showing the quantity of each denomination required.
     */
    private String calculateChange(String type, Integer denomination) {
        int count = 0;
        Money value = null;
        try {
            value = new Money(dataManager.getFloatCents(type, denomination));
            int amount = dataManager.getDenomQuantity(type, denomination);
            while (refundAmount >= value.getAsCents() && amount > 0) {
                refundAmount -= value.getAsCents();
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
        return finalString;
    }

}
