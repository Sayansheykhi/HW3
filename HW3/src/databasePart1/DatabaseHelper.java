package databasePart1;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;

import application.Answer;
import application.Question;
import application.User;
import java.util.ArrayList;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 
	
	// Fixed expiration duration.
	private final int expirationTimer = 15 * 60;
	// PreparedStatement pstmt
	private Connection connection = null;
	private Statement statement = null; 
	
	
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		// Create the user information table.
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "firstName VARCHAR(255), "
				+ "lastName VARCHAR (255), "
				+ "email VARCHAR(255), "
				+ "role VARCHAR(255))";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	    		+ "email VARCHAR(255), "
	            + "code VARCHAR(10) PRIMARY KEY, "
	    		+ "role VARCHAR(255), "
	            + "isUsed BOOLEAN DEFAULT FALSE, "
	            + "deadline DATE)";
	    statement.execute(invitationCodesTable);
	    
	    // Create the password reset codes table.
	    String passwordResetsTable = "CREATE TABLE IF NOT EXISTS passwordResets ("
	    		+ "userName VARCHAR(255), "
	    		+ "resetCode VARCHAR(255) UNIQUE, "
	    		+ "expiration INT)";
	    statement.execute(passwordResetsTable);
	    
		String questionTable = "CREATE TABLE IF NOT EXISTS questions ("
				+ "questionID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "studentUserName VARCHAR(255), "
				+ "studentFirstName VARCHAR(255), "
				+ "studentLastName VARCHAR(255), "
				+ "questionTitle VARCHAR(255), "
				+ "questionBody TEXT, "
				+ "isResolved BOOLEAN DEFAULT FALSE, "
				+ "creationTime DATETIME, "
				+ "oldQuestionID INT"
				+ ");";
		statement.execute(questionTable);
		
		String answerTable = "Create TABLE IF NOT EXISTS answers ("
				+ "answerID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "studentUserName VARCHAR(255), "
				+ "studentFirstName VARCHAR(255), "
				+ "studentLastName VARCHAR(255), "
				+ "questionID INT, "
				+ "answerText TEXT, "
				+ "isAnswerUnread BOOLEAN DEFAULT TRUE, "
				+ "isResolved BOOLEAN, "
				+ "creationTime DATETIME"
				+ ");";
		statement.execute(answerTable);	
		
		String questionReplyTable = "Create TABLE IF NOT EXISTS questionReplies ("
				+ "replyID INT AUTO_INCREMENT PRIMARY KEY, "
				+ "questionID INT, "
				+ "studentUserName VARCHAR(255), "
				+ "studentFirstName VARCHAR(255), "
				+ "studentLastName VARCHAR(255), "
				+ "questionReplyText TEXT, "
				+ "creationTime DATETIME, "
				+ "replyingTo TEXT"
				+ ");";
		statement.execute(questionReplyTable);	
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	//Get number of users in database.
	public int countDataBase() throws SQLException{
		String query = "SELECT COUNT(*) FROM cse360users";
		try(ResultSet resultSet = statement.executeQuery(query)){
			resultSet.next();
			int count = resultSet.getInt(1);
			return count;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int countAdminDataBase(){
		ArrayList<User> userList = getUserList();
		int count = 0;
		for(User u : userList) {
			if(u.getRole()[0]) {
				count++;
			}
		}
		return count;
	}
	
	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, firstName, lastName, email, role) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getFirstName());
			pstmt.setString(4, user.getLastName());
			pstmt.setString(5, user.getEmail());
			pstmt.setString(6, Arrays.toString(user.getRole())); 
			pstmt.executeUpdate();
		}
	}

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, Arrays.toString(user.getRole()));
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	public User getUser(String userName, String password) {
		String getUser = "SELECT * FROM cse360users WHERE userName = ? AND password = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(getUser)) {
			pstmt.setString(1, userName);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return new User(
					rs.getString("userName"),
					rs.getString("password"),
					new boolean[5],
					rs.getString("email"),
					rs.getString("firstName"),
					rs.getString("lastName")
				);
			}
		}
		catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieve all users information from database excluding password.
	public ArrayList<User> getUserList(){
		ArrayList<User>  userList = new ArrayList<>();
		String query = "SELECT userName || ',' || firstName || ',' || lastName ||  " 
				+ "',' || email || ',' || role FROM cse360users";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String[] userInfoString = rs.getString(1).split(",");
				boolean[] roles = stringToBoolArray(userInfoString, 4);
				userList.add(new User(userInfoString[0], "", roles, userInfoString[3], userInfoString[1], userInfoString[2]));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public boolean[] getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	String[] userInfoString = rs.getString(1).split(",");
	        	boolean[] roles = stringToBoolArray(userInfoString, 0);
	            return roles; // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public boolean[] getInvitedUserRole(String email) {
	    String query = "SELECT role FROM InvitationCodes WHERE email = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, email);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	String[] userInfoString = rs.getString(1).split(",");
	        	boolean[] roles = stringToBoolArray(userInfoString, 0);
	            return roles; // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public void deleteUser(User user) {
		String query = "DELETE FROM cse360users WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, user.getUserName());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode(String email, boolean[] roles, LocalDate deadline) {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (email, code, role, deadline) VALUES (?, ?, ?, ?)";
	    System.out.println(code);
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, email);
	        pstmt.setString(2, code);
	        pstmt.setString(3,	Arrays.toString(roles));
	        pstmt.setDate(4, java.sql.Date.valueOf(deadline));
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT COUNT(*) FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            if(checkExpiration(code)) {
	            	return true;
	            }else {
	            	return false;
	            }
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean checkExpiration(String code) {
		String query = "SELECT deadline FROM InvitationCodes WHERE code = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1,  code);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				System.out.print(rs.getDate(1));
				System.out.println(Date.valueOf(LocalDate.now()));
				if(rs.getDate(1).after(Date.valueOf(LocalDate.now()))){
					return true;
				}else {
					return false;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

	// Update user's password in userTable
	public void setUserPassword(String userName, String newPass, String oneTimePass) {
		String query = "UPDATE cse360users SET password = ? WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, newPass);
			pstmt.setString(2, userName);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	// Creates a password reset request in the passResets table.
	public void createRequest(String userName, String oneTimePass) {
		int expiration = (int) System.currentTimeMillis() / 1000; //Stores creation time in seconds.
		String query = "INSERT INTO passwordResets (userName, resetCode, expiration) VALUES(?, ?, ?)"; 
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			pstmt.setString(2, oneTimePass);
			pstmt.setInt(3, expiration);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	};
	// Removes a request from the passResets table.
	public void deleteRequest(String userName) {
		String query = "DELETE FROM passwordResets WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	};
	
	public ArrayList<String> getRequests() {
		ArrayList<String> requests = new ArrayList<String>();
		String query = "SELECT userName || ',' || resetCode || ',' || expiration FROM passwordResets";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				requests.add(rs.getString(1));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return requests;
	};
	
	public boolean doesRequestExist(String userName) {
		String query = "SELECT * FROM passwordResets WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() ? true : false;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	};
	
	public User getUserInfo(String userName) {
		String query = "SELECT firstName || ',' || lastName || " 
				+ "',' || email || ',' || role FROM cse360users WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				String[] userString = rs.getString(1).split(",");
				for(String s: userString) {
				System.out.println(s);
				}
				return new User(userName, "", stringToBoolArray(userString, 3), userString[2], userString[0], userString[1]);
			}	
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Nope don't work boy");
		}
		return null;
	}
	
	public void setUserRoles(User user, boolean[] roles) {
		String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
		try(PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, Arrays.toString(roles));
			pstmt.setString(2,  user.getUserName());
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean[] stringToBoolArray (String[] userInfo, int index) {
		boolean[] roles = new boolean[5];
		int j = 0;
		for(int i = index; i < userInfo.length; i++) {
			if(userInfo[i].contains("true")) {
				roles[j] = true;
				j++;
			}else {
				roles[j] = false;
				j++;
			}
		}
		return roles;
	}
	
	public int addQuestion(String questionTitle, String questionBody, Question question, User student) throws SQLException {
		String insertQuestion = "INSERT INTO questions (studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime, oldQuestionID) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
		int questionIDGenerated = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuestion, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, student.getUserName());
			pstmt.setString(2, student.getFirstName());
			pstmt.setString(3, student.getLastName());
			pstmt.setString(4, questionTitle);
			pstmt.setString(5, questionBody);
			pstmt.setBoolean(6, false);
			
			LocalDateTime creationTime = question.getCreationTime();
			pstmt.setTimestamp(7, Timestamp.valueOf(creationTime));
			
			pstmt.setInt(8, -1);
			
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) { // retrieve primary key "questionID" generated by INSERT above
				if (rs.next()) {
					questionIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return questionIDGenerated;
	}
	
		public String getUserFromQuestionID(int questionID) {
			String studentUserName = "";
			String getUser = "SELECT studentUserName FROM questions WHERE questionID = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(getUser)) {
				pstmt.setInt(1, questionID);
				
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next()) {
					studentUserName = rs.getString("studentUserName");
					
					System.out.printf("UserName in DataBaseHelper as a string is %s\n", studentUserName);
				}
			}
			catch (SQLException e) {
		        e.printStackTrace();
		    }
			return studentUserName;
		}
	
	
	public ArrayList<Question> getAllQuestions(User user) { 
		ArrayList<Question> allQuestions = new ArrayList<>();
		String sqlQuery = "SELECT questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime FROM questions";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				allQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allQuestions;
	}
	
	
	public ArrayList<Question> getAllReplies() {
		ArrayList<Question> allReplies = new ArrayList<>();
		String sqlQuery = "SELECT replyID, questionID, studentUserName, studentFirstName, studentLastName, questionReplyText, replyingTo FROM questionReplies";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int replyID = rs.getInt("replyID");
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionReplyText = rs.getString("questionReplyText");
				String questionReplyingto = rs.getString("replyingto");
				
				Question replyObject = new Question(replyID, questionID, studentUserName, studentFirstName, studentLastName, questionReplyText, questionReplyingto);
				allReplies.add(replyObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allReplies;	
	}
	
	public ArrayList<Question> getUnresolvedQuestions() { 
		ArrayList<Question> unresolvedQuestions = new ArrayList<>();
		String sqlQuery = "SELECT questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime FROM questions WHERE isResolved = false";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				unresolvedQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return unresolvedQuestions;
	}
	
	public ArrayList<Question> getAnsweredQuestions() {
		ArrayList<Question> getAnsweredQuestions = new ArrayList<>();
		String sqlQuery = "SELECT q.questionID, q.studentUserName, q.studentFirstName, q.studentLastName, q.questionTitle, q.questionBody, q.isResolved, q.creationTime FROM questions q WHERE q.questionID IN (SELECT a.questionID FROM answers a)";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				getAnsweredQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return getAnsweredQuestions;
	}
	
	public ArrayList<Question> addQuestionRepliesToAnsweredQuestions(ArrayList<Question> answeredQuestions) {
		String sqlQuery = "SELECT r.replyID, r.studentFirstName, r.studentLastName, r.questionReplyText, r.replyingTo FROM questionReplies r WHERE r.questionID IN (SELECT a.questionID FROM answers a)";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int replyID = rs.getInt("replyID");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionReplyText = rs.getString("questionReplyText");
				String replyingTo = rs.getString("replyingTo");
				
				Question questionObject = new Question(replyID, studentFirstName, studentLastName, questionReplyText, replyingTo);
				
				answeredQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answeredQuestions;
	}
	
	
	
	public ArrayList<Question> getRecentQuestions(LocalDateTime time) { 
		ArrayList<Question> recentQuestions = new ArrayList<>();
		Timestamp sqlTimestamp = Timestamp.valueOf(time);
		String sqlQuery = "SELECT questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime FROM questions WHERE creationTime > ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setTimestamp(1, sqlTimestamp);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime.toLocalDateTime());
				
				recentQuestions.add(questionObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return recentQuestions;
		
	}
	
	public void markQuestionDeleted(int questionID){
		String sqlUpdate = "UPDATE questions SET studentUserName = 'Deleted Student User Name', studentFirstName = 'Deleted Student First Name', studentLastName = 'Deleted Student Last Name', questionTitle = 'Deleted Question Title', questionBody = 'Deleted Question Body' WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setInt(1, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteQuestion(int questionID) {
		String sqlDelete = "DELETE FROM questions where questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteRepliesForQuestion(int questionID) {
		String sqlDelete = "DELETE FROM questionReplies where questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Question getQuestionByID(int questionID) {
		Question question = null;
		String sqlQuery = "SELECT studentFirstName, studentLastName, questionTitle, questionBody FROM questions WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
					
				question = new Question(studentFirstName, studentLastName, questionTitle, questionBody);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return question;
	}
	
	public int createNewQuestionfromOld(String newQuestionTitle, String newQuestionBody, Question newQuestion, User student, int oldQuestionID) {
		int questionIDGenerated = -1;
		String insertQuestion = "INSERT INTO questions (studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime, oldQuestionID) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuestion, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, newQuestion.getStudentUserName());
			pstmt.setString(2, newQuestion.getStudentFirstName());
			pstmt.setString(3, newQuestion.getStudentLastName());
			pstmt.setString(4, newQuestionTitle);
			pstmt.setString(5, newQuestionBody);
			pstmt.setBoolean(6, false);
			pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			pstmt.setInt(8, oldQuestionID);
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) { // retrieve primary key "questionID" generated by INSERT above
				if (rs.next()) {
					questionIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return questionIDGenerated;
	}
	
	public void editQuestion(String modifiedQuestionTitle, String modifiedQuestionBody, int questionID) {
		String sqlUpdate = "UPDATE questions SET questionTitle = ?, questionBody = ? WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setString(1, modifiedQuestionTitle);
			pstmt.setString(2, modifiedQuestionBody);
			pstmt.setInt(3, questionID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean markQuestionResolved(int questionID) {
	    String sqlUpdate = "UPDATE questions SET isResolved = TRUE WHERE questionID = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
	        pstmt.setInt(1, questionID);
	        int rowsAffected = pstmt.executeUpdate();
	        connection.commit(); 
	        return rowsAffected > 0;
	    }
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public int addAnswers(String answerText, Answer answer, User student, int questionID) {
		String insertAnswer = "INSERT INTO answers (studentUserName, studentFirstName, studentLastName, questionID, answerText, isAnswerUnread, isResolved, creationTime) VALUES (?, ?, ?, ?, ?, TRUE, FALSE, ?) ";
		int answerIDGenerated = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(insertAnswer, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, student.getUserName());
			pstmt.setString(2, student.getFirstName());
			pstmt.setString(3, student.getLastName());
			pstmt.setInt(4,questionID);
			pstmt.setString(5, answerText);
			//pstmt.setBoolean(6, answer.getIsAnswerUnread());
			//pstmt.setBoolean(7, answer.getIsResolved());
			
			LocalDateTime creationTime = answer.getCreationTime();
			pstmt.setTimestamp(6, Timestamp.valueOf(creationTime));
			pstmt.executeUpdate();
			
			try(ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					answerIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answerIDGenerated;
	}
	
	public ArrayList<Answer> getAllAnswers(User user) { 
		ArrayList<Answer> allAnswers = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime FROM answers";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int answerID = rs.getInt("answerID");
				int questionID = rs.getInt("questionID");
				String studentUserName= rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText= rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isResolved");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Answer answerObject = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime());
				
				allAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return allAnswers;
	}
	
	public ArrayList<Answer> getAnswersByQuestionID(int questionID) {
		ArrayList<Answer> answersForQuestionID = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isResolved, isAnswerUnread, creationTime FROM answers WHERE questionID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int answerID = rs.getInt("answerID");
				int questID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				boolean isResolved = rs.getBoolean("isResolved");
				boolean isAnswerUnread = rs.getBoolean("isAnswerUnread");
				Timestamp creationTime = rs.getTimestamp("creationTime");
					
				Answer answer = new Answer(answerID, questID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime());
				answersForQuestionID.add(answer);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answersForQuestionID;
	}
	
	public int getQuestionIDForAnswer(int answerID) {
		int questionID = -1;
		String sqlQuery = "SELECT questionID FROM answers WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, answerID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				questionID = rs.getInt("questionID");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return questionID;
	}
	
	public ArrayList<Answer> getUnreadAnswers() { 
		ArrayList<Answer> unreadAnswers = new ArrayList<>();
		String sqlQuery = "SELECT studentFirstName, studentLastName, answerText FROM answers WHERE isAnswerUnread = true";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery(sqlQuery);
			while(rs.next()) {
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				
				Answer answerObject = new Answer(studentFirstName, studentLastName, answerText) ;
				
				unreadAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return unreadAnswers;
	}
	
	// This may be wrong because its basing it on whether the answer its marked unresolved, not the question
	public ArrayList<Answer> getAnswersUnresolvedQuestions() {
		ArrayList<Answer> answersForUnresolvedQuestions = new ArrayList<>();
		String sqlQuery = "SELECT studentFirstName, studentLastName, answerText FROM answers WHERE isResolved = false";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery(sqlQuery);
			while(rs.next()) {
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				
				Answer answerObject = new Answer(studentFirstName, studentLastName, answerText) ;
				
				answersForUnresolvedQuestions.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answersForUnresolvedQuestions;
	}
	
	
	public ArrayList<Answer> getResolvedAnswers() {
		ArrayList<Answer> resolvedAnswers = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime FROM answers WHERE isResolved = TRUE";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int answerID = rs.getInt("answerID");
				int questionID = rs.getInt("questionID");
				String studentUserName= rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText= rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isResolved");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Answer answerObject = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime()) ;
				
				resolvedAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return resolvedAnswers;
	}
	
	public ArrayList<Answer> getUnresolvedAnswers() {
		ArrayList<Answer> unresolvedAnswers = new ArrayList<>();
		String sqlQuery = "SELECT answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime FROM answers WHERE isResolved = FALSE";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int answerID = rs.getInt("answerID");
				int questionID = rs.getInt("questionID");
				String studentUserName= rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText= rs.getString("answerText");
				boolean isAnswerUnread = rs.getBoolean("isResolved");
				boolean isResolved = rs.getBoolean("isResolved");
				Timestamp creationTime = rs.getTimestamp("creationTime");
				
				Answer answerObject = new Answer(answerID, questionID, studentUserName, studentFirstName, studentLastName, answerText, isAnswerUnread, isResolved, creationTime.toLocalDateTime()) ;
				
				unresolvedAnswers.add(answerObject);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return unresolvedAnswers;
	}
	
	public Answer getAnswerByID(int answerID) {
		Answer answer = null;
		String sqlQuery = "SELECT studentFirstName, studentLastName, answerText FROM answers WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, answerID);
			ResultSet rs = pstmt.executeQuery(sqlQuery);
			if (rs.next()) {
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");

				answer = new Answer(studentFirstName, studentLastName, answerText);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return answer;
	}
	
	public void deleteAnswer(int answerID) {
		String sqlDelete = "DELETE FROM answers where answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlDelete)) {
			pstmt.setInt(1, answerID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void editAnswer(String modifiedAnswer, int answerID) {
		String sqlUpdate = "UPDATE answers SET answerText = ? WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setString(1, modifiedAnswer);
			pstmt.setInt(2, answerID);
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean markAnswerResolved(int answerID) {
		String sqlUpdate = "UPDATE answers SET isResolved = TRUE WHERE answerID = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {
			pstmt.setInt(1, answerID);
			int rowsAffected = pstmt.executeUpdate();
			connection.commit(); 
			return rowsAffected > 0;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	public int addReply(String replyText, int parentQuestionID, Question questionReply, User student, String replyingTo) {
		String insertQuestionReply = "INSERT INTO questionReplies (questionID, studentUserName, studentFirstName, studentLastName, questionReplyText, creationTime, replyingTo) VALUES (?, ?, ?, ?, ?, ?, ?) ";
		int replyIDGenerated = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(insertQuestionReply, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, parentQuestionID);
			pstmt.setString(2, student.getUserName());
			pstmt.setString(3, student.getFirstName());
			pstmt.setString(4, student.getLastName());
			pstmt.setString(5, replyText);
			
			LocalDateTime creationTime = questionReply.getCreationTime();
			pstmt.setTimestamp(6, Timestamp.valueOf(creationTime));
			pstmt.setString(7, replyingTo);
			
			pstmt.executeUpdate();
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) { // retrieve primary key "replyID" generated by INSERT above
				if (rs.next()) {
					replyIDGenerated = rs.getInt(1);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return replyIDGenerated;
	}

	public ArrayList<Question> getUnansweredQuestions() {
		ArrayList<Question> unansweredQuestions = new ArrayList<>();
		String sqlQuery = "SELECT q.questionID, q.studentUserName, q.studentFirstName, q.studentLastName, q.questionTitle, q.questionBody, q.isResolved, q.creationTime " +
				"FROM questions q LEFT JOIN answers a ON q.questionID = a.questionID " +
				"WHERE a.answerID IS NULL";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentUserName = rs.getString("studentUserName");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				LocalDateTime creationTime = rs.getTimestamp("creationTime").toLocalDateTime();

				Question questionObject = new Question(questionID, studentUserName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime);
				questionObject.setQuestionID(questionID);
				unansweredQuestions.add(questionObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unansweredQuestions;
	}

	public ArrayList<Question> getQuestionsByUserName(String userName) {
		ArrayList<Question> userQuestions = new ArrayList<>();
		String sqlQuery = "SELECT questionID, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime " +
				"FROM questions WHERE studentUserName = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setString(1, userName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int questionID = rs.getInt("questionID");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String questionTitle = rs.getString("questionTitle");
				String questionBody = rs.getString("questionBody");
				boolean isResolved = rs.getBoolean("isResolved");
				LocalDateTime creationTime = rs.getTimestamp("creationTime").toLocalDateTime();

				Question questionObject = new Question(questionID, userName, studentFirstName, studentLastName, questionTitle, questionBody, isResolved, creationTime);
				questionObject.setQuestionID(questionID);
				userQuestions.add(questionObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userQuestions;
	}

	public int countUnreadPotentialAnswers(int questionID) {
		int unreadCount = 0;
		String sqlQuery = "SELECT COUNT(*) AS unreadCount FROM answers WHERE questionID = ? AND isAnswerUnread = TRUE";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			pstmt.setInt(1, questionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				unreadCount = rs.getInt("unreadCount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unreadCount;
	}

	public ArrayList<Answer> getResolvedAnswersWithQuestions() {
		ArrayList<Answer> resolvedAnswers = new ArrayList<>();
		String sqlQuery = "SELECT a.answerID, a.studentFirstName, a.studentLastName, a.answerText, a.questionID, q.questionTitle " +
				"FROM answers a JOIN questions q ON a.questionID = q.questionID " +
				"WHERE a.isResolved = TRUE";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int answerID = rs.getInt("answerID");
				String studentFirstName = rs.getString("studentFirstName");
				String studentLastName = rs.getString("studentLastName");
				String answerText = rs.getString("answerText");
				int questionID = rs.getInt("questionID");
				String questionTitle = rs.getString("questionTitle");

				Answer answerObject = new Answer(studentFirstName, studentLastName, answerText);
				answerObject.setAnswerID(answerID);
				answerObject.setQuestionID(questionID);
				
				Question questionObject = new Question(studentFirstName, studentLastName, questionTitle);
				
				String questionTitleForAnswer = questionObject.getQuestionTitle();
				answerObject.setQuestionTitleForAnswer(questionTitleForAnswer);
				resolvedAnswers.add(answerObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resolvedAnswers;
	}
	
	/*
	public ArrayList<Answer> getAnswersFromSpecifiedReviewers() {
		String sqlQuery = "SELECT studentFirstName, studentLastName, answerText FROM answers WHERE reviewerName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/
}