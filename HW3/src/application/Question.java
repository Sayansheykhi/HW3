package application;

import databasePart1.DatabaseHelper;
import java.time.LocalDateTime;
import java.util.Objects;

public class Question {
	private int userID;
	private User user; // Replaces studentID, studentFirstName, and studentLastName
	private boolean isResolved; // if true, an answer was marked as resolving the question
	private String questionTitle; // Title of the question
	private String questionBody; // Full question text
	private LocalDateTime creationTime; // Timestamp of question creation
	private int previousQuestionID; // Used for linking to a prior question
	private int questionID;
	public static String questionTitleError = ""; // Stores validation errors for question title
	public static String questionBodyError = ""; // Stores validation errors for question body
	public static String questionReplyError = ""; // Stores validation errors for question reply text
	private String studentUserName; // student Username in database
	private String studentFirstName;
	private String studentLastName;
	private String questionReply;
	int replyID;
	String replyingTo;

	// Empty Constructor
	public Question() {
		//this.user = user; // Initializes an empty user object
		this.isResolved = false;
		this.questionTitle = "";
		this.questionBody = "";
		this.creationTime = LocalDateTime.now();
		this.previousQuestionID = -1;
		this.studentFirstName = "";
		this.studentLastName = "";
		//this.questionID = -1;
	}

	// Constructor
	public Question(int userID, boolean isResolved, String questionTitle, String questionBody, LocalDateTime creationTime, String studentFirstName, String studentLastName, int previousQuestionID, int questionID) {
		//this.user = user;
		this.userID = userID;
		this.isResolved = isResolved;
		this.questionTitle = questionTitle;
		this.questionBody = questionBody;
		this.creationTime = creationTime;
		this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
		this.previousQuestionID = previousQuestionID;
		this.questionID = questionID;
		this.replyID = -1; // default
	}

	// Partial Constructor for retrieving questions from the database
	public Question(String studentFirstName, String studentLastName, String questionTitle, String questionBody) {
		//this.user = user;
		this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        
        // defaults set for columns I don't currently need
        //this.studentID = -1;
        this.isResolved = false;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default
	}

	// Partial Constructor for retrieving questions for observable list
		public Question(int questionID, String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody, boolean isResolved, LocalDateTime creationTime) {
			//this.user = user;
			this.questionID = questionID;
			this.studentUserName = studentUserName;
			this.studentFirstName = studentFirstName;
	        this.studentLastName = studentLastName;
	        this.questionTitle = questionTitle;
	        this.questionBody = questionBody;
	        this.creationTime = creationTime;
	        // defaults set for columns I don't currently need
	        //this.studentID = -1;
	        this.isResolved = isResolved;
	        this.replyID = -1; // default
		}
	
	// Partial Constructor for retrieving questions based on creation time
	public Question(String studentFirstName, String studentLastName, String questionTitle, String questionBody, LocalDateTime creationTime) {
		//this.user = user;
		this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.creationTime = LocalDateTime.now(); // PROB NEED TO FIX TO SSET = to creationTime
        // defaults set for columns I don't currently need
        //this.studentID = -1;
        this.isResolved = false;
        this.replyID = -1; // default
	}
	
	// Partial Constructor which includes studentUserName for use with getUnansweredQuestions()
	public Question(String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody, LocalDateTime creationTime) {
		this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.studentUserName = studentUserName;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default
	}
	
	// Partial Constructor which includes studentUserName for use with getUnansweredQuestions()
		public Question(int questionID, String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody, LocalDateTime creationTime) {
			this.questionID = questionID;
			this.studentFirstName = studentFirstName;
	        this.studentLastName = studentLastName;
	        this.questionTitle = questionTitle;
	        this.questionBody = questionBody;
	        this.studentUserName = studentUserName;
	        this.creationTime = creationTime;
	        this.replyID = -1; // default
		}
	
	
	// Partial Constructor for use with getResolvedAnswersWithQuestions()
	public Question(String studentFirstName, String studentLastName, String questionTitle) {
		this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionTitle = questionTitle;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default
	}
	
	// Partial Constructor specific to User
	public Question(User user, String questionTitle, String questionBody) {
		this.user = user;
		this.questionTitle = questionTitle;
        this.questionBody = questionBody;
        this.creationTime = LocalDateTime.now();
        this.replyID = -1; // default
        this.isResolved = false;
	}
	
	// Partial Constructor for marking Question deleted
		public Question(String studentUserName, String studentFirstName, String studentLastName, String questionTitle, String questionBody) {
			this.studentUserName = studentUserName;
			this.studentFirstName = studentFirstName;
			this.studentLastName = studentLastName;
			this.questionTitle = questionTitle;
	        this.questionBody = questionBody;
	        //this.creationTime = LocalDateTime.now();
	        this.replyID = -1; // default
	        //this.isResolved = false;
		}
	
	// Constructor specific for Question Replies
	public Question(int replyID, int questionID, String studentUserName, String studentFirstName, String studentLastName, String questionReplyText, String replyingTo) {
		this.questionID = questionID;
		this.replyID = replyID;
		this.studentUserName = studentUserName;
		this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.questionReply = questionReplyText;
        this.replyingTo = replyingTo;
	}

	
	// Partial Constructor specific for Question Replies
	public Question(String reply, String replyingTo, User user) {
		this.user = user;
		this.questionReply = reply;
        this.creationTime = LocalDateTime.now();
        this.isResolved = false;
        this.replyID = -1; // default
        this.replyingTo = replyingTo;
	}
	
	// Partial Constructor specific for Question Replies
	public Question(int replyID, String studentFirstName, String studentLastName, String questionReplyText, String replyingTo) {
		this.replyID = replyID;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.questionReply = questionReplyText;
		this.replyingTo = replyingTo;
        //this.isResolved = false;
	}
	
	// Partial Constructor specific for Marking Question Resolved
	public Question(int QuestionID) {
        this.creationTime = LocalDateTime.now();
        this.isResolved = true;
        this.replyID = -1; // default
	}

	// Getters and Setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public int getReplyID() {
		return replyID;
	}
	
	public void setReplyID(int replyID) {
		this.replyID = replyID;
	}
	
	public String getQuestionReply() {
		return questionReply;
	}
	
	public void setReplyingTo(String replyingTo) {
		this.replyingTo = replyingTo;
	}
	
	public String getReplyingTo() {
		return replyingTo;
	}
	
	public void setQuestionReply(String questionReply) {
		this.questionReply = questionReply;
	}
	
	public String getStudentUserName() {
		return studentUserName;
	}
	
	public void setStudentUserName(String userName) {
		this.studentUserName = userName;
	}
	
	public String getStudentFirstName() {
		//studentFirstName = user.getFirstName();
		return studentFirstName;
	}
	
	public void setStudentFirstName(String firstName) {
		this.studentFirstName = firstName;
	}
	
	
	public String getStudentLastName() {
		//studentLastName = user.getLastName();
		return studentLastName;
	}
	
	public void setStudentLastName(String lastName) {
		this.studentLastName = lastName;
	}
	

	public boolean getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getQuestionBody() {
		return questionBody;
	}

	public void setQuestionBody(String questionBody) {
		this.questionBody = questionBody;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public int getPreviousQuestionID() {
		return previousQuestionID;
	}

	public void setPreviousQuestionID(int previousQuestionID) {
		this.previousQuestionID = previousQuestionID;
	}

	public int getQuestionID() {
		return questionID;
	}

	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Question question = (Question) o;
		return questionID == question.questionID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(replyID, questionReply);
	}
	
	@Override
	public String toString() {
	    return "Question ID: " + questionID + 
	    		",replyID: " + replyID +
	    		", UserName: " + studentUserName +
	            ", Author: " + studentFirstName + " " + studentLastName + 
	            ", Question Reply: " + questionReply +
	            ", Replying To: " + replyingTo;
	}

	public String checkQuestionTitleInput(String questionTitle) {
		// questionTitleError returns an empty string if no errors
		String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$";

		if (questionTitle == null || questionTitle.trim().isEmpty()) {
			questionTitleError = "Error: Question Title is empty!";
			return questionTitleError;
		}
		if (questionTitle.length() < 3) {
			questionTitleError = "Error: Question Title is too short (minimum 3 characters required)";
			return questionTitleError;
		}
		if (questionTitle.length() > 500) {
			questionTitleError = "Error: Question Title exceeds 500 characters. Use the question body for additional details.";
			return questionTitleError;
		}
		if (!questionTitle.matches(regexCheckNonControlChar)) {
			questionTitleError = "Error: Question Title contains non-printable or control characters.";
			return questionTitleError;
		}

		questionTitleError = "";
		return questionTitleError;
	}

	public String checkQuestionBodyInput(String questionBody) {
		// questionBodyError returns an empty string if no errors
		String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$";

		if (questionBody == null || questionBody.trim().isEmpty()) {
			questionBodyError = "Error: Question Body is empty!";
			return questionBodyError;
		}
		if (questionBody.length() > 5000000) {
			questionBodyError = "Error: Question Body exceeds the 5MB limit.";
			return questionBodyError;
		}
		if (!questionBody.matches(regexCheckNonControlChar)) {
			questionBodyError = "Error: Question Body contains non-printable or control characters.";
			return questionBodyError;
		}

		questionBodyError = "";
		return questionBodyError;
	}
	
	public String checkQuestionReplyInput(String questionReply) {
		String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$";
		questionReplyError = "";
		if (questionReply == null || questionReply.trim().isEmpty() || questionReply == "") {
			questionReplyError = "Error: Question request for clarification is empty!";
			return questionReplyError;
		}
		if (questionReply.length() > 5000000) {
			questionReplyError = "Error: Question request for clarification exceeds the 5MB limit.";
			return questionReplyError;
		}
		if (!questionReply.matches(regexCheckNonControlChar)) {
			questionReplyError = "Error: Question request for clarification contains non-printable or control characters.";
			return questionReplyError;
		}
		return questionReplyError;
	}
}