package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import managers.DataManager;
import managers.IngredientManager;
import models.Ingredient;
import view.ThemeManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Create Ingredient Controller implements logic that runs behind the Create/Edit Ingredient popup window
 */
public class CreateIngredientController implements Initializable {
    private DataManager dataManager = DataManager.getDataManager();
    private IngredientManager ingredientManager = IngredientManager.getIngredientManager();
    private ThemeManager themeManager = ThemeManager.getThemeManager();

    private boolean ingredientCheck;

    @FXML
    private Button cancelCreateButton;

    @FXML
    private TextField ingredientName;

    @FXML
    private TextField ingredientCode;

    @FXML
    private TextField ingredientMeasurement;

    @FXML
    private TextField ingredientAmount;

    @FXML
    private TextField ingredientPrice;

    @FXML
    private CheckBox ingredientGF;

    @FXML
    private CheckBox ingredientVege;

    @FXML
    private CheckBox ingredientVegan;

    @FXML
    private Text badName;

    @FXML
    private Text badCode;

    @FXML
    private Text badMeasure;

    @FXML
    private Text badAmount;

    @FXML
    private Text badPrice;

    @FXML
    private AnchorPane mainPane;

    @FXML
    void cancelCreate(ActionEvent event) {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    /**
     * creates ingredient, error checking
     * @param event
     * @throws SQLException
     */
    public void createIngredient(ActionEvent event) throws SQLException {
        ingredientCheck = false;
        badName.setVisible(false);
        badCode.setVisible(false);
        badMeasure.setVisible(false);
        badAmount.setVisible(false);
        badPrice.setVisible(false);

        if (ingredientName.getText().isEmpty()) {
            ingredientCheck = true;
            badName.setVisible(true);
        }
        if (ingredientCode.getText().isEmpty()) {
            ingredientCheck = true;
        }
        if (ingredientCode.getText().isEmpty()) {
            ingredientCheck = true;
            badCode.setVisible(true);
        }
        if (ingredientMeasurement.getText().isEmpty()) {
            ingredientCheck = true;
            badMeasure.setVisible(true);
        }

        try {
            if  (ingredientAmount.getText().isEmpty() || Integer.parseInt(ingredientAmount.getText()) < 0) {
                ingredientCheck = true;
                badAmount.setVisible(true);
            }
        } catch (NumberFormatException e) {
            ingredientCheck = true;
            badAmount.setVisible(true);
        }

        try {
            if  (ingredientPrice.getText().isEmpty() || Float.parseFloat(ingredientPrice.getText()) < 0) {
                ingredientCheck = true;
                badPrice.setVisible(true);
            }
        } catch (NumberFormatException e) {
            ingredientCheck = true;
            badPrice.setVisible(true);
        }

        if (!ingredientCheck) {
            int price = (int) (Float.parseFloat(ingredientPrice.getText()) * 100);
            ingredientManager.addIngredient(new Ingredient(
                    ingredientCode.getText(),
                    ingredientName.getText(),
                    ingredientMeasurement.getText(),
                    ingredientGF.isSelected(),
                    ingredientVege.isSelected(),
                    ingredientVegan.isSelected(),
                    Integer.parseInt(ingredientAmount.getText()),
                    price));
            ingredientName.clear();
            ingredientCode.clear();
            ingredientMeasurement.clear();
            ingredientAmount.clear();
            ingredientPrice.clear();
            ingredientGF.setSelected(false);
            ingredientVegan.setSelected(false);
            ingredientVege.setSelected(false);
            cancelCreate(event);
        }
        ingredientManager.refreshIngredients();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        themeManager.setToCurrentTheme(mainPane);
        if (ViewDataController.currentIngredient != null) {
            Ingredient editingIngredient = ViewDataController.currentIngredient;
            // we are editing an item
            ingredientName.setText(editingIngredient.getName());
            ingredientCode.setText(editingIngredient.getCode());
            ingredientCode.setEditable(false);
            ingredientMeasurement.setText(editingIngredient.getQuantityMeasuredIn());
            ingredientAmount.setText(String.valueOf(editingIngredient.getAmount()));
            ingredientPrice.setText(String.valueOf(editingIngredient.getPrice().getAsFloat()));
            ingredientGF.setSelected(editingIngredient.getGlutenFree());
            ingredientVege.setSelected(editingIngredient.getVegetarian());
            ingredientVegan.setSelected(editingIngredient.getVegan());
        }
    }
}
