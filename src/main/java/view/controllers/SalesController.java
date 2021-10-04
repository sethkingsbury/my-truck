package view.controllers;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import managers.DataManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import managers.IngredientManager;
import managers.MenuItemManager;
import managers.OrderManager;
import models.*;
import models.Order;
import view.ThemeManager;
import view.components.MenuItemPane;
import view.components.OrderItemVBox;
import view.eventHandlers.HoverHandler;
import view.eventHandlers.SelectionHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Manages the sales screen
 */
public class SalesController extends MasterController implements Initializable {

    // General use vars
    /**
     * Gets data manager to access database data
     */
    private DataManager dataManager = DataManager.getDataManager();
    /**
     * Gets order manager to store current order
     */
    private OrderManager orderManager = OrderManager.getOrderManager();
    private IngredientManager ingredientManager = IngredientManager.getIngredientManager();
    private MenuItemManager menuItemManager = MenuItemManager.getMenuItemManager();


    private ThemeManager themeManager = ThemeManager.getThemeManager();
    /**
     * Current order being accessed
     */
    private Order order = null;

    @FXML
    private GridPane mainPane;

    // Swapping item category vars
    @FXML
    private HBox categoryBar;
    /**
     * Keeps track of current pane
     */
    private GridPane currentPane = null;

    ObservableList<MenuItem> menuItems;

    @FXML
    private GridPane menuItemsGrid6;
    @FXML
    private GridPane menuItemsGrid5;
    @FXML
    private GridPane menuItemsGrid4;
    @FXML
    private GridPane menuItemsGrid3;
    @FXML
    private GridPane menuItemsGrid2;
    @FXML
    private GridPane menuItemsGrid1;
    /**
     * Mapping of categories to gridPanes to allow access from buttons
     */
    private HashMap<String, GridPane> gridToButtonMap = new HashMap<>();

    // Order bar vars
    @FXML
    private VBox orderBar;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label orderNumLabel;
    /**
     * Maps remove Button to MenuItem
     */
    private HashMap<Button, MenuItem> removeButtonMap = new HashMap<>();

    /**
     * Maps Menu Item buttons (MenuItemPanes) to MenuItem
     */
    private HashMap<MenuItemPane, MenuItem> menuItemButtonMap = new HashMap<>();

    // Edit Ingredient vars
    @FXML
    private Label currentFocusLabel;
    @FXML
    private GridPane ingredientsGrid;
    @FXML
    private Button addIngredientButton;
    @FXML
    private Button removeIngredientButton;

    @FXML
    private Text glutenFreeCheck;

    @FXML
    private Text vegeterianCheck;

    @FXML
    private Text veganCheck;

    @FXML
    private Pane dietaryPane;

    /**
     * Maps ingredient button to Ingredient
     */
    private HashMap<Button, Ingredient> buttonToIngredientMap = new HashMap<>();
    /**
     * Count to keep track of which ingredients to show
     */
    private int ingredientPageCount = 0;
    /**
     * Current item to be edited
     */
    private MenuItem currentFocus;
    /**
     * List of ingredients from the item that can be added or removed
     */
    private HashMap<Ingredient, Integer> itemIngredients;
    /**
     * boolean true if ingredient is added, false if ingredient is removed
     */
    private boolean addOrRemove = true;

    /**
     * Initializes the scene with the first screen and data
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPane = menuItemsGrid1;

        // If order currently in progress will display that
        order = orderManager.getOrder();
        displayOrder();

        // Gets the item data
        try {
            menuItemManager.refreshMenuItems();
            menuItemManager.setMenuItemCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        menuItems = menuItemManager.getMenuItems();
        ObservableList<String> categories = menuItemManager.getMenuItemCategories();
        dataManager.linkIngredientsToMenuItems();
        try {
            dataManager.updateAllMenuItemServings(menuItems);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Creates a group of gridPanes to iterate through
        ArrayList<GridPane> gridPanes = groupGrids();
        // Initializes the changeable scene elements
        buildGrids(categories, menuItems, gridPanes);
        createCategoryButtons(categories);

        // Clickable
        SelectionHandler selectionHandler = new SelectionHandler(orderBar);
        orderBar.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMouseClickedHandler());

        themeManager.setToCurrentTheme(mainPane);
    }

    /**
     * Groups the gridPanes into an Array to be iterated through
     *
     * @return ArrayList<GridPane>
     */
    private ArrayList<GridPane> groupGrids() {
        ArrayList<GridPane> gridPanes = new ArrayList<GridPane>();
        gridPanes.add(menuItemsGrid1);
        gridPanes.add(menuItemsGrid2);
        gridPanes.add(menuItemsGrid3);
        gridPanes.add(menuItemsGrid4);
        gridPanes.add(menuItemsGrid5);
        gridPanes.add(menuItemsGrid6);
        return gridPanes;
    }

    /**
     * Initializes all the gridPanes with the menuItems, each gridPane is related to one category
     *
     * @param categories
     * @param menuItems
     * @param gridPanes
     */
    private void buildGrids(ObservableList<String> categories, ObservableList<MenuItem> menuItems, ArrayList<GridPane> gridPanes) {
        // Iterates through the menus categories and adds the appropriate items to the right grids
        gridToButtonMap = new HashMap<String, GridPane>();
        for (int i = 0; i < categories.size(); i++) {
            createButtons(menuItems, gridPanes.get(i), categories.get(i));
            gridToButtonMap.put(categories.get(i), gridPanes.get(i));
        }
    }

    private void clearGrids() {
        gridToButtonMap.clear();
        for (GridPane gridPane : groupGrids()) {
            gridPane.getChildren().clear();
        }
    }

    /**
     * Creates all the item buttons
     *
     * @param itemList
     * @param menuItemsGrid
     * @param category
     */
    private void createButtons(ObservableList<MenuItem> itemList, GridPane menuItemsGrid, String category){
        SelectionHandler selectionHandler = new SelectionHandler(menuItemsGrid);
        menuItemsGrid.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMouseClickedHandler());

        menuItemsGrid.getChildren().clear();

        // Index of item being processed
        int index = 0;

        // Row and column indices
        int column = 0;
        int row = 0;

        // Loops through every item in the given list
        while (row != 8 && index < itemList.size()) {
            // Checks if item should be added to current categories grid
            if (itemList.get(index).getCategory().equals(category)) {
                // Creates a new button for the item
                MenuItem item = itemList.get(index);
                item.setServings(dataManager.calculateMenuItemServings(item));
                MenuItemPane newItemPane = new MenuItemPane(this,item);
                HoverHandler hoverHandler = new HoverHandler(newItemPane);
                newItemPane.addEventHandler(MouseEvent.MOUSE_ENTERED, hoverHandler.getMouseEnteredHandler());
                newItemPane.addEventHandler(MouseEvent.MOUSE_EXITED, hoverHandler.getMouseExitedHandler());

                menuItemsGrid.setConstraints(newItemPane, column + 1, row + 1);
                menuItemsGrid.getChildren().add(newItemPane);
                menuItemButtonMap.put(newItemPane, item);

                if (item.getServings() == 0) {
                    newItemPane.setDisable(true);
                }
                // Moves to next grid space
                if (column == 16) {
                    column = 0;
                    row += 2;
                } else {
                    column += 2;
                }
            }
            // Moves to next item
            index += 1;

        }
    }

    /**
     * Updates the labels to represent how many servings of each menu item are available.
     *
     */
    public void updateMenuItemLabels() {
        for (MenuItemPane menuItemPane : menuItemButtonMap.keySet()) {
            menuItemPane.setItemServings(dataManager.calculateMenuItemServings(menuItemButtonMap.get(menuItemPane)));
            if (menuItemPane.getItemServings() > 0) {
                menuItemPane.setDisable(false);
            }
        }

    }

    /**
     * Creates all the category buttons
     *
     * @param categories
     */
    private void createCategoryButtons(ObservableList<String> categories) {
        int index = 0;

        // Loops through all given categories creating a button for each
        while(index < categories.size()) {
            Button newButton = new Button();
            newButton.setMaxHeight(119);
            newButton.setMinHeight(119);
            newButton.setMaxWidth(200);
            newButton.setMinWidth(200);
            categoryBar.getChildren().add(newButton);
            newButton.getStyleClass().add("categoryButton");
            newButton.setText(categories.get(index));
            newButton.setOnAction(this::swapMainPane);
            index++;
        }
    }

    /**
     * Swaps to given pane from current pane
     *
     * @param newPane
     */
    private void swapToPane(GridPane newPane) {
        currentPane.setVisible(false);
        newPane.setVisible(true);
        currentPane = newPane;
    }

    /**
     * Method for category buttons to switch panes
     *
     * @param e
     */
    private void swapMainPane(ActionEvent e){
        Button buttonSelected = (Button) e.getSource();
        swapToPane(gridToButtonMap.get(buttonSelected.getText()));
    }

    public void selectOrderItem(MenuItem item) {
        updateIngredientEdit(item);
        buildOrderDisplay();
    }

    /**
     * Displays the current order on the right side of the screen
     */
    private void buildOrderDisplay() {
        HashMap<MenuItem, Integer> items = order.getItems();
        orderBar.getChildren().clear();
        int count = 0;
        String style;

        for (MenuItem item : items.keySet()) {
            if (item.equals(currentFocus)) {
                style = "selectedItem";
            } else if (count % 2 == 0) {
                style = "evenItem";
            } else {
                style = "oddItem";
            }

            OrderItemVBox container = new OrderItemVBox(item, order, this, style);
            container.getStyleClass().clear();
            container.getStyleClass().add(style);

            Button removeButton = new Button("x");
            removeButtonMap.put(removeButton, item);
            removeButton.setId("removeButton");
            removeButton.setOnAction(this::removeItem);
            removeButton.setMaxHeight(20);
            removeButton.setMinHeight(20);
            removeButton.setPrefHeight(20);
            container.getTitle().getChildren().add(removeButton);

            orderBar.getChildren().add(container);

            count += 1;
        }
    }

    /**
     * Updates the total price display
     */
    private void updateTotal() {
        totalPriceLabel.setText(order.getTotalPrice().toString());
    }

    /**
     * Calls all methods to display the current order or update display
     */
    private void displayOrder() {
        buildOrderDisplay();
        updateTotal();
        updateOrderNum();
    }

    /**
     * Adds the given item to the order with the given quantity
     *
     * @param item MenuItem to be added
     * @param quantity int quantity of item to be added
     */
    private void addToOrder(MenuItem item, int quantity) {
        order.addItem(item, quantity);
    }

    /**
     * Called by the item buttons
     *
     * @param item MenuItem to be added
     */
    public void addMenuItem(MenuItem item) {
        addOrRemove = true;
        addIngredientButton.setDisable(true);
        removeIngredientButton.setDisable(false);
        addToOrder(item, 1);
        try {
            ingredientManager.refreshStockIngredientAmounts(item, -1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateIngredientEdit(item);
        try {
            dataManager.updateAllMenuItemServings(menuItems);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateMenuItemLabels();
        displayOrder();
    }

    private String getTime() {
        int second = LocalDateTime.now().getSecond();
        int minute = LocalDateTime.now().getMinute();
        int hour = LocalDateTime.now().getHour();
        return hour + ":" + (minute) + ":" + second;
    }


    /**
     * Called by confirm order button, creates a new order and updates displays
     *
     * @param e event
     */
    public void completeOrder(ActionEvent e) throws SQLException {
        Money m = order.getTotalPrice();
        if (!m.equals(0)) {
            order.setTime(getTime());
            order.setDate(new Date().getTime());
            orderManager.setOrder(order);
            displayOrder();

            try {
                swapToViewChangeScene(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Removes given item from the current order
     *
     * @param item MenuItem to remove
     */
    private void removeFromOrder(MenuItem item) {
        order.removeItem(item);
    }

    /**
     * Called by remove button to remove selected item
     *
     * @param e ActionEvent to get source
     */
    private void removeItem(ActionEvent e){
        Button sourceButton = (Button) e.getSource();
        removeFromOrder(removeButtonMap.get(sourceButton));
        try {
            if (removeButtonMap.get(sourceButton) instanceof CustomMenuItem) {
                CustomMenuItem item = ((CustomMenuItem) removeButtonMap.get(sourceButton));
                ingredientManager.refreshStockIngredientAmountsbyIngredients(item.getCustomIngredients());
            } else {
                ingredientManager.refreshStockIngredientAmounts(removeButtonMap.get(sourceButton), 1);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        displayOrder();
        try {
            dataManager.updateAllMenuItemServings(menuItems);
            updateMenuItemLabels();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }



        clearIngredientButtons();
    }

    /**
     * Updates the order number at the top of the screen
     */
    private void updateOrderNum() {
        orderNumLabel.setText(Integer.toString(orderManager.getLatestOrderNumber()));
    }

    /**
     * Clears currently displayed ingredients
     */
    private void clearIngredientButtons() {
        for (Button button: buttonToIngredientMap.keySet()) {
            ingredientsGrid.getChildren().remove(button);
        }
        glutenFreeCheck.setVisible(false);
        vegeterianCheck.setVisible(false);
        veganCheck.setVisible(false);
        dietaryPane.setVisible(false);
    }

    /**
     * Creates the ingredient buttons for adding and removing ingredients
     */
    private void buildIngredientButtons() {
        int startIndex = 7 * ingredientPageCount;
        int endIndex = startIndex + 7;
        int column = 3;
        int index = 0;

        // Clears buttons currently there
        clearIngredientButtons();
        glutenFreeCheck.setVisible(true);
        vegeterianCheck.setVisible(true);
        veganCheck.setVisible(true);
        dietaryPane.setVisible(true);

        // Sort ingredients
        TreeMap<Ingredient, Integer> sortedIngredients = new TreeMap<>(itemIngredients);

        // Creates new buttons
        for (Ingredient ingredient : sortedIngredients.keySet()) {

            if (index >= startIndex && index < endIndex) {
                Button newButton = new Button();
                newButton.setMaxWidth(100);
                newButton.setMinWidth(100);
                newButton.setMaxHeight(100);
                newButton.setMinHeight(100);
                ingredientsGrid.setConstraints(newButton, column, 3);
                ingredientsGrid.getChildren().add(newButton);
                newButton.getStyleClass().add("menuItemButton");
                newButton.setOnAction(this::editItem);
                newButton.setText(ingredient.getName() + "\nItem: " + sortedIngredients.get(ingredient) + "\nStock: " + ingredientManager.getIngredientByCode(ingredient.getCode()).getAmount());
                Ingredient tempIngredient = ingredient;
                buttonToIngredientMap.put(newButton, tempIngredient);
                column += 2;
            }
            index++;
        }
    }

    /**
     * Updates the ingredients shown to match the given item
     *
     * @param item MenuItem selected
     */
    private void updateIngredientEdit(MenuItem item) {

        HashMap<Ingredient, Integer> ingredients = null;
        try {
            MenuItem baseItem = menuItemManager.getMenuItemByCode(item.getCode());
            ingredients = menuItemManager.getMenuItemIngredients(baseItem);
            item.setIngredients(ingredients);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        currentFocus = item;
        if (addOrRemove) {
            currentFocusLabel.setText("Click ingredient to add to " + currentFocus.getName() + ":");
        } else {
            currentFocusLabel.setText("Click ingredient to remove from " + currentFocus.getName() + ":");

        }

        if(item instanceof CustomMenuItem) {
            itemIngredients = ((CustomMenuItem) item).getCustomIngredients();
        } else if (!(item instanceof CustomMenuItem)) {
            itemIngredients = item.getIngredients();

        }
        buildIngredientButtons();
        updateDietaryLabels(itemIngredients);
    }

    private void updateDietaryLabels( HashMap<Ingredient, Integer> ingredients) {
        ArrayList<String> dietary = new ArrayList<>();
        try {
            dietary = menuItemManager.getMenuItemDietaryStatus(ingredients);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        glutenFreeCheck.setText("Gluten Free: " + dietary.get(0).toUpperCase());
        vegeterianCheck.setText("Vegeterian: " + dietary.get(1).toUpperCase());
        veganCheck.setText("     Vegan:    " + dietary.get(2).toUpperCase());
    }

    /**
     * Switches to removing ingredients
     */
    public void switchToRemove() {
        if (itemIngredients != null) {
            addOrRemove = false;
            addIngredientButton.setDisable(false);
            removeIngredientButton.setDisable(true);
            currentFocusLabel.setText("Click ingredient to remove from " + currentFocus.getName() + ":");
            buildIngredientButtons();
        }
    }

    /**
     * Switches to adding ingredients
     */
    public void switchToAdd() {
        if (itemIngredients != null) {
            addOrRemove = true;
            addIngredientButton.setDisable(true);
            removeIngredientButton.setDisable(false);
            currentFocusLabel.setText("Click ingredient to add to " + currentFocus.getName() + ":");
            buildIngredientButtons();
        }
    }

    /**
     * Moves to the next ingredient page
     */
    public void nextPage() {
        if (itemIngredients != null) {
            if ((ingredientPageCount + 1) * 7 < itemIngredients.size()) {
                ingredientPageCount++;
            } else {
                ingredientPageCount = 0;
            }
            buildIngredientButtons();
        }
    }

    /**
     * Moves to the previous ingredient page
     */
    public void prevPage() {
        if (itemIngredients != null) {
            if (ingredientPageCount > 0) {
                ingredientPageCount--;
            } else {
                ingredientPageCount = itemIngredients.size() / 7;
            }
            buildIngredientButtons();
        }
    }

    /**
     * Adds or removes ingredient from selected item
     *
     * @param e ActionEvent to get source
     */
    private void editItem(ActionEvent e) {
        Button selectedButton = (Button) e.getSource();
        Ingredient ingredient = buttonToIngredientMap.get(selectedButton);

        if (!(currentFocus instanceof CustomMenuItem)) {
            CustomMenuItem customItem = new CustomMenuItem(currentFocus.getCode(), currentFocus.getName(),
                    currentFocus.getPrice(), currentFocus.getCategory(), currentFocus.getIngredients());
            removeFromOrder(currentFocus);
            currentFocus = customItem;
            addToOrder(customItem, 1);
        }
        if (addOrRemove) {
            if (ingredientManager.getIngredientByCode(ingredient.getCode()).getAmount() != 0) {
                ((CustomMenuItem) currentFocus).addIngredient(ingredient, 1);
                try {
                    ingredientManager.updateStockAmount(ingredient, -1);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            if (((CustomMenuItem) currentFocus).getCustomIngredients().get(ingredient) > 0) {
                try {
                    ingredientManager.updateStockAmount(ingredient, 1);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            ((CustomMenuItem) currentFocus).removeIngredient(ingredient);
        }
        displayOrder();
        updateIngredientEdit(currentFocus);
        updateMenuItemLabels();
        updateDietaryLabels(itemIngredients);
    }

//    public void clearOrder() {
//        while (removeButtonMap.size() > 0) {
//            for (Button button : removeButtonMap.keySet()) {
//                if (removeButtonMap.get(button))
//                button.fire();
//            }
//        }
//    }

}
