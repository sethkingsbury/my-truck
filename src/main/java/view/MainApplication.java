package view;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static view.Utility.*;

/**
 * The main function
 * mytruck starts from here :)
 */
public class MainApplication extends Application {

    private Stage primaryStage;

    private String configDir;

    public static ConnectionSource connectionSource;

    @Override
    public void start(Stage newPrimaryStage) throws Exception {

        DatabaseOperator dbOperator = new DatabaseOperator();
        connectionSource = dbOperator.establishConnection();

        // create directory to save all all the DTD files
        createUploadDirectoryForXMLFiles();

        // copy the files DTD files over to DTD directory
        copyDTDFilesToUploadDirectory();

        Parent initialScene = FXMLLoader.load(getClass().getResource("/gui/scenes/login.fxml"));
        primaryStage = newPrimaryStage;
        primaryStage.setTitle("mytruck");
        primaryStage.setScene(new Scene(initialScene));

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
