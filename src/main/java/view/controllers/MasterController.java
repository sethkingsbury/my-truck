package view.controllers;

import com.j256.ormlite.support.ConnectionSource;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import managers.DataManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import managers.MenuItemManager;
import utils.Permissions;
import view.MainApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Master controller defines all the logic of scene swapping
 * that is used by all the controllers
 */
public class MasterController {

    private MenuItemManager menuItemManager = MenuItemManager.getMenuItemManager();
    private ConnectionSource c = MainApplication.connectionSource;

    /**
     * changing the scene
     * @param scenePath new scene path
     * @param event
     */
    public void changeScene(String scenePath, ActionEvent event) {
        Parent sampleScene = null;
        try {
            sampleScene = FXMLLoader.load(getClass().getResource(scenePath));
            Permissions permissions = Permissions.getPermissions();
            permissions.setScope((VBox)sampleScene.lookup("#navigationMenu"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        oldStage.setScene(new Scene(sampleScene));
    }

    /**
     * change to view data screen
     * @param event
     * @throws IOException
     */
    public void swapToViewDataScene(ActionEvent event) throws IOException {
        changeScene("/gui/scenes/viewData.fxml", event);
    }

    /**
     * change to change screen
     * @param event
     * @throws IOException
     */
    public void swapToViewChangeScene(ActionEvent event) throws IOException {
        changeScene("/gui/scenes/change.fxml", event);
    }

    /**
     * change to upload screen
     * @param event
     * @throws IOException
     */
    public void swapToUploadScene(ActionEvent event) throws IOException {
        changeScene("/gui/scenes/upload.fxml", event);
    }

    /**
     * change to order screen
     * @param event
     * @throws Exception
     */
    public void swapToOrderScene(ActionEvent event) throws Exception {
        changeScene("/gui/scenes/order.fxml", event);
    }

    /**
     * change to sales screen
     * @param event
     * @throws Exception
     */
    public void swapToSalesScene(ActionEvent event) throws Exception {
        menuItemManager.refreshMenuItems();
        changeScene("/gui/scenes/sales.fxml", event);
    }

    /**
     * change to settings screen
     * @param event
     * @throws Exception
     */
    public void swapToSettingsScene(ActionEvent event) throws Exception {
        changeScene("/gui/scenes/settings.fxml", event);
    }

    /**
     * change to login screen
     * @param event
     * @throws Exception
     */
    public void swapToLoginScene(ActionEvent event) throws Exception {
        changeScene("/gui/scenes/login.fxml", event);
    }

    /**
     * change to analytics screen
     * @param event
     * @throws Exception
     */
    public void swapToAnalyticsScene(ActionEvent event) throws Exception {
        changeScene("/gui/scenes/analytics.fxml", event);
    }


    public void openConfirm(ActionEvent event, String confirmString, Method method) throws IOException {
        Stage newStage = new Stage();
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/scenes/confirm.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        ConfirmController controller = fxmlLoader.<ConfirmController>getController();
        controller.setVars(confirmString, method, this);
        newStage.setTitle("Confirm");
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }
}
