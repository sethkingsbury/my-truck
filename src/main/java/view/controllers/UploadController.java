package view.controllers;

import data.xml.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import managers.DataManager;
import managers.IngredientManager;
import managers.MenuItemManager;
import managers.UserManager;
import models.Ingredient;
import models.MenuItem;
import models.Supplier;
import models.User;
import view.ThemeManager;
import view.Utility;
import view.constants.Responses;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static view.Utility.configDir;
import static view.Utility.copyOneFileToDirectory;

/**
 * Upload Controller implements logic that runs behind the Upload screen in the program
 */
public class UploadController extends MasterController {

    @FXML
    private TextField filePathText;

    @FXML
    private Text statusText;

    private DataManager dataManager = DataManager.getDataManager();
    private IngredientManager ingredientManager = IngredientManager.getIngredientManager();
    private MenuItemManager menuItemManager = MenuItemManager.getMenuItemManager();
    private UserManager userManager = UserManager.getUserManager();

    private ThemeManager themeManager = ThemeManager.getThemeManager();

    @FXML
    private GridPane mainPane;

    @FXML
    private void initialize() {
        themeManager.setToCurrentTheme(mainPane);
        statusText.setVisible(false);
    }

    /**
     * uploadFile detects what 'type' of xml file is uploaded using getFileType(), it then runs the corresponding XML
     * parser in order to add the data from the xml file to the database using a switch/case statement.
     * This will only work if the correct filetype is added, so only specific types of XML's will work, preventing
     * the user uploading an incorrect document
     *
     * @param event
     */
    public void uploadFile(ActionEvent event) {
        if (!(filePathText.getText().isEmpty())) {
            String originalFilePath = filePathText.getText();
            String copiedFilePath = "";
            try {
                copiedFilePath = copyOneFileToDirectory(originalFilePath, configDir, false);
            } catch (IOException e) {
                statusText.setText(Responses.FILE_UPLOAD_BAD_TYPE);
                statusText.setVisible(true);
                return;
            }

            XMLReader xml = new XMLReader(copiedFilePath);
            if (!xml.getDocumentIsParseable()) {
                statusText.setText(Responses.FILE_UPLOAD_BAD_TYPE);
                statusText.setVisible(true);
                return;
            }

            String xmlFileType = xml.getFileType();
            ArrayList<Ingredient> ingredients;
            ArrayList<MenuItem> menuItems;
            ArrayList<Supplier> supplier;
            ArrayList<User> users;

            switch (xmlFileType) {
                case "ingredients":
                    ingredients = new IngredientXMLReader(copiedFilePath).parseXML();
                    ingredientManager.addIngredients(ingredients);
                    break;
                case "menus":
                    menuItems = new MenuItemXMLReader(copiedFilePath).parseXML();
                    menuItemManager.addMenuItems(menuItems);
                    break;
                case "suppliers":
                    supplier = new SupplierXMLReader(copiedFilePath).parseXML();
                    dataManager.addSuppliers(supplier);
                    break;
                case "users":
                    users = new UserXMLReader(copiedFilePath).parseXML();
                    userManager.createNewUsers(users);
                    break;
            }
            filePathText.clear();
            statusText.setText(Responses.FILE_UPLOAD_SUCCESS);
            statusText.setVisible(true);
        } else {
            statusText.setText(Responses.FILE_UPLOAD_BAD_TYPE);
            statusText.setVisible(true);
        }
    }

    public void chooseFileAction(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = filechooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filePathText.setText(selectedFile.getPath());
        }
        statusText.setVisible(false);
    }

    /**
     * exporting datam uses the writers on the correct xml
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    public void exportAllData() throws SQLException, IOException {
        CashFloatXMLWriter cashFloatWriter = new CashFloatXMLWriter(Utility.XMLExportDir, "cashfloat.xml");
        cashFloatWriter.writeToXML(dataManager.getAllDenom());

        IngredientXMLWriter ingWriter = new IngredientXMLWriter(Utility.XMLExportDir, "ingredients.xml");
        ingWriter.writeToXML(ingredientManager.getAllIngredients());

        MenuItemXMLWriter menuItemWriter = new MenuItemXMLWriter(Utility.XMLExportDir, "menuitems.xml");
        menuItemWriter.writeToXML(menuItemManager.getAllMenuItems());

        statusText.setText(Responses.FILE_DOWNLOAD_SUCCESS);
        statusText.setVisible(true);
    }

}
