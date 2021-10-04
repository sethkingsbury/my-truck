package view;

import com.j256.ormlite.support.ConnectionSource;
import data.db.DatabaseOperator;
import data.db.ThemeDBOperations;
import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import view.components.Theme;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Theme Manager provides support for accessing data throughout the program
 */
public class ThemeManager {

    private static final ThemeManager themeManager = new ThemeManager();
    private DatabaseOperator db = new DatabaseOperator();
    private ConnectionSource connectionSource = db.establishConnection();
    private ThemeDBOperations themeDBO = new ThemeDBOperations(connectionSource);

    private ObservableList themes = FXCollections.observableArrayList();

    public ThemeManager() {

        createDefaultThemes();
    }

    public void createDefaultThemes() {
        Theme redTheme = new Theme("Red Theme", "gui/stylesheets/redTheme.css");
        Theme darkTheme = new Theme("Dark Theme", "gui/stylesheets/darkTheme.css");

        redTheme.setActive(true);

        addTheme(redTheme);
        addTheme(darkTheme);
    }

    public void addTheme(Theme theme) {
        try {
            if (!(themeDBO.checkDuplicateTheme(theme))) {
                themeDBO.insertTheme(theme);
            }
        } catch (SQLException e) {
        }
    }

    public static ThemeManager getThemeManager() {
        return themeManager;
    }

    public void setTheme(String name) {
        try {
            themeDBO.setThemeByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Theme getCurrentTheme() {
        Theme theme = null;
        try {
            theme = themeDBO.getActiveTheme();
        } catch (SQLException e) {
        }
        return theme;
    }

    public void setToCurrentTheme(Pane pane) {
        try {
            pane.getStylesheets().clear();
            pane.getStylesheets().add(themeDBO.getActiveTheme().getPath());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Theme> getThemes() {
        try {
            themes.clear();
            themes.addAll(themeDBO.getAllThemes());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }

    public ObservableList<String> getThemeNames() {
        ObservableList<String> themeNames = FXCollections.observableArrayList();
        for (Theme theme : getThemes()) {
            themeNames.add(theme.getName());
        }
        return themeNames;
    }
}
