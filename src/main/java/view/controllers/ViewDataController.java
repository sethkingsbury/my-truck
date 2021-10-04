package view.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import managers.DataManager;
import managers.IngredientManager;
import managers.MenuItemManager;
import managers.UserManager;
import models.*;
import models.MenuItem;
import view.ThemeManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * View Data Controller implements logic that runs behind the View screen in the program
 */
public class ViewDataController extends MasterController implements Initializable {

    private Pane currentPane = null;
    private DataManager dataManager = DataManager.getDataManager();
    private IngredientManager ingredientManager = IngredientManager.getIngredientManager();
    private MenuItemManager menuItemManager = MenuItemManager.getMenuItemManager();
    private UserManager userManager = UserManager.getUserManager();


    private ThemeManager themeManager = ThemeManager.getThemeManager();

    @FXML
    private GridPane mainPane;

    @FXML
    private ComboBox dataSelectCombo;

    @FXML
    private Pane basePane;

    @FXML
    private Pane ingredientPane;

    @FXML
    private Pane menuItemPane;

    @FXML
    private Pane userPane;

    @FXML
    private Pane supplierPane;

    @FXML
    private Pane cashFloatPane;

    @FXML
    private VBox editPane2;

    @FXML
    private VBox editPane3;

    @FXML
    private VBox editPane4;

    // Ingredient table
    @FXML
    private TableView<Ingredient> ingredientTable;

    @FXML
    private TableColumn<Ingredient, String> ingredientId;

    @FXML
    private TableColumn<Ingredient, String> ingredientCode;

    @FXML
    private TableColumn<Ingredient, String> ingredientName;

    @FXML
    private TableColumn<Ingredient, Integer> ingredientPrice;

    @FXML
    private TableColumn<Ingredient, Integer> ingredientAmount;

    @FXML
    private TableColumn<Ingredient, String> ingredientMeasurement;

    @FXML
    private TableColumn<Ingredient, Boolean> ingredientGF;

    @FXML
    private TableColumn<Ingredient, Boolean> ingredientVeg;

    @FXML
    private TableColumn<Ingredient, Boolean> ingredientVegan;

    @FXML
    private Text ingredientNameMissing;

    @FXML
    private Text ingredientCodeMissing;

    @FXML
    private Text ingredientCodeExists;

    @FXML
    private Text ingredientMeasurementMissing;

    // MenuItem table
    @FXML
    private TableView<MenuItem> menuItemTable;

    @FXML
    private TableColumn<MenuItem, String> menuItemName;

    @FXML
    private TableColumn<MenuItem, Integer> menuItemServings;

    @FXML
    private TableColumn<MenuItem, Integer> menuItemPrice;

    @FXML
    private TableColumn<MenuItem, String> menuItemCategory;

    /**
     * Error checkers for menuItem Text Fields.
     */
    @FXML
    private Text itemNameMissing;

    @FXML
    private Text itemPriceMissing;

    @FXML
    private Text itemCategoryMissing;

    private boolean itemFailsCheck;

    @FXML
    private Text supplierErrorMessage;

    @FXML
    private Text ingredientErrorMessage;

    @FXML
    private Text menuItemErrorMessage;

    // Supplier table
    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    private TableColumn<Supplier, String> supplierName;

    @FXML
    private TableColumn<Supplier, String> supplierContact;

    @FXML
    private Text supplierNameMissing;

    @FXML
    private Text supplierContactMissing;

    // User table
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> userId;

    @FXML
    private TableColumn<User, String> userUsername;

    @FXML
    private TableColumn<User, String> userPermissions;

    @FXML
    private Label ingMenuItems;

    // Cash Float Table
    @FXML
    private TableView<CashFloat> cashFloatTable;

    @FXML
    private TableColumn<CashFloat, String> cashFloatDenoms;

    @FXML
    private TableColumn<CashFloat, String> cashFloatType;

    @FXML
    private TableColumn<CashFloat, Integer> cashFloatQuantity;


    ArrayList<String> newData = new ArrayList<>();

    private HashMap<Ingredient, Integer> recipeIngredients = new HashMap<>();

    private HashMap<Button, Ingredient> removeButtonMap = new HashMap<Button, Ingredient>();

    private HashMap<Button, Ingredient> addButtonMap = new HashMap<Button, Ingredient>();

    static MenuItem currentMenuItem;
    static Ingredient currentIngredient;
    static User currentlyEditingUser;
    static Supplier currentEditingSupplier;
    static CashFloat currentlyEditingCashFloat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeManager.setToCurrentTheme(mainPane);
        // TODO: Add support for changing tables
        dataSelectCombo.getSelectionModel().selectFirst();
        currentPane = ingredientPane;
        swapToPane(ingredientPane);
        try {
            ingredientManager.refreshIngredients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dataManager.updateAllMenuItemServings(menuItemManager.getMenuItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        populateIngredientTable();
        populateMenuItemTable();
        populateSupplierTable();

        if (userManager.getCurrentlyLoggedInUser().getAccountType() == 3) {
            populateUserTable();
            populateCashFloatTable();
            dataSelectCombo.getItems().add("Users");
            dataSelectCombo.getItems().add("Cash Float");
        }

        swapMainPane();
    }

    public void populateCashFloatTable() {
        dataManager.refreshCashFloat();

        cashFloatTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        cashFloatDenoms.setCellValueFactory(new PropertyValueFactory<CashFloat, String>("denomination"));
        cashFloatType.setCellValueFactory(new PropertyValueFactory<CashFloat, String>("type"));
        cashFloatQuantity.setCellValueFactory(new PropertyValueFactory<CashFloat, Integer>("quantity"));

        cashFloatTable.setItems(dataManager.getCashFloats());
    }

    /**
     * populates the table, using setCellValueFactory
     */
    public void populateIngredientTable() {
        ingredientTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ingredientId.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("id"));
        ingredientCode.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("code"));
        ingredientName.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));
        ingredientPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        ingredientAmount.setCellValueFactory(new PropertyValueFactory<Ingredient, Integer>("amount"));
        ingredientMeasurement.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("quantityMeasuredIn"));
        ingredientGF.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("glutenFree"));
        ingredientVeg.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("vegetarian"));
        ingredientVegan.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("vegan"));

        ingredientTable.setItems(ingredientManager.getIngredients());
    }

    /**
     *
     *  populates the table, using setCellValueFactory
     *
     */
    public void populateSupplierTable() {
		supplierTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		supplierName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		supplierContact.setCellValueFactory(new PropertyValueFactory<Supplier, String>("contact"));
		supplierTable.setItems(DataManager.getDataManager().getSuppliers());
    }

    /**
     * populates the table, using setCellValueFactory
     */
    public void populateUserTable() {
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        userId.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
        userUsername.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        userPermissions.setCellValueFactory(new PropertyValueFactory<User, String>("accountType"));

        userTable.setItems(UserManager.getUserManager().getUsers());
    }


    /**
     * populates the table, using setCellValueFactory
     */
    public void populateMenuItemTable() {
        menuItemTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        menuItemName.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("name"));
        menuItemServings.setCellValueFactory(new PropertyValueFactory<MenuItem, Integer>("servings"));
        menuItemPrice.setCellValueFactory(new PropertyValueFactory<MenuItem, Integer>("price"));
        menuItemCategory.setCellValueFactory(new PropertyValueFactory<MenuItem, String>("category"));

        menuItemTable.setItems(menuItemManager.getMenuItems());
        menuItemTable.setItems(menuItemManager.getMenuItems());
    }

    public void swapToPane(Pane newPane) {
        currentPane.setVisible(false);
        newPane.setVisible(true);
        currentPane = newPane;
    }

    public void swapMainPane() {
        if (currentPane == null) {
            currentPane = basePane;
        }

        try {
            if (dataSelectCombo.getValue().equals("Ingredients")) {
                ingredientManager.refreshIngredients();
                resetEditPane();
                swapToPane(ingredientPane);
            } else if (dataSelectCombo.getValue().equals("Suppliers")) {
                dataManager.refreshSuppliers();
                resetEditPane();
                swapToPane(supplierPane);
            } else if (dataSelectCombo.getValue().equals("Menu Items")) {
                menuItemManager.refreshMenuItems();
                resetEditPane();
                swapToPane(menuItemPane);
            } else if (dataSelectCombo.getValue().equals("Users")) {
                userManager.refreshUsers();
                resetEditPane();
                swapToPane(userPane);
            } else if (dataSelectCombo.getValue().equals("Cash Float")) {
                //dataManager.refreshCashFloat();
                resetEditPane();
                swapToPane(cashFloatPane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetEditPane() {
        editPane2.getChildren().clear();
        editPane3.getChildren().clear();
        editPane4.getChildren().clear();
    }

    /**
     * showIngredientMenuItems() displays the text with the selected item
     * @throws SQLException
     */
    @FXML
    public void showIngredientMenuItems() throws SQLException {
        String affectedMenuItemNames = "";
        Ingredient selectedIngredient = ingredientTable.getSelectionModel().getSelectedItem();
        ArrayList<MenuItem> affectedMenuItems = new ArrayList<>();
        try {
            affectedMenuItems = ingredientManager.getAllIngredientMenuItems(selectedIngredient);
        } catch (SQLException e) { e.printStackTrace(); }

        if (!affectedMenuItems.isEmpty()) {
            affectedMenuItemNames += "Used in menu items below:\n";
            for (MenuItem m : affectedMenuItems) {
                affectedMenuItemNames += menuItemManager.getMenuItemById(m.getId()).getName();
                affectedMenuItemNames += "\n";
            }
        }

        ingMenuItems.setText(affectedMenuItemNames);
    }

    /**
     * this is a  delete confirm button
     * @param event
     */
    @FXML
    private void enableConfirmDelete(ActionEvent event) {
        String selectedOption = (String) dataSelectCombo.getValue();
        String confirmString = "Are you sure you want to delete ";
        if (selectedOption.equals("Ingredients")) {
            if (ingredientTable.getSelectionModel().getSelectedItem() == null) { return; }
            confirmString += ingredientTable.getSelectionModel().getSelectedItem().getName() + "?";
        } else if (selectedOption.equals("Menu Items")) {
            if (menuItemTable.getSelectionModel().getSelectedItem() == null) { return; }
            confirmString += menuItemTable.getSelectionModel().getSelectedItem().getName() + "?";
        } else if (selectedOption.equals("Suppliers")) {
            if (supplierTable.getSelectionModel().getSelectedItem() == null) { return; }
            confirmString += supplierTable.getSelectionModel().getSelectedItem().getName() + "?";
        } else if (selectedOption.equals("Users")) {
            User targetUser = userTable.getSelectionModel().getSelectedItem();
            if (!userManager.getCurrentlyLoggedInUser().getUsername().equals(targetUser.getUsername())) {
                confirmString += userTable.getSelectionModel().getSelectedItem().getUsername() + "?";
            }
        }
        if (!(selectedOption.equals("Cash Float"))) {
            try {
                openConfirm(event, confirmString, ViewDataController.class.getMethod("deleteSelectedRow"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * deleteSelectedRow() actually finds and removes the ingredient from the database, then resets the table to refresh it
     */
    public void deleteSelectedRow() {
    	String selectedOption = (String) dataSelectCombo.getValue();
        if (selectedOption.equals("Ingredients")) {
            if (ingredientTable.getSelectionModel().getSelectedItem() == null) { return; }
            ingredientManager.deleteIngredient(ingredientTable.getSelectionModel().getSelectedItem());
            ingredientTable.getItems().removeAll(ingredientTable.getSelectionModel().getSelectedItem());
            resetEditPane();
        } else if (selectedOption.equals("Menu Items")) {
            if (menuItemTable.getSelectionModel().getSelectedItem() == null) { return; }
            MenuItem selectedMenuItem = menuItemTable.getSelectionModel().getSelectedItem();
            menuItemManager.deleteMenuItem(selectedMenuItem);
            menuItemTable.getItems().removeAll(menuItemTable.getSelectionModel().getSelectedItem());
            resetEditPane();
        } else if (selectedOption.equals("Suppliers")) {
            if (supplierTable.getSelectionModel().getSelectedItem() == null) { return; }
            dataManager.deleteExistingSupplier(supplierTable.getSelectionModel().getSelectedItem());
            supplierTable.getItems().removeAll(supplierTable.getSelectionModel().getSelectedItem());
            resetEditPane();
        } else if (selectedOption.equals("Users")) {
            User targetUser = userTable.getSelectionModel().getSelectedItem();
            if (!userManager.getCurrentlyLoggedInUser().getUsername().equals(targetUser.getUsername())) {
                userManager.deleteUser(targetUser.getId());
                userTable.getItems().remove(targetUser);
                resetEditPane();
            }
        }
    }

    /**
     * Creates a new entry into whatever table is selected
     * @param event
     * @throws IOException
     */
    @FXML
    private void createNewData(ActionEvent event) throws IOException {
        Stage createStage = new Stage();
        Parent createScene;
        int width = 0;
        int height = 0;
        createStage.centerOnScreen();
        if (dataSelectCombo.getValue().equals("Ingredients")) {
            currentIngredient = null;
            createScene = FXMLLoader.load(getClass().getResource("/gui/scenes/createIngredient.fxml"));
            createStage.setTitle("Create Ingredient");
            width = 700;
            height = 480;
            createStage.setScene(new Scene(createScene));
        } else if (dataSelectCombo.getValue().equals("Menu Items")) {
            currentMenuItem = null; // so that it doesnt think we are editing a MenuItem
            createScene = FXMLLoader.load(getClass().getResource("/gui/scenes/createMenuItem.fxml"));
            createStage.setTitle("Create Menu Item");
            width = 1420;
            height = 960;
            createStage.setScene(new Scene(createScene));
        } else if (dataSelectCombo.getValue().equals("Suppliers")) {
            currentEditingSupplier = null;
            createScene = FXMLLoader.load(getClass().getResource("/gui/scenes/createSupplier.fxml"));
            createStage.setTitle("Create Supplier");
            width = 700;
            height = 300;
            createStage.setScene(new Scene(createScene));
        } else if (dataSelectCombo.getValue().equals("Users")) {
            currentlyEditingUser = null; // so that it doesnt think we are editing a user
            createScene = FXMLLoader.load(getClass().getResource("/gui/scenes/createUser.fxml"));
            createStage.setTitle("Create User");
            width = 700;
            height = 300;
            createStage.setScene(new Scene(createScene));
        }
        if (!(dataSelectCombo.getValue().equals("Cash Float"))) {
            createStage.setWidth(width);
            createStage.setHeight(height);

            createStage.initModality(Modality.APPLICATION_MODAL);
            createStage.showAndWait();
        }
    }

    /**
     * shows popup window
     * @param newStage The window name
     * @param pathToFXML loads the required information to edit/add
     * @param width width of window
     * @param height height of window
     * @throws IOException
     */
    private void showPopupWindow(Stage newStage, String pathToFXML, int width, int height) throws IOException {
        Parent scene = FXMLLoader.load(getClass().getResource(pathToFXML));
        newStage.setWidth(width);
        newStage.setHeight(height);
        newStage.setScene(new Scene(scene));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }

    /**
     * this function decides what table we are going to edit, then launches the popup window with the fields for input
     * @param event
     * @throws Exception
     */
    @FXML
    private void editSelectedRow(ActionEvent event) throws Exception {
        Stage editStage = new Stage();

        if (dataSelectCombo.getValue().equals("Ingredients")) {
            if (ingredientTable.getSelectionModel().getSelectedItem() == null) { return; }
            currentIngredient = ingredientTable.getSelectionModel().getSelectedItem();
            showPopupWindow(editStage, "/gui/scenes/createIngredient.fxml", 700, 480);

        } else if (dataSelectCombo.getValue().equals("Menu Items")) {
            if (menuItemTable.getSelectionModel().getSelectedItem() == null) { return; }
            currentMenuItem = menuItemTable.getSelectionModel().getSelectedItem();
            showPopupWindow(editStage, "/gui/scenes/createMenuItem.fxml", 1420, 960);

        } else if (dataSelectCombo.getValue().equals("Suppliers")) {
            if (supplierTable.getSelectionModel().getSelectedItem() == null) { return; }
            currentEditingSupplier = supplierTable.getSelectionModel().getSelectedItem();
            showPopupWindow(editStage, "/gui/scenes/createSupplier.fxml", 700, 300);

        } else if (dataSelectCombo.getValue().equals("Users")) {
            if (userTable.getSelectionModel().getSelectedItem() == null) { return; }
            currentlyEditingUser = userTable.getSelectionModel().getSelectedItem();
            showPopupWindow(editStage, "/gui/scenes/createUser.fxml", 700, 300);

        } else if (dataSelectCombo.getValue().equals("Cash Float")) {
            currentlyEditingCashFloat = cashFloatTable.getSelectionModel().getSelectedItem();
            showPopupWindow(editStage, "/gui/scenes/createCashFloat.fxml", 540, 480);
        }
    }

    /**
     * loadMenuItemData takes all the entries from the menu items and loads it into the pane
     * @param menuItem
     * @throws SQLException
     */
    private void loadMenuItemData(MenuItem menuItem) throws SQLException {
        editPane2.getChildren().clear();
        Label name = new Label("Menu Item Name: ");
        TextField menuItemName = new TextField(menuItem.getName());
        Label code = new Label("Menu Item Code: ");
        Label menuItemCode = new Label(menuItem.getCode());
        Label category = new Label("Menu Item Category: ");
        TextField menuItemCategory = new TextField(menuItem.getCategory());
        Label price = new Label("Menu Item Price: ");
        TextField menuItemPrice = new TextField(Float.toString(menuItem.getPrice().getAsFloat()));

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().add("menuItemButton");
        confirmButton.setOnAction(this::editMenuItem);

        editPane2.getChildren().add(name);
        editPane2.getChildren().add(menuItemName);
        editPane2.getChildren().add(code);
        editPane2.getChildren().add(menuItemCode);
        editPane2.getChildren().add(category);
        editPane2.getChildren().add(menuItemCategory);
        editPane2.getChildren().add(price);
        editPane2.getChildren().add(menuItemPrice);


        try {
            buildCurrentMenuItemingredientsDisplay();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        editPane2.getChildren().add(confirmButton);
    }

    /**
     * buildCurrentMenuItemingredientDisplay sets the new container (Hbox) with all the functional buttons (add/remove)
     * @throws SQLException
     */
    public void buildCurrentMenuItemingredientsDisplay() throws SQLException {
        removeButtonMap = new HashMap<>();
        int count = 0;
        for (Ingredient ingredient : recipeIngredients.keySet()) {
            HBox container = new HBox();
            container.setMaxHeight(30);
            container.setMinHeight(30);
            container.setAlignment(Pos.CENTER_LEFT);
            if (count % 2 == 0) {
                container.setStyle("-fx-background-color: #e2e2e2");
            } else {
                container.setStyle("-fx-background-color: #f4f4f4");
            }

            Pane padding = new Pane();
            padding.setMinWidth(5);
            padding.setMaxWidth(5);
            padding.setPrefWidth(5);

            Label name = new Label(ingredient.getName());
            name.setMaxWidth(230);
            name.setMinWidth(230);

            Label quantity = new Label(Integer.toString(recipeIngredients.get(ingredient)));
            quantity.setMinWidth(50);


            Button removeButton = new Button("Remove");
            removeButtonMap.put(removeButton, ingredient);
            removeButton.setId("removeButton");
            removeButton.setOnAction(this::removeIngredient);
            removeButton.setMaxHeight(30);
            removeButton.setMinHeight(30);
            removeButton.setPrefHeight(30);

            Button addButton = new Button("Add");
            addButtonMap.put(addButton, ingredient);
            addButton.setId("addButton");
            addButton.setOnAction(this::addIngredient);
            addButton.setMaxHeight(30);
            addButton.setMinHeight(30);
            addButton.setPrefHeight(30);

            container.getChildren().addAll(padding, name, quantity,  removeButton, addButton);
            editPane2.getChildren().add(container);
            count += 1;
        }

    }

    /**
     * calls function removeIngredientFromList to remove an ingredient
     * @param e
     */
    public void removeIngredient(ActionEvent e){
        Button sourceButton = (Button) e.getSource();
        removeIngredientFromList(removeButtonMap.get(sourceButton));
        try {
            loadMenuItemData(currentMenuItem);
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * when increasing quantity this function is called. removing the ingredient then adding the ingredient with +1 quantity
     * @param e
     */
    public void addIngredient(ActionEvent e) {
        Button sourceButton = (Button) e.getSource();
        Ingredient ingredient = addButtonMap.get(sourceButton);
        int quantity = recipeIngredients.get(ingredient);
        recipeIngredients.remove(ingredient);
        recipeIngredients.put(ingredient, quantity + 1);
        try {
            loadMenuItemData(currentMenuItem);
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * decrementing an ingredient, by removing ingredient then returning the same ingredient with - 1 quantity
     * @param ingredient
     */
    public void removeIngredientFromList(Ingredient ingredient) {
        if (recipeIngredients.get(ingredient) != 0) {
            int quantity = recipeIngredients.get(ingredient);
            recipeIngredients.remove(ingredient);
            recipeIngredients.put(ingredient, quantity-1);
        }
    }

    /**
     * editMenuItem error checks user entry into the fields, also takes entry and sets the pane with the new information
     * @param event
     */
    public void editMenuItem(ActionEvent event) {
        itemFailsCheck = false;
        float f;
        float newPrice = 0;

        if ((((TextField) editPane2.getChildren().get(1)).getText()).isEmpty()) {
            menuItemErrorMessage.setText("Check Menu Item name is correct!");
            itemFailsCheck = true;
        }
        if ((((TextField) editPane2.getChildren().get(5)).getText()).isEmpty()) {
            menuItemErrorMessage.setText("Check Menu Item category is correct!");
            itemFailsCheck = true;
        }
        try {
            f = Float.valueOf(((TextField) editPane2.getChildren().get(7)).getText());
        } catch (NumberFormatException e) {
            menuItemErrorMessage.setText("Check Menu Item price");
            itemFailsCheck = true;
        }
        if (((((TextField) editPane2.getChildren().get(7)).getText()).isEmpty())) {
            menuItemErrorMessage.setText("Check Menu Item price");
            itemFailsCheck = true;
        }

        if (itemFailsCheck == false) {
            ArrayList<String> dietary = new ArrayList<>();
            try {
                dietary = menuItemManager.getMenuItemDietaryStatus(recipeIngredients);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            newPrice = Float.valueOf(((TextField) editPane2.getChildren().get(7)).getText());
            currentMenuItem.setName(((TextField) editPane2.getChildren().get(1)).getText());
            currentMenuItem.setCategory(((TextField) editPane2.getChildren().get(5)).getText());
            currentMenuItem.setPrice(newPrice);
            currentMenuItem.setGF((dietary.get(0).equals("true")));
            currentMenuItem.setVege((dietary.get(1).equals("true")));
            currentMenuItem.setVegan((dietary.get(2).equals("true")));




            try {
                menuItemManager.updateMenuItemIngredients(currentMenuItem, recipeIngredients);
                String menuItemCode = currentMenuItem.getCode();
                dataManager.calculateMenuItemServings(menuItemManager.getMenuItemByCode(menuItemCode));
                menuItemManager.updateMenuItem(currentMenuItem);
                menuItemManager.refreshMenuItems();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            populateMenuItemTable();
            editPane2.getChildren().clear();
            menuItemErrorMessage.setText("");
        }
    }


}
