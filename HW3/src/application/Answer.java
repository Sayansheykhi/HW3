package application;

import databasePart1.DatabaseHelper;
import java.time.LocalDateTime;

public class Answer {
	private String studentUserName; // student Username in database
	private User student;
	private int answerID;
	private int questionID;
	private String questionTitle;
	private String studentFirstName;
	private String studentLastName;
	private boolean isResolved; // if 1, student marked that potential answer resolved issue, else 0
	private String answerText; // full text of input Answer
	private boolean isAnswerUnread; // if 0, potential answer is unread, else if 1, has been read
	private LocalDateTime creationTime; // date and time answer was created 
	public static String questionAnswerError = ""; // error if invalid input for answerText
	
	// Empty constructor
	public Answer() {
		//this.studentID = -1;
		this.studentFirstName = "";
		this.studentLastName = "";
		this.isResolved = false;
		this.answerText = "";
		this.isAnswerUnread = true;
		this.creationTime = LocalDateTime.now();
	}
	
	// Constructor
	public Answer(String studentUserName, String studentFirstName, String studentLastName, boolean isResolved, String answerText, boolean isAnswerUnread, LocalDateTime creationTime) {
		this.studentUserName = studentUserName;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.isResolved = isResolved;
		this.answerText = answerText;
		this.isAnswerUnread = isAnswerUnread;
		this.creationTime = creationTime;
	}
	
	// Partial Constructor for functions that pull answers from the database
	public Answer(String studentFirstName, String studentLastName, String answerText) {
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		
		this.isResolved = false;
		//this.studentID = -1;
		this.isAnswerUnread = false;
		this.creationTime = LocalDateTime.now();
	}
	
	public Answer(int answerID, int questionID, String studentUserName, String studentFirstName, String studentLastName, String answerText, boolean isAnswerUnread, boolean isResolved, LocalDateTime creationTime) {
		this.answerID = answerID;
		this.questionID = questionID;
		this.studentUserName = studentUserName;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		
		this.isResolved = isResolved;
		this.isAnswerUnread = isAnswerUnread;
		this.creationTime = creationTime;
	}
	
	public Answer(String studentUserName, String studentFirstName, String studentLastName, String answerText) {
		this.studentUserName = studentUserName;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		
		this.isResolved = false;
		//this.studentID = -1;
		this.isAnswerUnread = false;
		this.creationTime = LocalDateTime.now();
	}
	
	// Partial Constructor for functions that pull answers from the database
	public Answer(String studentFirstName, String studentLastName, String answerText, User user) {
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
		this.answerText = answerText;
		this.student = user;
		this.studentUserName = user.getUserName();
		
		this.isResolved = false;
		this.isAnswerUnread = true;
		this.creationTime = LocalDateTime.now();
	}
		
	
	// Getters and Setters
	public User getUser() {
		return student;
	}

	public void setUser(User user) {
		this.student = user;
	}
	
	
	public String getStudentID() {
		return studentUserName;
	}
	
	public void setStudentID(String studentUserName) {
		this.studentUserName = studentUserName;
	}
	
	public String getStudentFirstName() {
		//studentFirstName = student.getFirstName();
		return studentFirstName;
	}
	
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}
	
	public String getStudentUserName() {
		//studentUserName = student.getUserName();
		return studentUserName;
	}
	
	public void setStudentUserName(String studentUserName) {
		this.studentUserName = studentUserName;
	}
	
	
	public String getStudentLastName() {
		//studentLastName = student.getLastName();
		return studentLastName;
	}

	
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

	
	public boolean getIsResolved() {
		return this.isResolved;
	}
	
	public void setIsResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}
	
	public String getAnswerText() {
		return answerText;
	}
	
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
	public boolean getIsAnswerUnread() {
		return isAnswerUnread;
	}
	
	public void setIsAnswerUnread(boolean isAnswerUnread) {
		this.isAnswerUnread = isAnswerUnread;
	}
	
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}
	
	public int getAnswerID() {
		return answerID;
	}
	
	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	
	public void setQuestionTitleForAnswer(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	
	public String checkAnswerInput(String answerText) {
		// questionAnswerError returns "" if no errors
		String regexCheckNonControlChar = "^[\\x20-\\x7E\\t\\n\\r]+$"; 
		
		// check for HTML tags like </p><h1>Hello</h1>?
		
		if(answerText.equals("") || answerText == null) {
			questionAnswerError = "Error Answer: answer is empty!";
			return questionAnswerError;
		}
		if(answerText.length() > 5000000) { // length is more than 500 characters, additional text should be put into body
			questionAnswerError = "Error Answer: answer must have no more than 5,000,000 characters";
			return questionAnswerError;
		}
		if(!answerText.matches(regexCheckNonControlChar)) {
			questionAnswerError = "Error Answer: answer includes control characters or other non-printable characters";
			return questionAnswerError;
		}
		else {
			questionAnswerError = "";
		}
		return questionAnswerError;
	}
	
}