package view.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.ThemeManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Confirm Controller implements the logic behind confirm screens
 */
public class ConfirmController extends MasterController implements Initializable {

    ThemeManager themeManager = ThemeManager.getThemeManager();

    @FXML
    private Button confirmButton;

    @FXML
    private Button returnButton;

    @FXML
    private Label confirmLabel;

    @FXML
    private AnchorPane mainPane;

    private String confirmString;
    private Method method;
    private Object root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            themeManager.setToCurrentTheme(mainPane);
            confirmLabel.setText(confirmString);
        });
    }

    public void setVars(String confirmString, Method method, Object root) {
        this.confirmString = confirmString;
        this.method = method;
        this.root = root;
    }

    /**
     * Goes back to previous screen
     */
    public void goBack() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Confirms the selection
     */
    public void confirm() {
        goBack();
        try {
            method.invoke(root);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
