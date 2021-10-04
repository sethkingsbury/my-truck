package view.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import managers.UserManager;
import models.User;
import view.ThemeManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Create User Controller implements logic that runs behind the Create/Edit User pop window
 */
public class CreateUserController implements Initializable {

    private UserManager userManager = UserManager.getUserManager();
    private ThemeManager themeManager = ThemeManager.getThemeManager();

    private User currentlyEditingUser;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField userUsername;

    @FXML
    private TextField userPermissions;

    @FXML
    private TextField userPassword;

    @FXML
    private Text badUsername;

    @FXML
    private Text badPermissions;

    @FXML
    private Text badPassword;

    @FXML
    private Button cancelCreateButton;

    @FXML
    private Text popupTitle;

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
     * creating a user, with error handling
     * @param event
     * @throws SQLException
     */
    public void createUser(ActionEvent event) throws SQLException {
        boolean userFormHasErrors = false;
        badUsername.setVisible(false);
        badPassword.setVisible(false);
        badPermissions.setVisible(false);

        List<String> userUsernames = userManager.getAllUserUsernames();

        if (userUsername.getText().isEmpty() || userUsername.getText().trim().length() < 1) {
            badUsername.setVisible(true);
            userFormHasErrors = true;
        }

        if (userUsernames.contains(userUsername.getText()) && currentlyEditingUser == null) {
            badUsername.setVisible(true);
            userFormHasErrors = true;
        }

        try {
            Integer.parseInt(userPermissions.getText());
            if (userPermissions.getText().isEmpty() || userPermissions.getText().trim().length() < 1 || Integer.parseInt(userPermissions.getText()) > 3 || Integer.parseInt(userPermissions.getText()) < 0) {
                badPermissions.setVisible(true);
                userFormHasErrors = true;
            }
        } catch(NumberFormatException e){
            badPermissions.setVisible(true);
            userFormHasErrors = true;
        }

        if ((userPassword.getText().isEmpty() || userPassword.getText().trim().length() < 1) && currentlyEditingUser == null) {
            badPassword.setVisible(true);
            userFormHasErrors = true;
        }

        if (!userFormHasErrors) {
            int userId = -1;
            if (currentlyEditingUser != null) {
                userId = currentlyEditingUser.getId();
            }

            User user = new User(
                userId,
                userUsername.getText(),
                userPassword.getText(),
                Integer.parseInt(userPermissions.getText())
            );

            if ((userPassword.getText().isEmpty() || userPassword.getText().trim().length() < 1) && currentlyEditingUser != null) {
                user.setPasswordHash(currentlyEditingUser.getPassword());
            }

            userManager.createOrUpdateNewUser(user);

            userUsername.clear();
            userPassword.clear();
            userPermissions.clear();
            userManager.refreshUsers();
            cancelCreate(event);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popupTitle.setText("Create User");
        themeManager.setToCurrentTheme(mainPane);

        this.currentlyEditingUser = ViewDataController.currentlyEditingUser;
        if (currentlyEditingUser != null) {
            popupTitle.setText("Edit User");
            userUsername.setText(currentlyEditingUser.getUsername());
            userPermissions.setText(Integer.toString(currentlyEditingUser.getAccountType()));
        }
    }
}
