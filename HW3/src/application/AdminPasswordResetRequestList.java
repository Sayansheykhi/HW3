package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import databasePart1.*;

public class AdminPasswordResetRequestList {
	private final DatabaseHelper databaseHelper;
	private final User user;
	public AdminPasswordResetRequestList(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.user = user;
	}
	
	public void show(Stage primaryStage, User user) {
		//Get requests from database		
		ArrayList<String> requestUsers = databaseHelper.getRequests();
		
		
		
		
		//List of all requests
		ObservableList<String> requests = FXCollections.observableArrayList(requestUsers);
		ListView<String> requestList = new ListView<String>(requests);
		
		TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);
		
		//Text that shows pertinent information to user
		Label passwordResetText = new Label();
		passwordResetText.setStyle("-fx-font-size: 14px;");
		
		
		//Button resets password of given username
		Button resetPasswordButton = new Button("Reset Password");
		
		
		//Button that returns user to AdminHomePage
		Button backButton = new Button("Back");
		
		
		
		resetPasswordButton.setOnAction(b -> {
			String userName = userNameField.getText();
			if(databaseHelper.doesUserExist(userName)) {
				if(!databaseHelper.doesRequestExist(userName)) {
					String oneTimePass = "";
					//Generates a string of 10 random lowercase letters
					for(int i = 0; i < 10; i++) {
						oneTimePass += (char) ((int)(Math.random() * (122 - 97) + 97));
					}
					
					//adds request to database
					databaseHelper.createRequest(userName, oneTimePass);
					
					//adds username to list
					//requests.add(userName);
					
					passwordResetText.setText("User: " + userName + " has been given one time password: " + oneTimePass);
				}	
				else {
					passwordResetText.setText("Request Already Exists");
				}
					
			}
			else {
				passwordResetText.setText("User does not exist.");
			}
			
			

			
		});
		
		
		//Returns user to AdminHomePage
		backButton.setOnAction(a -> {
			new AdminHomePage(databaseHelper, user).show(primaryStage, this.user);
		});
		
		
		
		
		
		
		
		//Constructs window
		VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(requestList, userNameField, passwordResetText, resetPasswordButton, backButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Reset Requests");
        primaryStage.show();
	}
}
