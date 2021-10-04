package managers;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.OrderDBOperations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Order;
import models.MenuItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Order Manager keeps track of the current order and orders in the database
 */
public class OrderManager {

    private static final OrderManager orderManager = new OrderManager();
    private DatabaseOperator db = new DatabaseOperator();
    private ConnectionSource connectionSource = db.establishConnection();
    private OrderDBOperations oDBO = new OrderDBOperations(connectionSource);

    private Order order = null;

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    /**
     * Default constructor for Order Manager
     */
    public OrderManager() { }

    /**
     * Creates a new order and keeps track of it
     */
    public void newOrder() {
        order = new Order();
    }

    /**
     * @return Existing Order Manager instance
     */
    public static OrderManager getOrderManager() {
        return orderManager;
    }

    /**
     * Sets the current order to Order specified
     * and sets the current order number by the latest number + 1
     * @param order
     * @throws SQLException
     */
    public void setOrder(Order order) throws SQLException {
        this.order = order;
        this.order.setOrderNum(getLatestOrderNumber() + 1);
    }

    /**
     * @return Current order
     */
    public Order getOrder() {
        if (order == null)
            order = new Order();
        return order;
    }

    /**
     * Writes the current order to the database
     * @throws SQLException
     */
    public void insertOrder() throws SQLException {
        oDBO.insertOrder(order);
    }

    /**
     * @return Latest order number in the database
     */
    public Integer getLatestOrderNumber() {
        try {
            return oDBO.getLatestOrderNum();
        } catch (SQLException e) {
        }
        return 0;
    }

    /**
     * Refunds an order, sets its status to refunded
     * @param order Order to be refunded
     * @throws SQLException
     */
    public void refundOrder(Order order) throws SQLException {
        oDBO.refundOrder(order);
    }

    /**
     * Syncs the orders for Javafx tables from the orders in DB
     * @throws SQLException
     */
    public void refreshOrders() throws SQLException {
        orders.setAll(oDBO.getAllOrders());
    }

    /**
     * @return List of orders, useful for Javafx
     * @throws SQLException
     */
    public ObservableList<Order> getOrders() throws SQLException {
        refreshOrders();
        return orders;
    }

    /**
     * Sets list of orders, useful for Javafx tables
     * @param orders List of orders
     */
    public void setOrders(ObservableList<Order> orders) {
        this.orders = orders;
    }

    /**
     * Returns all the orders within a given timeframe
     * @param minTime Starting time
     * @param maxTime End time
     * @return List of orders within the given timeframe
     */
    public List<Order> getOrdersByTimePeriod(long minTime, long maxTime) {
        List<Order> orders = null;
        try {
            Optional<List<Order>> optionalOrders = oDBO.getOrdersByTimePeriod(minTime, maxTime);
            if (optionalOrders.isPresent()) orders = optionalOrders.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

}
