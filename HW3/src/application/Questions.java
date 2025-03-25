package application;

import databasePart1.DatabaseHelper;
import java.util.ArrayList;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Questions {
	private ArrayList<Question> questions;
	private ArrayList<Question> replies;
	private final DatabaseHelper databaseHelper;
	private static User user;

	public Questions(DatabaseHelper databaseHelper, User user) {
		this.databaseHelper = databaseHelper;
		this.user = user;
	}

	// Add a new question with proper validation checks
	public int addQuestion(String questionTitle, String questionBody, Question question, User user) {
		int questionID = -1;
		try {
			questionID = databaseHelper.addQuestion(questionTitle, questionBody, question, user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questionID;
	}
	
	// Retrieve userName associated with a specific questionID
	public String getUserFromQuestionID(int questionID) {
		String UserName = databaseHelper.getUserFromQuestionID(questionID);
		return UserName;
	}

	// Retrieve all questions
	public ArrayList<Question> getAllQuestions(User user) {
		questions = databaseHelper.getAllQuestions(user);
		return questions;
	}
	
	// Retrieve all questions
	public ArrayList<Question> getAllReplies() {
		replies = databaseHelper.getAllReplies();
		return replies;
	}

	// Retrieve all unresolved questions along with their potential answers
	public ArrayList<Question> getUnresolvedQuestions() {
		questions = databaseHelper.getUnresolvedQuestions();
		return questions;
	}

	// Retrieve only answered questions
	public ArrayList<Question> getAnsweredQuestions() {
		// get questions which have answers
		questions = databaseHelper.getAnsweredQuestions();
		// get all replies attached to questions that have answers and combine them into a single ArrayList with their parent questions
		ArrayList<Question> repliesAndQuestions = databaseHelper.addQuestionRepliesToAnsweredQuestions(questions);
		// return the combined ArrayList for filtering
		return repliesAndQuestions;
	}

	// Retrieve only unanswered questions
	public ArrayList<Question> getUnansweredQuestions() {
		questions = databaseHelper.getUnansweredQuestions();
		return questions;
	}

	// Retrieve questions posted after a given timestamp
	public ArrayList<Question> getRecentQuestions(LocalDateTime time) {
		questions = databaseHelper.getRecentQuestions(time);
		return questions;
	}

	// Retrieve questions by a specific user
	public ArrayList<Question> getQuestionsByUserName(String username) {
		questions = databaseHelper.getQuestionsByUserName(username);
		return questions;
	}

	// Count unread potential answers for each unresolved question
	public int countUnreadPotentialAnswers(int questionID) {
		return databaseHelper.countUnreadPotentialAnswers(questionID);
	}

	
	/**
     * Commented out as not currently used in StudentQuestionAnswers.java
     
	// Retrieve only resolved answers and link them to their respective questions
	public ArrayList<Question> onlyResolvedAnswers() {
		return databaseHelper.getResolvedAnswersWithQuestions();
	}
	
	*/

	// Create a new question based on an old question
	public int createNewQuestionfromOld(String newQuestionTitle, String newQuestionBody, Question newQuestion, User user, int oldQuestionID) {
		int questionID = databaseHelper.createNewQuestionfromOld(newQuestionTitle, newQuestionBody, newQuestion, user, oldQuestionID);
		return questionID;
	}

	// Soft delete a question (marks it as deleted instead of removing it)
	public Question markQuestionDeleted(int questionID, Question questionToDelete) {
		databaseHelper.markQuestionDeleted(questionID); 
		questionToDelete = new Question("Deleted Student User Name", "Deleted Student First Name", "Deleted Student Last Name", "Deleted Question Title", "Deleted Question Body");
		questionToDelete.setQuestionID(questionID);
		questionToDelete.setReplyID(-1);
		return questionToDelete;
	}

	// Hard delete a question (completely removes it from the database)
	public void deleteQuestion(int questionID) {
		databaseHelper.deleteQuestion(questionID);
	}
	
	public void deleteRepliesForQuestion(int questionID) {
		databaseHelper.deleteRepliesForQuestion(questionID);
	}

	// Retrieve a question by its ID
	public Question getQuestionByID(int questionID) {
		return databaseHelper.getQuestionByID(questionID);
	}

	// Edit a question's title and body
	public void editQuestion(String modifiedQuestionTitle, String modifiedQuestionBody, int questionID) {
		databaseHelper.editQuestion(modifiedQuestionTitle, modifiedQuestionBody, questionID);
	}
	
	public int addReply(String replyText, int parentQuestionID, Question questionReply, User student, String replyingTo) {
        int replyID = databaseHelper.addReply(replyText, parentQuestionID, questionReply, student, replyingTo);
        System.out.printf("Reply ID is %d\n", replyID);
        return replyID;
    }
}