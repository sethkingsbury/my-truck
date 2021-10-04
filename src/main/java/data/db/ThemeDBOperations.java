package data.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import view.components.Theme;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ThemeDBOperations provides functions to interact with
 * the THEME table
 */
public class ThemeDBOperations {
    /**
     * Connection to the database
     */
    private ConnectionSource connectionSource;

    /**
     * Interface that handles queries to the THEME table
     */
    private Dao<Theme, Integer> themeDao;

    /**
     * Default constructor for ThemeDBOperations
     * @param connectionSource connection to the database
     */
    public ThemeDBOperations(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
        try {
            themeDao = DaoManager.createDao(connectionSource, Theme.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a theme into the database
     * @param theme Theme in the database
     * @throws SQLException
     */
    public void insertTheme(Theme theme) throws SQLException {
        themeDao.create(theme);
    }

    /**
     * Updates a theme in the database
     * @param theme Updated theme with the same ID
     * @throws SQLException
     */
    public void updateTheme(Theme theme) throws SQLException {
        UpdateBuilder<Theme, Integer> updateBuilder = themeDao.updateBuilder();
        updateBuilder.updateColumnValue("name", theme.getName());
        updateBuilder.where().eq("id", theme.getId());
        updateBuilder.updateColumnValue("path", theme.getPath());
        updateBuilder.where().eq("id", theme.getId());
        updateBuilder.update();
    }

    /**
     * Returns all the themes in the database
     * @return List of all the themes in the DB
     * @throws SQLException
     */
    public ArrayList<Theme> getAllThemes() throws SQLException {
        ArrayList<Theme> themes = new ArrayList<Theme>(themeDao.queryForAll());
        return themes;
    }

    /**
     * Returns the currently active theme
     * @return Theme object that is currently active
     * @throws SQLException
     */
    public Theme getActiveTheme() throws SQLException {
        return themeDao.queryBuilder().where().eq("active", true).queryForFirst();
    }

    /**
     * Deletes a theme from the database
     * @param id ID of theme to be deleted
     * @throws SQLException
     */
    public void deleteTheme(Integer id) throws SQLException {
        themeDao.deleteById(id);
    }

    /**
     * Sets a theme to be active by its name
     * @param name Name of theme to be activated
     * @throws SQLException
     */
    public void setThemeByName(String name) throws SQLException{
        Theme currentTheme = getActiveTheme();
        Theme newTheme = themeDao.queryBuilder().where().eq("name", name).queryForFirst();

        UpdateBuilder<Theme, Integer> updateBuilder = themeDao.updateBuilder();
        updateBuilder.updateColumnValue("active", false);
        updateBuilder.where().eq("id", currentTheme.getId());
        updateBuilder.update();

        UpdateBuilder<Theme, Integer> updateBuilder2 = themeDao.updateBuilder();
        updateBuilder2.updateColumnValue("active", true);
        updateBuilder2.where().eq("id", newTheme.getId());
        updateBuilder2.update();
    }

    /**
     * Checks if a theme exists in the database
     * @param theme Theme object in query
     * @return True if exists, False otherwise
     * @throws SQLException
     */
    public boolean checkDuplicateTheme(Theme theme) throws SQLException {
        ArrayList<Theme> similarMenuItem = new ArrayList<>(themeDao.queryForEq("name", theme.getName()));
        return similarMenuItem.size() > 0;
    }
}

