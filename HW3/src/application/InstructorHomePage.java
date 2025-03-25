package application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.*;

public class InstructorHomePage {
	
	private DatabaseHelper databaseHelper;
	private User user;
	
	public InstructorHomePage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

    public void show(Stage primaryStage, User user) {
    	this.user = user;
        // Create a layout container
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Create a welcome label
        Label welcomeLabel = new Label("Welcome Instructor");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create the Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            // Create a confirmation dialog asking if the user wants to save and logout
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Logout Confirmation");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Do you want to save and logout?");

            // Optionally customize button types
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmAlert.getButtonTypes().setAll(yesButton, noButton);

            // Show the confirmation dialog and process the response
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == yesButton) { // Safe to compare with '==' here since these are the same instances
                    // TODO: Insert any save logic here if needed before logout
                	UserLoginPage loginPage = new UserLoginPage(databaseHelper);
                	loginPage.show(primaryStage, this.user);
                }
                // If "No" is chosen, do nothing and return to the page
            });
        });

        // Add the label and button to the layout
        layout.getChildren().addAll(welcomeLabel, logoutButton);

        // Create the scene and assign it to the stage
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Instructor Home");
        primaryStage.show();
    }
}