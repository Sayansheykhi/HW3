package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class StaffHomePage {

	private final DatabaseHelper databaseHelper;
	private User user;

    public StaffHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    public void show(Stage primaryStage, User user) {
    	this.user = user;
    	VBox layout = new VBox(10);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello student
	    Label staffLabel = new Label("Welcome, Staff!");
	    staffLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    Button logoutButton = new Button("Logout");
	    logoutButton.setOnAction(a -> confirmLogout(primaryStage));
	    layout.getChildren().addAll(staffLabel, logoutButton);
	    Scene staffScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(staffScene);
	    primaryStage.setTitle("Staff Page");
    }
    
    private void confirmLogout(Stage primaryStage) {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Please confirm if you're logging out.");
        alert.setContentText("Make sure all changes are saved.");

        ButtonType saveAndLogout = new ButtonType("Save and Logout");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveAndLogout, cancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == saveAndLogout) {
                logout(primaryStage);
            }
        });
    }
    
    private void logout(Stage primaryStage) {
    	UserLoginPage loginPage = new UserLoginPage(databaseHelper);
    	loginPage.show(primaryStage, this.user);
    }
}