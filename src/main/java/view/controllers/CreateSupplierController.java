package view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import managers.DataManager;
import models.Supplier;
import view.ThemeManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Create Supplier Controller implements logic that runs behind the Create/Edit Supplier pop window
 */
public class CreateSupplierController implements Initializable {

    private DataManager dataManager = DataManager.getDataManager();
    private ThemeManager themeManager = ThemeManager.getThemeManager();

    private boolean supplierCheck;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField supplierName;

    @FXML
    private TextField supplierContact;

    @FXML
    private Text badSupName;

    @FXML
    private Text badSupContact;

    @FXML
    private Button cancelCreateButton;

    private Supplier currentlyEditSupplier;

    /**
     * revert an accidental create
     * @param event
     */
    @FXML
    void cancelCreate(ActionEvent event) {
        Stage stage = (Stage) cancelCreateButton.getScene().getWindow();
        stage.close();
    }

    /**
     * creating supplier, with error handling
     * @param event
     * @throws SQLException
     */
    public void createSupplier(ActionEvent event) throws SQLException {
        supplierCheck = false;
        badSupName.setVisible(false);
        badSupContact.setVisible(false);
        ArrayList<String> supNames = dataManager.getAllSupplierNames();

        if (supplierName.getText().isEmpty() || supNames.contains(supplierName.getText())) {
            supplierCheck = true;
            badSupName.setVisible(true);
        }

        try {
            int i = Integer.valueOf(supplierContact.getText());
        } catch (NumberFormatException e) {
            supplierCheck = true;
            badSupContact.setVisible(true);
        }

        if ((supplierContact.getText().isEmpty())) {
            supplierCheck = true;
            badSupContact.setVisible(true);
        }

        if (!supplierCheck) {
            Supplier newSupplier = new Supplier(
                    supplierName.getText(),
                    supplierContact.getText());

            if (currentlyEditSupplier != null) {
                newSupplier.setId(currentlyEditSupplier.getId());
            }
            dataManager.addSupplier(newSupplier);
            supplierContact.clear();
            supplierName.clear();
            cancelCreate(event);
        }
        dataManager.refreshSuppliers();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        themeManager.setToCurrentTheme(mainPane);
        this.currentlyEditSupplier = ViewDataController.currentEditingSupplier;
        if (currentlyEditSupplier != null) {
            supplierName.setText(currentlyEditSupplier.getName());
            supplierContact.setText(currentlyEditSupplier.getContact());
        }
    }
}
