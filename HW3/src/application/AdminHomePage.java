package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import databasePart1.DatabaseHelper;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	private DatabaseHelper databaseHelper;
	private User user;
	
	public AdminHomePage(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.user = user;
	}
	
    public void show(Stage primaryStage, User user) {
    	VBox layout = new VBox(20);
    	this.user = user;
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
		adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		
		// buttons for main functions
		Button inviteButton = new Button("Invite Users");
		Button manageUsersButton = new Button("Manage Users");
		Button passwordResetRequestButton = new Button("Password Reset Requests");
		Button logoutButton = new Button("Logout");
		String buttonStyle = "-fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 250px;";
		inviteButton.setStyle(buttonStyle);
		manageUsersButton.setStyle(buttonStyle);
		passwordResetRequestButton.setStyle(buttonStyle);
		logoutButton.setStyle(buttonStyle);
		
		// handles redirecting..
		inviteButton.setOnAction(e -> openInvitePage(primaryStage));
		manageUsersButton.setOnAction(e -> openUserListPage(primaryStage));
		passwordResetRequestButton.setOnAction(e -> openPasswordResetPage(primaryStage));
		logoutButton.setOnAction(e -> confirmLogout(primaryStage));
		
		// Set the scene to primary stage
	    layout.getChildren().addAll(adminLabel, inviteButton, manageUsersButton, passwordResetRequestButton, logoutButton);
	    Scene adminScene = new Scene(layout, 800, 400);
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Homepage");
    }
    
    // Redirects to AdminInvitationPage.java
    
    private void openInvitePage(Stage primaryStage) {
    	AdminInvitationPage invitePage = new AdminInvitationPage(databaseHelper, user);
    	invitePage.show(primaryStage, this.user);
    }
    
    // Redirects to AdminUserList.java
    private void openUserListPage(Stage primaryStage) {
    	AdminUserList userListPage = new AdminUserList(databaseHelper, user);
    	userListPage.show(primaryStage, this.user);
    }
    
    // Redirects to AdminPasswordResetRequestList.java
    private void openPasswordResetPage(Stage primaryStage) {
    	AdminPasswordResetRequestList resetPage = new AdminPasswordResetRequestList(databaseHelper, user);
    	resetPage.show(primaryStage, this.user);
    }
    
    // Redirects to UserLoginPage.java and display confirmation
    private void confirmLogout(Stage primaryStage) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
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