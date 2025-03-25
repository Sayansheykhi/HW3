package application;

import java.util.ArrayList;
import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class StudentHomePage {
	
	private final DatabaseHelper databaseHelper;
	private User user;

	public StudentHomePage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	/**
	 * Display the student home page.
	 */
	public void show(Stage primaryStage, User user) {
	    this.user = user;
	    
	    VBox mainLayout = new VBox(15);
	    mainLayout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Label welcomeLabel = new Label("Welcome, Student!");
	    welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
	    
	    // Navigation Buttons
	    Button privateMessagesButton = new Button("View Private Messages");
	    privateMessagesButton.setOnAction(e -> openPrivateMessages(primaryStage));

	    Button trustedReviewersButton = new Button("Trusted Reviewers");
	    trustedReviewersButton.setOnAction(e -> openTrustedReviewers(primaryStage));

	    Button questionsAndAnswersButton = new Button("Questions and Answers");
	    questionsAndAnswersButton.setOnAction(e -> openQuestionsAndAnswers(primaryStage));

	    // Questions and Answers Section
	    HBox contentLayout = new HBox(20);
	    contentLayout.setStyle("-fx-padding: 20;");

	    Button logoutButton = new Button("Logout");
	    logoutButton.setOnAction(a -> confirmLogout(primaryStage));

	    mainLayout.getChildren().addAll(
	        welcomeLabel,
	        privateMessagesButton,
	        trustedReviewersButton,
	        questionsAndAnswersButton,
	        contentLayout,
	        logoutButton
	    );

	    Scene studentScene = new Scene(mainLayout, 1200, 600);
	    primaryStage.setScene(studentScene);
	    primaryStage.setTitle("Student Home Page");
	    primaryStage.show();
	}

	/**
	 * Navigation Methods
	 */
	private void openPrivateMessages(Stage primaryStage) {
	    StudentPrivateMessages messagesPage = new StudentPrivateMessages(databaseHelper);
	    messagesPage.show(primaryStage, this.user);
	}

	private void openTrustedReviewers(Stage primaryStage) {
	    StudentTrustedReviewersList reviewersPage;
	    reviewersPage = new StudentTrustedReviewersList(databaseHelper);
	    reviewersPage.show(primaryStage, this.user);
	}

	private void openQuestionsAndAnswers(Stage primaryStage) {
	    StudentQuestionsAnswers qaPage = new StudentQuestionsAnswers(databaseHelper);
	    qaPage.show(primaryStage, this.user);
	}

	private void confirmLogout(Stage primaryStage) {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Logout Confirmation");
	    alert.setHeaderText("Please confirm if you're logging out.");
	    alert.setContentText("Make sure all changes are saved.");

	    alert.getButtonTypes().setAll(new ButtonType("Save and Logout"), new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
	    alert.showAndWait().ifPresent(response -> logout(primaryStage));
	}

	private void logout(Stage primaryStage) {
	    new UserLoginPage(databaseHelper).show(primaryStage, this.user);
	}
}