package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import models.MenuItem;
import models.Order;

import java.sql.SQLException;
import java.util.*;

/**
 * OrderDBOperations provides functions to interact with
 * the ORDER table
 */
public class OrderDBOperations {

    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the USER table
     */
    private Dao<Order, Integer> orderDao;

    /**
     * Default constructor for OrderDBOperations
     * @param connectionSource
     */
    public OrderDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            orderDao = DaoManager.createDao(connectionSource, Order.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a single order into database
     * @param order Order
     * @throws SQLException
     */
    public void insertOrder(Order order) throws SQLException {
        orderDao.create(order);
    }

    /**
     * Delete a single order by its Id
     * @param id Id to delete
     * @throws SQLException
     */
    public void deleteOrderById(Integer id) throws SQLException {
        orderDao.deleteById(id);
    }

    /**
     * Return a single order by its Id
     * @param id Id of order
     * @return Order object found
     * @throws SQLException
     */
    public Order getOrderById(Integer id) throws SQLException {
        return orderDao.queryForId(id);
    }

    /**
     * Returns the latest number of order
     * @return 0 if there was no existing order, number of latest order otherwise
     * @throws SQLException
     */
    public Integer getLatestOrderNum() throws SQLException {
        Order o = orderDao.queryBuilder()
                .orderBy("id", false)
                .queryForFirst();

        if (o == null)
            return 0;

        return o.getOrderNum();
    }

    /**
     * Returns a list of all orders in the database
     * @return ArrayList of all orders in the database
     * @throws SQLException
     */
    public ArrayList<Order> getAllOrders() throws SQLException {
    	return new ArrayList(orderDao.queryForAll());
    }

    /**
     * Returns an order by the order number
     * @param orderNum order number
     * @return Order object found in database
     * @throws SQLException
     */
    public Order getOrderByOrderNumber(Integer orderNum) throws SQLException {
        Order order = orderDao.queryBuilder()
                .where()
                .eq("orderNum", orderNum)
                .queryForFirst();
        return order;
    }

    /**
     * Returns the items found in order specified by the order number
     * @param orderNum order number
     * @return HashMap of items in the order
     * @throws SQLException
     */
    public HashMap<MenuItem, Integer> getMenuItemsByOrderNum(Integer orderNum) throws SQLException {
        Order order = orderDao.queryBuilder()
                .where()
                .eq("orderNum", orderNum)
                .queryForFirst();
        return order.getItems();
    }

    /**
     * Get the total price of items in order specified by the order number
     * @param orderNum order number
     * @return Total price of items in the order
     * @throws SQLException
     */
    public int getTotalPriceByOrderNum(Integer orderNum) throws SQLException {
        HashMap<MenuItem, Integer> menuItems = getMenuItemsByOrderNum(orderNum);
        int totalPrice = 0;
        for (Map.Entry<MenuItem, Integer> menuItem : menuItems.entrySet()) {
            MenuItem m = menuItem.getKey();
            Integer quantity = menuItem.getValue();
            totalPrice += (m.getPrice().getAsCents() * quantity) / 100;
        }
        return totalPrice;
    }

    /**
     * Adds an item to the MenuItems in the order specified by order number
     * @param orderNum order number
     * @param item MenuItem to add
     * @param quantity quantity of MenuItem
     * @throws SQLException
     */
    public void addItem(Integer orderNum, MenuItem item, int quantity) throws SQLException {
        Order o = getOrderByOrderNumber(orderNum);
        HashMap<MenuItem, Integer> items = o.getItems();
        if (!(items.containsKey(item))) {
            items.put(item, quantity);
        } else {
            int currentQuantity = items.get(item);
            items.put(item, currentQuantity + quantity);
        }
        orderDao.update(o);
    }

    /**
     * Remove an item from the MenuItems in the order specified by order number
     * @param orderNum order number
     * @param item MenuItem to remove
     * @throws SQLException
     */
    public void removeItem(Integer orderNum, MenuItem item) throws SQLException {
        Order o = getOrderByOrderNumber(orderNum);
        HashMap<MenuItem, Integer> items = o.getItems();
        if (items.get(item) == null) {
            throw new RuntimeException("Item does not exist in current order");
        }

        if (items.get(item) == 1) {
            items.remove(item);
        } else {
            int currentQuantity = items.get(item);
            items.remove(item);
            items.put(item, currentQuantity - 1);
        }
        orderDao.update(o);
    }

    /**
     * Edit an item's quantity from the MenuItems in the order specified by order number
     * @param orderNum order number
     * @param item MenuItem to edit
     * @param quantity new quantity
     * @throws SQLException
     */
    public void editItem(Integer orderNum, MenuItem item, int quantity) throws SQLException {
        Order o = getOrderByOrderNumber(orderNum);
        HashMap<MenuItem, Integer> items = o.getItems();
        items.remove(item);
        items.put(item, quantity);
        orderDao.update(o);
    }

    /**
     * Refunds an order, sets it status to Refunded
     * @param order
     * @throws SQLException
     */
    public void refundOrder(Order order) throws SQLException {
        Order o = getOrderByOrderNumber(order.getOrderNum());
        o.setStatus("Refunded");
        orderDao.update(o);
    }

    /**
     * Fetches all orders between two given date/times.
     * @param minTime the minimum epoch time to search from.
     * @param maxTime the minimum epoch time to search to.
     * @return the list op orders
     * @throws SQLException
     */
    public Optional<List<Order>> getOrdersByTimePeriod(long minTime, long maxTime) throws SQLException {
        List<Order> orders = orderDao.queryBuilder().where().between("date", minTime, maxTime).query();
        return Optional.ofNullable(orders);
    }
}
