package view.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import managers.AnalyticsManager;
import managers.OrderManager;
import managers.UserManager;
import models.CustomDate;
import utils.DateComparator;
import utils.MapUtil;
import view.ThemeManager;

import java.net.URL;
import java.nio.MappedByteBuffer;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Analytics Controller implements logic that runs the Analytics screen
 */
public class AnalyticsController extends MasterController implements Initializable {

    private ThemeManager themeManager = ThemeManager.getThemeManager();
    private AnalyticsManager analyticsManager = AnalyticsManager.getAnalyticsManager();

    @FXML
    private GridPane mainPane;


    @FXML
    private Pane graphPane1;

    @FXML
    private Pane graphPane2;

    @FXML
    private Pane graphPane3;

    @FXML
    private Pane graphPane4;

    @FXML
    private VBox mostPopularPane;

    @FXML
    private VBox leastPopularPane;

    @FXML
    private ComboBox timePeriodSelector;

    private String currentTimeScale = "Last Day";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeManager.setToCurrentTheme(mainPane);
        ObservableList<String> themes = themeManager.getThemeNames();
        timePeriodSelector.getSelectionModel().selectFirst();
        refreshGraphs();
    }

    @FXML
    private void timePeriodChanged(ActionEvent e){
        refreshGraphs();
    }

    /**
     * Refreshes the grpahs with the new data when changing the combobox.
     */
    public void refreshGraphs() {
        String targetTimeScale = timePeriodSelector.getSelectionModel().getSelectedItem().toString();
        HashMap<Date, Float> timeVsPrice = analyticsManager.getSalesOverTime(targetTimeScale);
        HashMap<Date, Integer> timeVsTotalOrders = analyticsManager.getOrdersOverTime(targetTimeScale);
        HashMap<Date, Float> averageOrderPriceOverTime = analyticsManager.getAverageOrderPricesOverTime(targetTimeScale);
        createSalesOverTimeChart(timeVsPrice, targetTimeScale);
        createOrdersOverTimeChart(timeVsTotalOrders, targetTimeScale);
        createAverageOrderPriceOverTimeChart(averageOrderPriceOverTime, targetTimeScale);
    }

    /** Creates the Total revenue against the current time period
     *
     * @param timeVsPrice the data to be put into the graph
     * @param targetimeScale time period to deicde what format the labels are shown as
     */
    public void createSalesOverTimeChart(HashMap<Date, Float> timeVsPrice, String targetimeScale) {
        graphPane1.getChildren().clear();

        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelRotation(90);
        xAxis.setLabel("Time");
        yAxis.setLabel("Price ($)");

        //creating the chart
        final LineChart<String, Number> lineChart = new LineChart<String,Number>(xAxis, yAxis);

        lineChart.setTitle("Revenue Over Time");

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Order Price");

        //populating the series with data
        ObservableList<Date> dates = FXCollections.observableArrayList();

        for(Date date : timeVsPrice.keySet()) {
            CustomDate customDate = new CustomDate();
            customDate.setTime(date.getTime());
            if (targetimeScale.equals("Last Day")) {
                series.getData().add(new XYChart.Data(customDate.toStringInHours(), (timeVsPrice.get(date))));
            } else {
                series.getData().add(new XYChart.Data(customDate.toStringInDate(), (timeVsPrice.get(date))));
            }
            dates.add(date);

        }
        ObservableList<String> dateStrings = FXCollections.observableArrayList();
        Collections.sort(dates, new DateComparator());
        for (Date dateSort : dates) {
            CustomDate customDate2 = new CustomDate();
            customDate2.setTime(dateSort.getTime());
            if (targetimeScale.equals("Last Day")) {
                dateStrings.add(customDate2.toStringInHours());
            } else {
                dateStrings.add(customDate2.toStringInDate());

            }
        }
        xAxis.setCategories(dateStrings);
        lineChart.getData().add(series);
        graphPane1.getChildren().add(lineChart);
    }

    /** Creates the total orders against the current time period
     *
     * @param timeVstotalOrders the data to be put into the graph
     * @param targetimeScale time period to deicde what format the labels are shown as
     */
    public void createOrdersOverTimeChart(HashMap<Date, Integer> timeVstotalOrders,  String targetimeScale) {
        graphPane2.getChildren().clear();

        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelRotation(90);

        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Number of Orders");

        //creating the chart
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis, yAxis);

        lineChart.setTitle("Orders Over Time");

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Total Orders");

        //populating the series with data
        ObservableList<Date> dates = FXCollections.observableArrayList();
        for(Date date : timeVstotalOrders.keySet()) {
            CustomDate customDate = new CustomDate();
            customDate.setTime(date.getTime());
            if (targetimeScale.equals("Last Day")) {
                series.getData().add(new XYChart.Data(customDate.toStringInHours(), (timeVstotalOrders.get(date))));
            } else {
                series.getData().add(new XYChart.Data(customDate.toStringInDate(), (timeVstotalOrders.get(date))));
            }
            dates.add(date);
        }
        ObservableList<String> dateStrings = FXCollections.observableArrayList();
        Collections.sort(dates, new DateComparator());
        for (Date dateSort : dates) {
            CustomDate customDate2 = new CustomDate();
            customDate2.setTime(dateSort.getTime());
            if (targetimeScale.equals("Last Day")) {
                dateStrings.add(customDate2.toStringInHours());
            } else {
                dateStrings.add(customDate2.toStringInDate());

            }
        }
        xAxis.setCategories(dateStrings);

        lineChart.getData().add(series);
        graphPane2.getChildren().add(lineChart);
    }

    /** Creates the average order price against the current time period
     *
     * @param averagePriceVsTime the data to be put into the graph
     * @param targetimeScale time period to deicde what format the labels are shown as
     */
    public void createAverageOrderPriceOverTimeChart(HashMap<Date, Float> averagePriceVsTime,  String targetimeScale) {
        graphPane3.getChildren().clear();

        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelRotation(90);

        xAxis.setLabel("Time");
        yAxis.setLabel("Average Price ($)");

        //creating the chart
        final LineChart<String,Number> lineChart =
                new LineChart<String,Number>(xAxis,yAxis);

        lineChart.setTitle("Average Order Price Over Time");

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Average Order Price");

        //populating the series with data
        ObservableList<Date> dates = FXCollections.observableArrayList();


        for(Date date : averagePriceVsTime.keySet()) {
            CustomDate customDate = new CustomDate();
            customDate.setTime(date.getTime());
            if (targetimeScale.equals("Last Day")) {
                series.getData().add(new XYChart.Data(customDate.toStringInHours(), (averagePriceVsTime.get(date))));
            } else {
                series.getData().add(new XYChart.Data(customDate.toStringInDate(), (averagePriceVsTime.get(date))));
            }
            dates.add(date);

        }
        ObservableList<String> dateStrings = FXCollections.observableArrayList();
        Collections.sort(dates, new DateComparator());
        for (Date dateSort : dates) {
            CustomDate customDate2 = new CustomDate();
            customDate2.setTime(dateSort.getTime());
            if (targetimeScale.equals("Last Day")) {
                dateStrings.add(customDate2.toStringInHours());
            } else {
                dateStrings.add(customDate2.toStringInDate());

            }
        }
        xAxis.setCategories(dateStrings);
        lineChart.getData().add(series);
        graphPane3.getChildren().add(lineChart);
    }

//    public void createMostPopularList(HashMap<String, Integer> popularHashMap) {
//        int count = 1;
//        //TODO: Use MapUtil properly
//        MapUtil sorted = new MapUtil(popularHashMap);
//        mostPopularPane.getChildren().clear();
//        mostPopularPane.getChildren().add(new Label("Most Popular Items"));
//        for (String menuItemName : sorted.keySet()) {
//            HBox container = new HBox();
//            Label name = new Label(count + ". " + menuItemName);
//            Label total = new Label(sorted.get(menuItemName));
//            container.getChildren().add(name);
//            container.getChildren().add(total);
//            mostPopularPane.getChildren().add(container);
//            count += 1;
//        }
//    }

//    public void createLeastPopularList(HashMap<String, Integer> popularHashMap) {
//        int count = 1;
//        //TODO: Use MapUtil properly
//        MapUtil sorted = new MapUtil(popularHashMap);
//        leastPopularPane.getChildren().clear();
//        leastPopularPane.getChildren().add(new Label("Least Popular Items"));
//        for (String menuItemName : sorted.keySet()) {
//            HBox container = new HBox();
//            Label name = new Label(menuItemName);
//            Label total = new Label(sorted.get(menuItemName));
//            container.getChildren().add(name);
//            container.getChildren().add(total);
//            leastPopularPane.getChildren().add(container);
//            count += 1;
//        }
//    }

    public void changeTimeScale(String scale) {
        currentTimeScale = scale;
    }
}
