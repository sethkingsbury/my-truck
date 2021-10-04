package view.components;

import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import managers.DataManager;
import models.MenuItem;
import view.controllers.SalesController;
import view.interfaces.Hoverable;
import view.interfaces.Selectable;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Custom GridPane for displaying an item with image and names
 */
public class MenuItemPane extends Pane implements Selectable, Hoverable {

    private SalesController manager;
    private MenuItem item;
    private Rectangle clip;
    private Rectangle clipFocused;
    Label itemServings;

    public MenuItemPane(SalesController manager, MenuItem tempItem) {

        this.manager = manager;
        item = tempItem;

        this.setMaxWidth(120);
        this.setMinWidth(120);
        this.setMaxHeight(120);
        this.setMinHeight(120);
        this.getStyleClass().add("menuItemPane");

        ImageView imageView;
        if (item.getImagePath().equals("images/itemPlaceholder.png")) {
            imageView = new ImageView(item.getImagePath());
        } else {
            InputStream input = null;
            try {
                input = new FileInputStream(item.getImagePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Image image = new Image(input);
            imageView = new ImageView(image);
        }
        imageView.setFitHeight(100);
        imageView.setFitWidth(120);

        Label itemName = new Label(item.getName());
        itemName.getStyleClass().add("label3");
        itemName.setMaxWidth(110);
        itemName.setMinWidth(110);
        itemName.setPrefWidth(110);

        itemServings = new Label(Integer.toString(item.getServings()));
        itemServings.getStyleClass().add("label2");

        Label itemPrice = new Label(item.getPrice().toString());
        itemPrice.getStyleClass().add("label2");

        clip = new Rectangle(120, 120);
        clip.setArcWidth(20);
        clip.setArcHeight(20);

        clipFocused = new Rectangle(120, 120);
        clipFocused.setArcWidth(10);
        clipFocused.setArcHeight(10);

        this.getChildren().addAll(imageView, itemName, itemServings, itemPrice);

        imageView.setX(0);
        imageView.setY(0);
        itemName.setLayoutX(5);
        itemName.setLayoutY(5);
        itemServings.setLayoutX(5);
        itemServings.setLayoutY(100);
        itemPrice.setLayoutX(60);
        itemPrice.setLayoutY(100);

        this.setClip(clip);

        GridPane.setHalignment(itemName, HPos.CENTER);
    }

    @Override
    public boolean requestSelection(boolean select) {
        return true;
    }

    @Override
    public void notifySelection(boolean select) {
        item.setServings(item.getServings() - 1);
        itemServings.setText(Integer.toString(item.getServings()));
        manager.addMenuItem(item);
        itemServings.setText(Integer.toString(item.getServings()));
        if (item.getServings() == 0) {
            this.setDisable(true);
        }
    }

    @Override
    public void notifyMouseEntry(boolean select) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), this);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.play();
        this.setClip(clipFocused);
    }

    @Override
    public void notifyMouseExit(boolean select) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), this);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
        this.setClip(clip);
    }

    public void setItemServings(int servings) {
        item.setServings(servings);
        itemServings.setText(Integer.toString(servings));
    }

    public int getItemServings() {return Integer.parseInt(itemServings.getText());}
}
