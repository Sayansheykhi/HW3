package application;

import javafx.scene.Scene;
import databasePart1.DatabaseHelper;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Placeholder page for private messages between a student and others.
 */
 
public class StudentPrivateMessages {
    
	private final DatabaseHelper databaseHelper;
	private User user;

    public StudentPrivateMessages(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * Show a simple placeholder UI for private messages.
     */
    
    public void show(Stage primaryStage, User user) {
    	this.user = user;
        VBox layout = new VBox(10);
        Label header = new Label("Private Messages (Placeholder)");
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // query for messages specifically for this student.
        Label placeholderText = new Label("No messages to display yet. (Placeholder)");
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(a -> confirmLogout(primaryStage));

        layout.getChildren().addAll(header, placeholderText, logoutButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Private Messages");
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