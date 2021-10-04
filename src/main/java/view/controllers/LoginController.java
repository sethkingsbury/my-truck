package view.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import managers.UserManager;

import javafx.event.ActionEvent;
import view.ThemeManager;
import view.constants.Responses;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Login Controller implements logic that runs behind the Login screen in the program
 */
public class LoginController extends MasterController implements Initializable {
    /**
     * Gets data manager to access database data
     */
    private UserManager userManager = UserManager.getUserManager();
    private ThemeManager themeManager = ThemeManager.getThemeManager();
    private String formType;

    @FXML
    private Label headerLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private ImageView loadingIcon;

    @FXML
    private GridPane mainPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        themeManager.setToCurrentTheme(mainPane);

        statusLabel.setVisible(false);

        // If there aren't any users that can create/delete accounts, show the initial owner creation screen.
        if (userManager.getAllOwners().isEmpty()) {
            firstTimeSetup();
        } else {
            regularLogin();
        }
    }

    /**
     * if there are no registered users, the program prompts to setup an initial admin/owner role
     */
    private void firstTimeSetup() {
        formType = "Register";
        registerButton.setVisible(true);
        loginButton.setVisible(false);
        headerLabel.setText(Responses.LOGIN_FIRST_TIME_HEADER);
    }

    /**
     * assuming there is already users in the tables, attempt to login
     */
    private void regularLogin() {
        formType = "Login";
        registerButton.setVisible(false);
        loginButton.setVisible(true);
        headerLabel.setText(Responses.LOGIN_REGULAR_HEADER);
    }

    /**
     * upon login, swap to sales screen into the program
     * @param event
     * @throws Exception
     */
    @FXML
    void loginAction(ActionEvent event) throws Exception  {
        statusLabel.setVisible(false);

        if (validateForm()) {
            if (attemptLogin()) {
                swapToSalesScene(event);
            }
        }
    }

    /**
     * first time login event. so creates account then attempts to login
     * @param event
     * @throws Exception
     */
    @FXML
    void registerAction(ActionEvent event) throws Exception {
        statusLabel.setVisible(false);

        if (validateForm()) {
            userManager.createNewUser(usernameField.getText(), passwordField.getText(), 3);
            if (attemptLogin()) {
                swapToSalesScene(event);
            }
        }
    }

    /**
     * hitting enter key attempts to login
     * @param event
     * @throws Exception
     */
    @FXML
    public void onEnter(ActionEvent event) throws Exception {
        if (formType.equals("Login")) {
            loginAction(event);
        } else {
            registerAction(event);
        }
    }

    /**
     * checks if the username/password fields are filled
     * @return
     */
    private boolean validateForm() {
        String username = usernameField.getText();
        String plaintext = passwordField.getText();
        if (username.isEmpty() || plaintext.isEmpty() || username.trim().length() < 1 || plaintext.trim().length() < 1) {
            statusLabel.setText(Responses.LOGIN_MALFORMED_REQUEST);
            statusLabel.setVisible(true);
            return false;
        }
        return true;
    }

    /**
     * checks if the login works. uses authenticate() to check. if it passes we return true
     * if the details do not check out it returns false
     * @return
     */
    private boolean attemptLogin() {
        statusLabel.setVisible(false);
        loadingIcon.setVisible(true);

        if (userManager.authenticate(usernameField.getText(), passwordField.getText())) {
            loadingIcon.setVisible(false);
            return true;

        } else {
            loadingIcon.setVisible(false);
            statusLabel.setVisible(true);
            statusLabel.setText(Responses.LOGIN_INVALID_CREDENTIALS);
            return false;
        }
    }
}
