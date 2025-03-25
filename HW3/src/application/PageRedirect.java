package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import databasePart1.*;

/**
 * 
 * This will immediately redirect a user to their welcome page if the only have one role
 * 
 */
public class PageRedirect {
	
	private final DatabaseHelper databaseHelper;
	private User user;

    public PageRedirect(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    public void redirect(Stage primaryStage, User user) {
    	this.user = user;
	    if(user.getRole()[0] == true) {
		    new AdminHomePage(databaseHelper, this.user).show(primaryStage, this.user);     
	    } else if(user.getRole()[1] == true) {
		    new StudentHomePage(databaseHelper).show(primaryStage, this.user);		        

	    } else if(user.getRole()[2] == true) {
		    new ReviewerHomePage(databaseHelper).show(primaryStage, this.user);		        

	    } else if(user.getRole()[3] == true) {
	    	new InstructorHomePage(databaseHelper).show(primaryStage, this.user);
	
	    } else if(user.getRole()[4] == true) {
		    new StaffHomePage(databaseHelper).show(primaryStage, this.user);		        
	    }

    }
}
