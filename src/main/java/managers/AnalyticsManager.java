package managers;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Order;

import java.sql.SQLException;
import java.util.*;

/**
 * Analytics Manager provides support for accessing data throughout
 * the analytics view controller.
 */
public class AnalyticsManager {

    private DatabaseOperator db = new DatabaseOperator();
    private ConnectionSource connectionSource = db.establishConnection();

    private static final AnalyticsManager analyticsManager = new AnalyticsManager();

    private OrderManager orderManager = new OrderManager();

    private ObservableList<Order> orders = FXCollections.observableArrayList();


    public AnalyticsManager() {

        try {
            orders = orderManager.getOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static AnalyticsManager getAnalyticsManager() {
        return analyticsManager;
    }

    /**
     * Creates the data for the sales over time graph
     * @param currentTimeScale current time period
     * @return the data in a hashmap of date and float
     */
    public HashMap<Date, Float> getSalesOverTime(String currentTimeScale) {
        long currentTime = new Date().getTime();
        long timeIncrement = calculateMilliSecondIncrement(currentTimeScale);
        int counterIncrement = calculateMaxIncrement(currentTimeScale);
        int counterStatic = calculateMaxIncrement(currentTimeScale);

        //TODO: THis orders list need stored be ordered by time.
        List<Order> ordersInTime = orderManager.getOrdersByTimePeriod((currentTime - (timeIncrement * counterIncrement)), currentTime);

        HashMap<Date, Float> salesOverTime = new HashMap<>();

        while (counterIncrement > 0) {
            float sum = 0.0f;
            for (Order order : ordersInTime) {
                if (order.getDate() <= currentTime && order.getDate() >= currentTime-timeIncrement ) {
                    sum += order.getTotalPrice().getAsFloat();
                }
            }

            counterIncrement -= 1;
            Date dateToAdd = new Date();
            if (currentTimeScale.equals("Last Day")) {
                dateToAdd = subtractHours(dateToAdd, (counterStatic - counterIncrement));
            } else {
                dateToAdd = subtractDays(dateToAdd, (counterStatic - counterIncrement));
            }
            salesOverTime.put(dateToAdd, sum);
            currentTime -= timeIncrement;
        }
       return salesOverTime;
    }

    /**
     * creates the data for average prices of order over time grpah
     * @param currentTimeScale current time period
     * @return hashmap of date and Float
     */
    public HashMap<Date, Float> getAverageOrderPricesOverTime(String currentTimeScale) {
        long currentTime = new Date().getTime();
        long timeIncrement = calculateMilliSecondIncrement(currentTimeScale);
        int counterIncrement = calculateMaxIncrement(currentTimeScale);
        int counterStatic = calculateMaxIncrement(currentTimeScale);

        List<Order> ordersInTime = orderManager.getOrdersByTimePeriod((currentTime - (timeIncrement * counterIncrement)), currentTime);
        HashMap<Date, Float> salesOverTime = new HashMap<>();

        while (counterIncrement > 0) {
            float orderSum = 0.0f;
            float priceSum = 0.0f;

            for (Order order : ordersInTime) {
                if (order.getDate() <= currentTime && order.getDate() >= currentTime-timeIncrement ) {
                    priceSum += order.getTotalPrice().getAsFloat();
                    orderSum += 1.0;
                }
            }
            float floatToAdd = 0.0f;
            if (priceSum == 0.0) {
                floatToAdd = 0.0f;
            } else {
                floatToAdd =  priceSum / orderSum;
            }
            counterIncrement -= 1;
            Date dateToAdd = new Date();
            if (currentTimeScale.equals("Last Day")) {
                dateToAdd = subtractHours(dateToAdd, (counterStatic - counterIncrement));
            } else {
                dateToAdd = subtractDays(dateToAdd, (counterStatic - counterIncrement));
            }
            salesOverTime.put(dateToAdd, floatToAdd);
            currentTime -= timeIncrement;
        }
        return salesOverTime;
    }

    /**
     * creates the data for the total orders over time graph
     * @param currentTimeScale current time period
     * @return the data in hashmap of date and int.
     */
    public HashMap<Date, Integer> getOrdersOverTime(String currentTimeScale) {
        long currentTime = new Date().getTime();
        int currentDate = calculateDateNumber(currentTimeScale, new Date());
        long timeIncrement = calculateMilliSecondIncrement(currentTimeScale);
        int counterIncrement = calculateMaxIncrement(currentTimeScale);
        int counterStatic = calculateMaxIncrement(currentTimeScale);

        List<Order> ordersInTime = orderManager.getOrdersByTimePeriod((currentTime - (timeIncrement * counterIncrement)), currentTime);
        HashMap<Date, Integer> salesOverTime = new HashMap<>();

        while (counterIncrement > 0) {
            int sum = 0;
            for (Order order : ordersInTime) {
                if (order.getDate() <= currentTime && order.getDate() >= currentTime-timeIncrement ) {
                    sum += 1;
                }
            }

            counterIncrement -= 1;
            Date dateToAdd = new Date();
            if (currentTimeScale.equals("Last Day")) {
                dateToAdd = subtractHours(dateToAdd, (counterStatic - counterIncrement));
            } else {
                dateToAdd = subtractDays(dateToAdd, (counterStatic - counterIncrement));
            }
            salesOverTime.put(dateToAdd, sum);
            currentTime -= timeIncrement;
        }
        return salesOverTime;
    }

    /**
     * Calculates the increment to make the data with. Return milliseconds of increment
     * @param currentTimeScale the current time period
     * @return timeincrement The millisecond of the increment
     */
    public long calculateMilliSecondIncrement(String currentTimeScale) {
        long timeIncrement = 0;
        int msInHour = 3600000;
        int msInDay = msInHour * 24;
        int msInWeek = msInDay * 7;
        int msInMonth = msInDay * 30;

        switch (currentTimeScale) {
            case "Last Day":
                timeIncrement = msInHour;
                break;
            case "Last Week":
                timeIncrement = msInDay;
                break;
            case "Last Month":
                timeIncrement = msInDay;
                break;
            case "Last Year":
                timeIncrement = msInMonth;
                break;
        }
        return timeIncrement;
    }

    /**
     * Calulates the number of points in the graph
     * @param currentTimeScale the current time period
     * @return the max number of points to be in the data
     */
    public int calculateMaxIncrement(String currentTimeScale) {
        int maxIncrement = 0;

        switch (currentTimeScale) {
            case "Last Day":
                maxIncrement = 24;
                break;
            case "Last Week":
                maxIncrement = 7;
                break;
            case "Last Month":
                maxIncrement = 30;
                break;
            case "Last Year":
                maxIncrement = 12;
                break;
        }
        return maxIncrement;
    }

    /**
     * calculates the current date number to start teh data with.
     * @param currentTimeScale current itme period
     * @param date the date
     * @return maxincrement which will be either Hour or Date or Month
     */
    public int calculateDateNumber(String currentTimeScale, Date date) {
        int maxIncrement = 0;
        switch (currentTimeScale) {
            case "Last Day":
                maxIncrement = date.getHours();
                break;
            case "Last Week":
                maxIncrement = date.getDate();
                break;
            case "Last Month":
                maxIncrement = date.getDate();
                break;
            case "Last Year":
                maxIncrement = date.getMonth();
                break;
        }
        return maxIncrement;
    }

    /**
     * Function to decrement the dates by days
     * @param date date
     * @param days amount to decrement
     * @return
     */
    public static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }

    /**
     * function to decrement the dates by hours.
     * @param date date
     * @param hours amount to decrement
     * @return
     */
    public static Date subtractHours(Date date, int hours) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, -hours);

        return cal.getTime();
    }


}
