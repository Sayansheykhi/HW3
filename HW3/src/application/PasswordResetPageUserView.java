package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class PasswordResetPageUserView {
    
    private final DatabaseHelper databaseHelper;
    private User user;
    
    
    public PasswordResetPageUserView(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User User) {
    	this.user = user;
        // Input field for the user's userName, password
        ArrayList<String> list = databaseHelper.getRequests();
        TextField userNameInput = new TextField();
        userNameInput.setPromptText("Enter Username");
        userNameInput.setMaxWidth(250);
        
        Label prompt = new Label();
        prompt.setStyle("-fx-font-size: 12px;");
        prompt.setText("Please enter your username");

        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorLabel.setText("Error: User does not exist");
        errorLabel.setVisible(false);
        

        
        

        Button continueButton = new Button("Continue");
        Button returnButton = new Button("Return to Login");
        
        returnButton.setOnAction(a -> {
            UserLoginPage userLoginPage =  new UserLoginPage(databaseHelper);
            userLoginPage.show(primaryStage, this.user);
            
        });
        
        continueButton.setOnAction(a -> {
            // Retrieve user inputs
            String userName = userNameInput.getText();
            String tempPassword = "";
                       
            

            
            
            if(databaseHelper.doesUserExist(userName)) {
                if(!databaseHelper.doesRequestExist(userName)) {
                errorLabel.setVisible(false);

                //adds request to database
                String oneTimePass = "";
                //Generates a string of 10 random lowercase letters
                for(int i = 0; i < 10; i++) {
                    oneTimePass += (char) ((int)(Math.random() * (122 - 97) + 97));
                }
                databaseHelper.createRequest(userName, oneTimePass);
                NewPasswordPage newPasswordPage =  new NewPasswordPage(databaseHelper);
                newPasswordPage.show(primaryStage, userName, oneTimePass, this.user);
                
                
                } else {
                    for(String item : list) {
                        if(item.contains(userName)) {
                            tempPassword = item;
                            tempPassword = tempPassword.substring(tempPassword.indexOf(",")+1);
                            tempPassword = tempPassword.substring(0, tempPassword.indexOf(","));
                        }
                    }
                    NewPasswordPage newPasswordPage =  new NewPasswordPage(databaseHelper);
                    newPasswordPage.show(primaryStage, userName, tempPassword, this.user);
                }
         
            } else {
                errorLabel.setText("Error: User does not exist");
                errorLabel.setVisible(true);
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(prompt, userNameInput, errorLabel, continueButton, returnButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Password Reset");
        primaryStage.show();
    }
    
    
}
