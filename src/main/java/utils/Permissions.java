package utils;

import javafx.scene.layout.VBox;
import managers.UserManager;

public class Permissions {

    private static final Permissions permissions = new Permissions();

    private UserManager userManager = UserManager.getUserManager();

    public void setScope(VBox navigationMenu) {

        if (navigationMenu == null) { return; }

        // General functionality that all users have
        navigationMenu.lookup("#salesMenuButton").setVisible(true);         // Sales screen button
        navigationMenu.lookup("#orderMenuButton").setVisible(true);         // Orders screen button
        navigationMenu.lookup("#settingsMenuButton").setVisible(true);      // Settings screen button
        navigationMenu.lookup("#viewMenuButton").setVisible(false);         // View screen button
        navigationMenu.lookup("#uploadMenuButton").setVisible(false);       // Upload screen button
        navigationMenu.lookup("#analyticsMenuButton").setVisible(false);    // Analytics screen button

        // Profile specific functionality
        switch (userManager.getCurrentlyLoggedInUser().getAccountType()) {
            case 1: // Analyst
                navigationMenu.lookup("#analyticsMenuButton").setVisible(true);     // Analytics screen button
                break;

            case 2: // Manager
            case 3: // Owner
                navigationMenu.lookup("#viewMenuButton").setVisible(true);          // View screen button
                navigationMenu.lookup("#uploadMenuButton").setVisible(true);        // Upload screen button
                navigationMenu.lookup("#analyticsMenuButton").setVisible(true);     // Analytics screen button
                break;
        }
    }

    public static Permissions getPermissions() { return permissions; }
}
