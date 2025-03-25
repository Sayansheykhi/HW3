package application;

import databasePart1.DatabaseHelper;
import java.util.ArrayList;

public class Answers {
    // Holds all Answer objects, loaded from DB
    private ArrayList<Answer> answers;

    // Reference to the DB helper
    private final DatabaseHelper databaseHelper;

    // Replaces the old `Student student` reference
    private User user;

   
    public Answers(DatabaseHelper databaseHelper, User user) {
        this.databaseHelper = databaseHelper;
        this.user = user;
    }

    /**
     * Create (propose) a new answer.
     * @param answerText    The text content of the new answer
     * @param answer        An Answer object containing details
     * @param user          The user who is posting the answer
     * @param questionID    The question to which this answer belongs
     * @return              The newly generated answerID
     */
    public int addAnswer(String answerText, Answer answer, User user, int questionID) {
        return databaseHelper.addAnswers(answerText, answer, user, questionID);
    }

   
    public ArrayList<Answer> getAllAnswers(User user) {
        answers = databaseHelper.getAllAnswers(user);
        return answers;
    }

   
    public ArrayList<Answer> getAnswersByQuestionID(int questionID) {
        answers = databaseHelper.getAnswersByQuestionID(questionID);
        return answers;
    }

   
    public ArrayList<Answer> getUnreadAnswers() {
        answers = databaseHelper.getUnreadAnswers();
        return answers;
    }

  
    public ArrayList<Answer> getAnswersUnresolvedQuestions() {
        answers = databaseHelper.getAnswersUnresolvedQuestions();
        return answers;
    }

   
    public ArrayList<Answer> getResolvedAnswers() {
        answers = databaseHelper.getResolvedAnswers();
        return answers;
    }
    
    public ArrayList<Answer> getUnresolvedAnswers() {
        answers = databaseHelper.getUnresolvedAnswers();
        return answers;
    }

    
    public Answer getAnswerByID(int answerID) {
        return databaseHelper.getAnswerByID(answerID);
    }

   
    public void deleteAnswer(int answerID) {
        databaseHelper.deleteAnswer(answerID);
    }

    
    public void editAnswer(String modifiedAnswer, int answerID) {
        databaseHelper.editAnswer(modifiedAnswer, answerID);
    }

    
    public int getQuestionIDForAnswer(int answerID) {
        return databaseHelper.getQuestionIDForAnswer(answerID);
    }

    
    public boolean markAnswerResolved(int answerID, int questionIDForAnswer, Answer answer, Question question) {
        // 1) Mark the answer as resolved in the DB
    	boolean wereRowsAffected;
    	boolean wereRowsAffectedQuestion;
        wereRowsAffected = databaseHelper.markAnswerResolved(answerID);
        
        // 2) Mark the question as resolved in the DB
        wereRowsAffectedQuestion = databaseHelper.markQuestionResolved(questionIDForAnswer);
        return wereRowsAffectedQuestion;
    } 
}