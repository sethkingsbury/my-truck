package view.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import managers.DataManager;
import managers.IngredientManager;
import managers.MenuItemManager;
import models.Ingredient;
import models.MenuItem;
import models.Money;
import view.ThemeManager;
import view.Utility;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Create Menu Item Controller implements logic that runs behind the Create/Edit Menu Item pop window
 */
public class CreateMenuItemController implements Initializable {
    private DataManager dataManager = DataManager.getDataManager();
    private IngredientManager ingredientManager = IngredientManager.getIngredientManager();
    private MenuItemManager menuItemManager = MenuItemManager.getMenuItemManager();
    private ThemeManager themeManager = ThemeManager.getThemeManager();

    private MenuItem currentMenuItem;

    private boolean supplierCheck;
    private ObservableList<Ingredient> selectedIng;
    private Boolean itemFailsCheck;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<Ingredient> ingTable;

    @FXML
    private TableColumn<Ingredient, String> ingCode;

    @FXML
    private TableColumn<Ingredient, String> ingName;

    @FXML
    private TableColumn<Ingredient, Boolean> ingGF;

    @FXML
    private TableColumn<Ingredient, Boolean> ingVege;

    @FXML
    private TableColumn<Ingredient, Boolean> ingVegan;

    @FXML
    private TableView<Ingredient> ingTable1;

    @FXML
    private TableColumn<Ingredient, String> ingCode1;

    @FXML
    private TableColumn<Ingredient, String> ingName1;

    @FXML
    private TableColumn<Ingredient, Boolean> ingGF1;

    @FXML
    private TableColumn<Ingredient, Boolean> ingVege1;

    @FXML
    private TableColumn<Ingredient, Boolean> ingVegan1;

    @FXML
    private TableColumn<Ingredient, Integer> ingAmount1;

    @FXML
    private Button addIngButton;

    @FXML
    private Button removeIngButton;

    @FXML
    private Button clearIngButton;

    @FXML
    private TextField ingAmount;

    @FXML
    private Text ingAmountWarning;

    @FXML
    private TextField menuItemName;

    @FXML
    private TextField menuItemCode;

    @FXML
    private TextField menuItemCategory;

    @FXML
    private TextField menuItemPrice;

    @FXML
    private TextField itemImagePath;

    @FXML
    private ImageView imagePreview;

    @FXML
    private Text itemNameMissing;

    @FXML
    private Text itemCategoryMissing;

    @FXML
    private Text itemPriceMissing;

    @FXML
    private Text itemCodeMissing;

    @FXML
    private Text noIngselected;

    @FXML
    private Button cancelCreateButton;

    @FXML
    void cancelCreate(ActionEvent event) {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    /**
     * populate ingredient table
     */
    public void fillIngtable()  {
        ingTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ingCode.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("code"));
        ingName.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));
        ingGF.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("glutenFree"));
        ingVege.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("vegetarian"));
        ingVegan.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("vegan"));

        ingTable.setItems(ingredientManager.getIngredients());
    }

    /**
     * edit a selected ingredient
     * @param event
     */
    public void editIng(ActionEvent event) {
        int amount = 1;
        if (ingAmount.getText().isEmpty()) {
            ingAmountWarning.setVisible(true);
            return; // dont continue if Amount is empty
        }

        Ingredient selectedIngredient = ingTable1.getSelectionModel().getSelectedItem();

        try {
            amount = Integer.valueOf(ingAmount.getText());
            ingAmountWarning.setVisible(false);
            ingAmount.clear();

            selectedIng.remove(selectedIngredient);
            selectedIngredient.setAmount(amount); // set the new amount to ingredient
            selectedIng.add(selectedIngredient);

            ingTable1.setItems(selectedIng);

        } catch (NumberFormatException e) {
            ingAmountWarning.setVisible(true);
        }
    }

    /**
     * add a new ingredient
     * @param event
     */
    public void addIng(ActionEvent event) {
        int amount = 1;
        if (ingAmount.getText().isEmpty()) {
            ingAmountWarning.setVisible(true);
            return; // dont continue if Amount is empty
        }

        try {
            amount = Integer.valueOf(ingAmount.getText());

            if (amount > 0) {
                ingAmountWarning.setVisible(false);
                ingAmount.clear();

                Ingredient item = ingTable.getSelectionModel().getSelectedItem();
                item.setAmount(amount);

                if (!selectedIng.contains(item)) {
                    selectedIng.add(item);
                }

                ingTable1.setItems(selectedIng);
                noIngselected.setVisible(false);
            } else {
                ingAmountWarning.setVisible(true);
            }

        } catch (NumberFormatException e) {
            ingAmountWarning.setVisible(true);
        }
    }

    /**
     * remove an ingredient from table and database, refresh the table after.
     * @param event
     */
    public void removeIng(ActionEvent event) {
        selectedIng.remove(ingTable1.getSelectionModel().getSelectedItem());
        ingTable1.setItems(selectedIng);
    }

    /**
     * clear the ingredient
     * @param event
     */
    public void clearIng(ActionEvent event) {
        selectedIng.clear();
        ingTable1.setItems(selectedIng);
    }

    /**
     * initialize the table
     */
    public void initIngTable1() {
        ingTable1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ingCode1.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("code"));
        ingName1.setCellValueFactory(new PropertyValueFactory<Ingredient, String>("name"));
        ingGF1.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("glutenFree"));
        ingVege1.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("vegetarian"));
        ingVegan1.setCellValueFactory(new PropertyValueFactory<Ingredient, Boolean>("vegan"));
        ingAmount1.setCellValueFactory(new PropertyValueFactory<Ingredient, Integer>("amount"));
    }

    /**
     * try upload a picture from the file browser
     * @param event
     */
    public void openFileViewer(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = filechooser.showOpenDialog(stage);

        if (selectedFile != null) {
            itemImagePath.setText(selectedFile.getPath());
            imagePreview.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    /**
     * checking the filepath
     * @return directory and file
     */
    private String handleImageUpload() {
        Utility.createImageDirectory();
        final String uploadDir = Utility.getImageDirectory();
        String uploadedFile = "";

        FileInputStream fis = null;
        try {
            File fileToUpload = new File(itemImagePath.getText());
            fis = new FileInputStream(fileToUpload);
            FileOutputStream fos = new FileOutputStream(new File(uploadDir + fileToUpload.getName()));
            int c;
            while((c=fis.read())!=-1) {
                fos.write(c);
            }
            uploadedFile = fileToUpload.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadDir + uploadedFile;
    }

    /**
     * create the menu item, error handling
     * @param event
     * @throws SQLException
     */
    public void createMenuItem(ActionEvent event) throws SQLException {
        itemFailsCheck = false;
        itemNameMissing.setVisible(false);
        itemCodeMissing.setVisible(false);
        itemPriceMissing.setVisible(false);
        itemCategoryMissing.setVisible(false);
        noIngselected.setVisible(false);

        if (menuItemName.getText().isEmpty()) {
            itemNameMissing.setVisible(true);
            itemFailsCheck = true;
        }

        if (menuItemCode.getText().isEmpty()) {
            itemCodeMissing.setVisible(true);
            itemFailsCheck = true;
        }

        try {
            if (menuItemPrice.getText().isEmpty() || Float.valueOf(menuItemPrice.getText()) < 0) {
                itemPriceMissing.setVisible(true);
                itemFailsCheck = true;
            }
        } catch (NumberFormatException e) {
            itemPriceMissing.setVisible(true);
            itemFailsCheck = true;
        }

        if (menuItemCategory.getText().isEmpty()) {
            itemCategoryMissing.setVisible(true);
            itemFailsCheck = true;
        }

        if (selectedIng.size() < 1) {
            noIngselected.setVisible(true);
            itemFailsCheck = true;
        }

        if (itemFailsCheck == false) {
            models.MenuItem menuItem = new MenuItem(menuItemCode.getText(), menuItemName.getText(),  new Money(Float.valueOf(menuItemPrice.getText())), menuItemCategory.getText());

            HashMap<String, Integer> ingredientsByCode = new HashMap<>();
            for (Ingredient i : selectedIng) {
                ingredientsByCode.put(i.getCode(), i.getAmount());
            }

            String uploadedFile = "";
            if (!itemImagePath.getText().isEmpty()) {
                uploadedFile = handleImageUpload();
                menuItem.setImagePath(uploadedFile);
            }
            itemImagePath.setText(null);

            menuItem.setIngredientsByCode(ingredientsByCode);
            menuItemManager.addMenuItem(menuItem);
            menuItemManager.getMenuItemByCode(menuItem.getCode()).setServings(dataManager.calculateMenuItemServings(menuItem));

            selectedIng.clear();

            menuItemCode.clear();
            menuItemName.clear();
            menuItemCategory.clear();
            menuItemPrice.clear();
            itemImagePath.clear();

            menuItemManager.refreshMenuItems();
            cancelCreate(event);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        themeManager.setToCurrentTheme(mainPane);

        fillIngtable();
        initIngTable1();
        this.currentMenuItem = ViewDataController.currentMenuItem;
        selectedIng = FXCollections.observableArrayList();
        if (currentMenuItem != null) {
            HashMap<Ingredient, Integer> menuItemIngredients = new HashMap<>();

            try {
                menuItemIngredients = menuItemManager.getMenuItemIngredients(currentMenuItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (Ingredient i : menuItemIngredients.keySet()) {
                i.setAmount(menuItemIngredients.get(i));
                selectedIng.add(i);
            }

            menuItemName.setText(currentMenuItem.getName());
            menuItemCode.setText(currentMenuItem.getCode());
            menuItemCategory.setText(currentMenuItem.getCategory());
            String itemPrice = currentMenuItem.getPrice().getDollars() + "." + currentMenuItem.getPrice().getCents();
            menuItemPrice.setText(itemPrice);
            itemImagePath.setText(currentMenuItem.getImagePath());

            ingTable1.setItems(selectedIng);
        }

    }
}
