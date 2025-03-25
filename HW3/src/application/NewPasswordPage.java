package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.util.ArrayList;


import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class NewPasswordPage {
	
    private final DatabaseHelper databaseHelper;
    private User user;
    
    public NewPasswordPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, String username, String oneTimeCode, User user) {
    	this.user = user;
    	// Input field for the user's userName, password
    	ArrayList<String> list = databaseHelper.getRequests();
        PasswordField tempPassInput = new PasswordField();
        tempPassInput.setPromptText("Enter Temporary Password");
        tempPassInput.setMaxWidth(250);

        PasswordField newPass = new PasswordField();
        newPass.setPromptText("Enter New Password");
        newPass.setMaxWidth(250);
        
        PasswordField verPass = new PasswordField();
        verPass.setPromptText("Verify Password");
        verPass.setMaxWidth(250);
        
        // Label to display error messages for verification
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setText("Error: Password and Verification Password don't match.");
        
        // Label to display error message for temp password
        Label tempError = new Label();
        tempError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        tempError.setText("Error: Temporary password is incorrect");
        
        Label oneTimePassMessage = new Label();
        oneTimePassMessage.setStyle("fx-font-size: 12px;");
        oneTimePassMessage.setText("Your one-time-password is " + oneTimeCode);
        
        // Text for error message for invalid password 
        Text errorPassword = new Text();
        errorPassword.setFill(Color.RED);
        errorPassword.setVisible(false);
    	errorLabel.setVisible(false);
    	tempError.setVisible(false);
        
    	
    	// Displays error message
        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.setStyle("-fx-padding: 0; -fx-margin: 0;");
        textFlow.setLineSpacing(5);
        textFlow.setMaxHeight(400); // Limit how much height it takes
        textFlow.setMaxWidth(800);
        
        
        
        // Button to confirm password reset
        Button resetButton = new Button("Reset Password");
        
        // Button to go back a page
        Button backButton = new Button("Back");
        
        // Goes back a page if pressed
        backButton.setOnAction(a -> {
        	PasswordResetPageUserView passwordResetPage =  new PasswordResetPageUserView(databaseHelper);
        	passwordResetPage.show(primaryStage, this.user);
        	
        });
        
        
        // Resets password if pressed
        resetButton.setOnAction(a -> {
        	// Retrieve user inputs
            String tempPasswordInput = tempPassInput.getText();
            String newPassword = newPass.getText();
            String verPassword = verPass.getText();
            
            // Searches for temp password in the database


            // Evaluates and checks if the password is valid
            String passwordValidity = PasswordEvaluator.evaluatePassword(newPassword);
            errorPassword.setText(passwordValidity);            
            
            // Checks if new password and temp password is valid            
            if(passwordValidity.equals("") && tempPasswordInput.equals(oneTimeCode)) {
            	
            	tempError.setVisible(false);
            	
            	// Checks if new password and verification password match
            	if(verPassword.equals(newPassword)) {
            		
            		errorLabel.setVisible(false);
            		
					databaseHelper.setUserPassword(username, newPassword, tempPasswordInput); // Sets password to new password
					
					databaseHelper.deleteRequest(username); //Deletes request from the database
					
			        UserLoginPage userLoginPage =  new UserLoginPage(databaseHelper);
			        
			        userLoginPage.show(primaryStage, this.user);
		            	
            	} else {
            		errorLabel.setVisible(true);
            	}
            } else {
            	
            	if(!tempPasswordInput.equals(oneTimeCode)) 
            		
            		tempError.setVisible(true);
            	
            	else 
            		
            		tempError.setVisible(false);
            	
            	
            	if(!passwordValidity.equals("")) {
            		
	            	errorLabel.setVisible(false);
	            	
	            	errorPassword.setVisible(true);
	            	
	                textFlow.getChildren().add(errorPassword);
	                
            	}
           }

        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(oneTimePassMessage, tempPassInput, newPass, verPass, tempError, textFlow, errorLabel, resetButton, backButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Password Reset");
        primaryStage.show();
    }
    
    
}
