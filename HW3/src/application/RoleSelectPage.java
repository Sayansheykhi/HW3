package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;


public class RoleSelectPage {
	
	private final DatabaseHelper databaseHelper;
	private User user;
	public RoleSelectPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	
    public void show(Stage primaryStage, User user) {
    	VBox layout = new VBox(5);
    	this.user = user;

    	// Label to display the welcome message for the first user
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #666666");
	    Label userLabel = new Label("Please select your role");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Creates the role buttons
	    Button adminRoleButton = new Button("Admin");
	    Button staffRoleButton = new Button("Staff");
	    Button reviewerRoleButton = new Button("Reviewer");
	    Button instructorRoleButton = new Button("Instructor");
	    Button studentRoleButton = new Button("Student");
	    
	    // Positions the role buttons
	    adminRoleButton.setTranslateX(-126);
	    adminRoleButton.setTranslateY(0);
	    studentRoleButton.setTranslateX(-66);
	    studentRoleButton.setTranslateY(-30);
	    reviewerRoleButton.setTranslateX(-0);
	    reviewerRoleButton.setTranslateY(-60);
	    instructorRoleButton.setTranslateX(70);
	    instructorRoleButton.setTranslateY(-90);
	    staffRoleButton.setTranslateX(129);
	    staffRoleButton.setTranslateY(-120);
	    
	    
	    //Sets all the buttons to false
	    adminRoleButton.setVisible(false);
	    staffRoleButton.setVisible(false);
	    reviewerRoleButton.setVisible(false);
	    instructorRoleButton.setVisible(false);
	    studentRoleButton.setVisible(false);


	    /*
	     * The next segment will check if a user is assigned a role
	     * If they are assigned a role they will be able to see
	     * and select the roles they've been assigned
	     * */
	   
	    // Button to navigate to the Admin Welcome page
	    if(user.getRole()[0] == true) {
	    	adminRoleButton.setVisible(true);
		    adminRoleButton.setOnAction(a -> {
          
		        new AdminHomePage(databaseHelper, user).show(primaryStage, this.user);

		        
		    });
	    }
	    
	    
	    // Button to navigate to the Student Welcome page
	    if(user.getRole()[1] == true) {
		    studentRoleButton.setVisible(true);
		    studentRoleButton.setOnAction(a -> {
		    	new StudentHomePage(databaseHelper).show(primaryStage, this.user);		        
		    });
	    }
	    
	    // Button to navigate to the Reviewer Welcome page
	    if(user.getRole()[2] == true) {
		    reviewerRoleButton.setVisible(true);
		    reviewerRoleButton.setOnAction(a -> {

		    	new ReviewerHomePage(databaseHelper).show(primaryStage, this.user);		        

		    });
	    }
	    
	    // Button to navigate to the Instructor Welcome page
	    if(user.getRole()[3] == true) {
		    instructorRoleButton.setVisible(true);
		    instructorRoleButton.setOnAction(a -> {
		    	new InstructorHomePage(databaseHelper).show(primaryStage, this.user);     
		    });
	    }
	    
	    // Button to navigate to the Staff Welcome page
	    if(user.getRole()[4] == true) {
	    	staffRoleButton.setVisible(true);
		    staffRoleButton.setOnAction(a -> {
		    	new StaffHomePage(databaseHelper).show(primaryStage, this.user);		        
		    });
	    }
	    


	    layout.getChildren().addAll(userLabel, adminRoleButton, studentRoleButton, reviewerRoleButton, instructorRoleButton, staffRoleButton);
	    Scene roleSelect = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(roleSelect);
	    primaryStage.setTitle("Role Selection");
    	primaryStage.show();
    }
    
}
