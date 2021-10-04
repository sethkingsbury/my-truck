package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import managers.DataManager;
import models.CashFloat;
import models.Ingredient;
import view.ThemeManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Create Cash Float Controller implements logic that runs behind the Create/Edit Cash Float popup window
 */
public class CreateCashFloatController implements Initializable {

    private DataManager dataManager = DataManager.getDataManager();
    private ThemeManager themeManager = ThemeManager.getThemeManager();

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button cancelCreateButton;

    @FXML
    private TextField coin10;

    @FXML
    private TextField coin20;

    @FXML
    private TextField coin50;

    @FXML
    private TextField coin1;

    @FXML
    private TextField coin2;

    @FXML
    private TextField note5;

    @FXML
    private TextField note10;

    @FXML
    private TextField note20;

    @FXML
    private TextField note50;

    @FXML
    private TextField note100;

    @FXML
    void cancelCreate(ActionEvent event) {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        themeManager.setToCurrentTheme(mainPane);
        try {
            ArrayList<CashFloat> floats = dataManager.getAllDenom();
            coin10.setText(String.valueOf(floats.get(0).getQuantity()));
            coin20.setText(String.valueOf(floats.get(1).getQuantity()));
            coin50.setText(String.valueOf(floats.get(2).getQuantity()));
            coin1.setText(String.valueOf(floats.get(3).getQuantity()));
            coin2.setText(String.valueOf(floats.get(4).getQuantity()));
            note5.setText(String.valueOf(floats.get(5).getQuantity()));
            note10.setText(String.valueOf(floats.get(6).getQuantity()));
            note20.setText(String.valueOf(floats.get(7).getQuantity()));
            note50.setText(String.valueOf(floats.get(8).getQuantity()));
            note100.setText(String.valueOf(floats.get(9).getQuantity()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the virtual cashfloat
     * @param event
     */
    @FXML
    private void setFloat(ActionEvent event) {
        try {
            ArrayList<CashFloat> floats = dataManager.getAllDenom();
            dataManager.setCashFloat(floats.get(0), Integer.parseInt(coin10.getText()));
            dataManager.setCashFloat(floats.get(1), Integer.parseInt(coin20.getText()));
            dataManager.setCashFloat(floats.get(2), Integer.parseInt(coin50.getText()));
            dataManager.setCashFloat(floats.get(3), Integer.parseInt(coin1.getText()));
            dataManager.setCashFloat(floats.get(4), Integer.parseInt(coin2.getText()));
            dataManager.setCashFloat(floats.get(5), Integer.parseInt(note5.getText()));
            dataManager.setCashFloat(floats.get(6), Integer.parseInt(note10.getText()));
            dataManager.setCashFloat(floats.get(7), Integer.parseInt(note20.getText()));
            dataManager.setCashFloat(floats.get(8), Integer.parseInt(note50.getText()));
            dataManager.setCashFloat(floats.get(9), Integer.parseInt(note100.getText()));
            dataManager.refreshCashFloat();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cancelCreate(event);
    }
}
