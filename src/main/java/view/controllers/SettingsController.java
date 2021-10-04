package view.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import managers.UserManager;
import view.ThemeManager;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Settings Controller implements logic that runs behind the Setting screen in the program
 */
public class SettingsController extends MasterController implements Initializable {

    private ThemeManager themeManager = ThemeManager.getThemeManager();
    private UserManager userManager = UserManager.getUserManager();

    @FXML
    private GridPane mainPane;

    @FXML
    private ComboBox themeCombo;

    @FXML
    private Label profileNameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeManager.setToCurrentTheme(mainPane);

        ObservableList<String> themes = themeManager.getThemeNames();
        themeCombo.setItems(themes);
        themeCombo.setValue(themeManager.getCurrentTheme().getName());
        profileNameField.setText(userManager.getCurrentlyLoggedInUser().getUsername());
    }

    /**
     * change theme to selected theme
     */
    public void changeTheme() {
        themeManager.setTheme((String) themeCombo.getValue());
        themeManager.setToCurrentTheme(mainPane);
    }

    /**
     * logout button, go to login screen where user is prompted to login
     * @param event
     * @throws Exception
     */
    @FXML
    void logout(ActionEvent event) throws Exception {
        userManager.logout();
        swapToLoginScene(event);
    }
}
