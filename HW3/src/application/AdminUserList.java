package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.util.Callback;
import javafx.scene.text.Font;
import java.util.ArrayList;

import java.sql.SQLException;


import databasePart1.*;
import application.User;

public class AdminUserList extends Application { // class required to use JavaFX TableView
	public final DatabaseHelper databaseHelper;
	private User user;
	public AdminUserList(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
    }
	
	public static class UserData { // class for data to be displayed in table
		private final SimpleStringProperty userName;
		private final SimpleStringProperty firstName;
		private final SimpleStringProperty lastName;
		private final SimpleStringProperty email;
		private final SimpleBooleanProperty adminRole;
		private final SimpleBooleanProperty studentRole;
		private final SimpleBooleanProperty reviewerRole;
		private final SimpleBooleanProperty instructorRole;
		private final SimpleBooleanProperty staffRole;
		
		// Constructor
		public UserData(String userName, String firstName, String lastName, String email, boolean adminRole, boolean studentRole, boolean reviewerRole, boolean instructorRole, boolean staffRole) {
			this.userName = new SimpleStringProperty(userName);
			this.firstName = new SimpleStringProperty(firstName);
			this.lastName = new SimpleStringProperty(lastName);
			this.email = new SimpleStringProperty(email);
			this.adminRole = new SimpleBooleanProperty(adminRole);
			this.studentRole = new SimpleBooleanProperty(studentRole);
			this.reviewerRole = new SimpleBooleanProperty(reviewerRole);
			this.instructorRole = new SimpleBooleanProperty(instructorRole);
			this.staffRole = new SimpleBooleanProperty(staffRole);
		}
		
		
		// Role order for booleans in database is Admin, Student, Reviewer, Instructor, Staff
		
		public SimpleStringProperty getUserNameValue() {
			return userName;
		}
		
		public SimpleStringProperty getFirstNameValue() {
			return firstName;
		}
		
		public SimpleStringProperty getLastNameValue() {
			return lastName;
		}
		
		public SimpleStringProperty getEmailValue() {
			return email;
		}
		
		public SimpleBooleanProperty getAdminRoleValue() {
			return adminRole;
		}
		
		public SimpleBooleanProperty getStudentRoleValue() {
			return studentRole;
		}
		
		public SimpleBooleanProperty getReviewerRoleValue() {
			return reviewerRole;
		}
		
		public SimpleBooleanProperty getInstructorRoleValue() {
			return instructorRole;
		}
		
		public SimpleBooleanProperty getStaffRoleValue() {
			return staffRole;
		}
		
		// Loop through User objects and create UserData objects
		public static ArrayList<UserData> convertToUserDataList(ArrayList<User> userList) {
	        // Loop through the list of User objects and create UserData objects
			ArrayList<UserData> userDataList = new ArrayList<>();
	        for (User user : userList) {
	            // Get user data from User object using getter methods
	            String userName = user.getUserName();
	            String firstName = user.getFirstName();
	            String lastName = user.getLastName();
	            String email = user.getEmail();
	            boolean adminRole = user.getRole()[0];
	            boolean studentRole = user.getRole()[1];
	            boolean reviewerRole = user.getRole()[2];
	            boolean instructorRole = user.getRole()[3];
	            boolean staffRole = user.getRole()[4];
	
	            // Create a new UserData object with the data from the User object
	            UserData userData = new UserData(userName, firstName, lastName, email, adminRole, studentRole, reviewerRole, instructorRole, staffRole);
	            
	            // Add the UserData object to the list
	            userDataList.add(userData);
	        }
	
	        return userDataList;
	    }
	}

	public void show(Stage primaryStage, User user) {
	
		// Create a TableView
		TableView<UserData> tableView = new TableView<>();
		
		// ArrayList to pull user information from database
		ArrayList<User> userList = databaseHelper.getUserList();
		
		// Convert array list userList from User (User.java) type to UserData type (AdminUserList.java) and save in userDataList
		ArrayList<UserData> userDataList = UserData.convertToUserDataList(userList);
		
		// Pull from database to populate table
		ObservableList<UserData> pullDatabaseData = FXCollections.observableArrayList(userDataList);
		// function to pull each set of data from database
		
		// Create columns for each field to display in the table
		TableColumn<UserData, String> userNameColumn = new TableColumn<>("USERNAME");
		userNameColumn.setCellValueFactory(cellData -> cellData.getValue().getUserNameValue());
		userNameColumn.setPrefWidth(85);;
		userNameColumn.setResizable(true); // allows user resize of column
		userNameColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, String> firstNameColumn = new TableColumn<>("FIRST NAME");
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameValue());
		firstNameColumn.setPrefWidth(85);;
		firstNameColumn.setResizable(true); // allows user resize of column
		firstNameColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, String> lastNameColumn = new TableColumn<>("LAST NAME");
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameValue());
		lastNameColumn.setPrefWidth(85);;
		lastNameColumn.setResizable(true); // allows user resize of column
		lastNameColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, String> emailColumn = new TableColumn<>("EMAIL");
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailValue());
		emailColumn.setPrefWidth(120);;
		emailColumn.setResizable(true); // allows user resize of column
		emailColumn.setStyle("-fx-font-size: 12px;");
		
		TableColumn<UserData, Boolean> adminRoleColumn = new TableColumn<>("ADMIN ROLE");
		adminRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getAdminRoleValue());
		adminRoleColumn.setPrefWidth(85);;
		adminRoleColumn.setResizable(true); // allows user resize of column
		adminRoleColumn.setStyle("-fx-font-size: 12px;");
		adminRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has an admin role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no admin role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> studentRoleColumn = new TableColumn<>("STUDENT ROLE");
		studentRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentRoleValue());
		studentRoleColumn.setPrefWidth(100);;
		studentRoleColumn.setResizable(true); // allows user resize of column
		studentRoleColumn.setStyle("-fx-font-size: 12px;");
		studentRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has a student role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no student role
						setStyle("-fx-text-fill: grey;");// Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> reviewerRoleColumn = new TableColumn<>("REVIEWER ROLE");
		reviewerRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getReviewerRoleValue());
		reviewerRoleColumn.setPrefWidth(105);
		reviewerRoleColumn.setResizable(true); // allows user resize of column
		reviewerRoleColumn.setStyle("-fx-font-size: 12px;");
		reviewerRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has a reviewer role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no reviewer role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> instructorRoleColumn = new TableColumn<>("INSTRUCTOR ROLE");
		instructorRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getInstructorRoleValue());
		instructorRoleColumn.setPrefWidth(120);
		instructorRoleColumn.setResizable(true); // allows user resize of column
		instructorRoleColumn.setStyle("-fx-font-size: 12px;");
		instructorRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has an instructor role
						setStyle("-fx-font-weight: bold;");// Bold text
					}
					else { // user has no instructor role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Boolean> staffRoleColumn = new TableColumn<>("STUDENT ROLE");
		staffRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getStaffRoleValue());
		staffRoleColumn.setPrefWidth(100);
		staffRoleColumn.setResizable(true); // allows user resize of column
		staffRoleColumn.setStyle("-fx-font-size: 12px;");
		staffRoleColumn.setCellFactory(column -> new TableCell<UserData, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) { // null or empty
					setText(null);
					setStyle("");
				}
				else {
					setText(item ? "True" : "False"); // Boolean returns as true/false
					if (item) { // user has a staff role
						setStyle("-fx-font-weight: bold;"); // Bold text
					}
					else { // user has no staff role
						setStyle("-fx-text-fill: grey;"); // Grey out text
					}
				}
			}
		});
		
		TableColumn<UserData, Void> profileButtonColumn = new TableColumn<>("USER PROFILE");
		profileButtonColumn.setPrefWidth(85);
		profileButtonColumn.setResizable(true); // allows user resize of column
		profileButtonColumn.setStyle("-fx-font-size: 12px;");
		profileButtonColumn.setCellFactory(new Callback<TableColumn<UserData, Void>, TableCell<UserData, Void>>() {
			@Override
		    public TableCell<UserData, Void> call(TableColumn<UserData, Void> param) {
		        return new TableCell<UserData, Void>() {
		            private final Button btn = new Button("User Profile");

		            {
		                // Set the action when the button is clicked
		                btn.setOnAction(event -> {
		                    UserData userData = getTableView().getItems().get(getIndex());
		                    System.out.println("Button clicked for user: " + userData.getUserNameValue().get());
		                    String userNameOfRow = userData.getUserNameValue().get();
		                    System.out.println(userNameOfRow);
		                    AdminUserModifications adminUserMod = new AdminUserModifications(databaseHelper, userNameOfRow, user);
		                    Stage AdminUserModificationStage = new Stage();
		                    adminUserMod.show(primaryStage, user);
		                });
		            }

		            @Override
		            public void updateItem(Void item, boolean empty) {
		                super.updateItem(item, empty);
		                if (empty) {
		                    setGraphic(null);  // No button if the row is empty
		                } else {
		                    setGraphic(btn);  // Set the button to the row
		                }
		            }
		        };
		    }
		});
		
		
		// Add columns to TableView
		tableView.getColumns().addAll(userNameColumn, firstNameColumn, lastNameColumn, emailColumn, adminRoleColumn, studentRoleColumn, reviewerRoleColumn, instructorRoleColumn, staffRoleColumn, profileButtonColumn);
		
		
		// Create a search box 
		TextField searchBox = new TextField();
        searchBox.setPromptText("Search");
        searchBox.setMaxWidth(250);
    	
    	// Set up sorting capability
        FilteredList<UserData> filteredUserData = new FilteredList<>(pullDatabaseData, p -> true);
        
        // Enable the text entered in searchBox to filter the data
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUserData.setPredicate(userData -> {
            	if (newValue == null || newValue.isEmpty()) {
            		return true; // display full user list as searchBox is null or empty
            	}
            	String convertToLowerCase = newValue.toLowerCase(); //converts the searched value to lower case
            	
            	// checks for search terms specific to boolean role values so ignoring case
            	if ("admin".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getAdminRoleValue().get(); // return only rows where the value is "True" for Admin Role column
            	}
            	if ("student".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getStudentRoleValue().get(); // return only rows where the value is "True" for Student Role column
            	}
            	if ("reviewer".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getReviewerRoleValue().get(); // return only rows where the value is "True" for Reviewer Role column
            	}
            	if ("instructor".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getInstructorRoleValue().get(); // return only rows where the value is "True" for Instructor Role column
            	}
            	if ("staff".equalsIgnoreCase(convertToLowerCase)) {
            		return userData.getStaffRoleValue().get(); // return only rows where the value is "True" for Staff Role column
            	}
            	
            	return userData.getUserNameValue().get().toLowerCase().contains(convertToLowerCase) || // converts all returned values to lower case for comparison with search term
            		   userData.getFirstNameValue().get().toLowerCase().contains(convertToLowerCase) ||
            		   userData.getLastNameValue().get().toLowerCase().contains(convertToLowerCase) ||
            		   userData.getEmailValue().get().toLowerCase().contains(convertToLowerCase);
            });
        });
        
        SortedList<UserData> sortedUserData = new SortedList<>(filteredUserData); // applies sorting to data
        sortedUserData.comparatorProperty().bind(tableView.comparatorProperty()); // keeps the data accurate when sort of a column is done
        
        // set the table items with the sorted database data
        tableView.setItems(sortedUserData);
     		
        
	    Button returnButton = new Button("Return to Admin homepage");
	    // Button to return to the Admin homepage
	    
	    returnButton.setOnAction(a -> {
	    	AdminHomePage adminHomePage = new AdminHomePage(databaseHelper, user);
	    	adminHomePage.show(primaryStage, this.user);
	        
	    });
	    VBox layout = new VBox(5);
	    layout.setStyle("-fx-padding: 5;");
	    layout.getChildren().addAll(searchBox, tableView,returnButton);
	    Scene scene = new Scene(layout, 980, 400);

	    // Set the scene to primary stage
	    
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Admin User View");
    	primaryStage.show();
    }
	
	@Override
    public void start(Stage primaryStage) {
        show(primaryStage, user);
    }
	
    public static void main(String[] args) {
    	launch(args);
    }
}