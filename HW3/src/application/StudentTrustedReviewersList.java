package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

/**
 * This class is a placeholder page for the list of "trusted reviewers" that a student maintains.
 * 
**/

public class StudentTrustedReviewersList {
    
	private final DatabaseHelper databaseHelper;
	private User user;

    public StudentTrustedReviewersList(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * Show a (very) basic UI listing trusted reviewers.
     */
    public void show(Stage primaryStage, User user) {
    	this.user = user;
        // Simple placeholder UI
        VBox layout = new VBox(10);
        Label header = new Label("Trusted Reviewers Page (Placeholder)");
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // retrieve a list of reviewers from your DB or an API
        // and display them in a ListView or TableView. For now, just a placeholder.
        Label placeholderText = new Label("No reviewers to display yet. (Placeholder)");
        
        Button logoutButton = new Button("Logout");
	    logoutButton.setOnAction(a -> confirmLogout(primaryStage));

        layout.getChildren().addAll(header, placeholderText, logoutButton);
       
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trusted Reviewers");
        primaryStage.show();
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