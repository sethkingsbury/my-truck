package models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.HashMap;

/**
 * This class implements an order object which stores information about the customers order,
 * including items and total cost
 *
 * Author: Samuel Sandri
 * Last Edit: Josh Yee 09/10/19
 */
@DatabaseTable(tableName = "ORDER")
public class Order {

    @DatabaseField(generatedId = true)
    private int id;

	/**
	 * HashMap of items and their quantities
	 */
    @DatabaseField(dataType = DataType.SERIALIZABLE)
	private HashMap<MenuItem, Integer> items = new HashMap<MenuItem, Integer>();

	/**
	 * The total price of the order
	 */
	private Money totalPrice = new Money(0, 0);

    /**
     * The total amount of the order
     * A small workaround to save the total amount
     * since the database would need to store the Money table if
     * we use the Money totalPrice field as a DatabaseField
     */
	@DatabaseField()
    private int totalAmount = 0;

	/**
	 * The number of the order
	 */
	@DatabaseField(unique = true)
	private int orderNum;

	/**
	 * The time when the order was made
	 */
	@DatabaseField()
	private String time;


    @DatabaseField
    private Long date;

    @DatabaseField()
    private String status = "";

    /**
     * Returns the HashMap of items and their quantities in the order
     *
     * @return HashMap of items and quantities
     */
    public HashMap<MenuItem, Integer> getItems() {
        return items;
    }

    public Order() {
    }

    public Order(int orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * Returns the total price of the order
     *
     * @return totalPrice of the order
     */
    public Money getTotalPrice() {
        Money newTotal = new Money(0, 0);
        for (MenuItem item : items.keySet()) {
            if (item instanceof CustomMenuItem) {
                newTotal.add(Money.multiply(((CustomMenuItem) item).getCustomPrice(), items.get(item)));
            } else {
                newTotal.add(Money.multiply(item.getPrice(), items.get(item)));
            }
        }
        totalPrice = newTotal;
        setTotalAmount();
        return newTotal;
    }

    public int getId() {
        return id;
    }

    /**
     * Adds the given quantity of the given item to the order
     *
     * @param item The MenuItem.java to add to the order
     * @param quantity The int quantity of the item to add
     */
    public void addItem(MenuItem item, int quantity) {
        if (!(items.containsKey(item))) {
            items.put(item, quantity);
        } else {
            int currentQuantity = items.get(item);
            items.remove(item);
            items.put(item, currentQuantity + quantity);
        }
        totalPrice.add(Money.multiply(item.getPrice(), quantity));
        setTotalAmount();
    }

    public void setTotalAmount() {
        totalAmount = totalPrice.getDollars() * 100 + totalPrice.getCents();
    }

    /**
     * Removes the given MenuItem from the order
     *
     * @param item MenuItem.java to remove
     */
    public void removeItem(MenuItem item) {
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
        totalPrice.subtract(item.getPrice());
        setTotalAmount();
    }

    /**
     * Sets the quantity of the given item in the order to the given quantity
     *
     * @param item MenuItem.java to set
     * @param quantity int quantity to set the item to
     */
    public void editItem(MenuItem item, int quantity) {
        items.remove(item);
        items.put(item, quantity);
    }

    /**
     *
     * @return Returns the order number
     */
    public int getOrderNum() {
        return orderNum;
    }

    /**
     * sets the order number of the order to orderNum
     * @param orderNum
     */
    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * Returns the time of when the order was made
     * @return
     */
    public String getTime() {
        return time;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status;}

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }


    /**
     * sets the time of the order.
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    public String toString() {
        String template = "";
        template += this.orderNum;
        template+= this.items;
        template += this.time;
        return template;
    }
}
