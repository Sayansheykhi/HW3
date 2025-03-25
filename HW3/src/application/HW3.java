package application;

import databasePart1.DummyDatabaseHelper;

/**
 * ValidationTestingAutomation performs automated tests for the validation methods
 * in the Question and Answer classes, and for the addAnswer method in Answers.
 *
 * <p>This class tests:
 * <ol>
 *   <li>Question.checkQuestionReplyInput()</li>
 *   <li>Question.checkQuestionTitleInput()</li>
 *   <li>Question.checkQuestionBodyInput()</li>
 *   <li>Answer.checkAnswerInput()</li>
 *   <li>Answers.addAnswer()</li>
 * </ol>
 *
 * <p>Each test case displays the input, the expected result (valid or invalid), and the
 * actual output. A summary of the total passed and failed tests is then printed.</p>
 *
 * <p>Author: Sajjad Sheykhi</p>
 */
public class HW3{
    
    static int numPassed = 0;  // Counter for passed tests
    static int numFailed = 0;  // Counter for failed tests

    public static void main(String[] args) {
        System.out.println("______________________________________");
        System.out.println("Automated Testing for Validation Methods");
        System.out.println("______________________________________\n");

        // Test cases for Question.checkQuestionReplyInput
        performTestCaseForQuestionReply(1, "This is a valid reply.", true);
        performTestCaseForQuestionReply(2, "", false);
        performTestCaseForQuestionReply(3, "Invalid\u0001Reply", false);

        // Test cases for Question.checkQuestionTitleInput
        performTestCaseForQuestionTitle(1, "Valid Question Title", true);
        performTestCaseForQuestionTitle(2, "", false);
        performTestCaseForQuestionTitle(3, "Hi", false);
        // Test for too long title: 501 characters
        StringBuilder longTitleBuilder = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            longTitleBuilder.append("a");
        }
        performTestCaseForQuestionTitle(4, longTitleBuilder.toString(), false);
        performTestCaseForQuestionTitle(5, "Title\u0002Test", false);

        // Test cases for Question.checkQuestionBodyInput
        performTestCaseForQuestionBody(1, "This is a valid question body.", true);
        performTestCaseForQuestionBody(2, "", false);
        performTestCaseForQuestionBody(3, "Body with control\u0003char", false);

        // Test cases for Answer.checkAnswerInput
        performTestCaseForAnswerInput(1, "This is a valid answer.", true);
        performTestCaseForAnswerInput(2, "", false);
        performTestCaseForAnswerInput(3, "Answer with control\u0004char", false);

        // Test case for Answers.addAnswer using a dummy database helper
        performTestCaseForAddAnswer(1, "This is a test answer", 123, true);

        // Report summary
        System.out.println("______________________________________");
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
        System.out.println("______________________________________");
    }
    
    /**
     * Tests the Question.checkQuestionReplyInput method.
     *
     *@param testCase the test case number
     *@param inputText the reply input to test
     *@param expectedValid true if the input should be considered valid; false otherwise
     */
    private static void performTestCaseForQuestionReply(int testCase, String inputText, boolean expectedValid) {
        System.out.println("______________________________________");
        System.out.println("\nTest case " + testCase + " for Question.checkQuestionReplyInput:");
        System.out.println("Input: \"" + inputText + "\"");
        Question question = new Question();
        String result = question.checkQuestionReplyInput(inputText);
        boolean isValid = result.isEmpty();  // valid if no error message
        if (isValid == expectedValid) {
            System.out.println("***Success***: Input \"" + inputText + "\" is " +
                    (isValid ? "valid" : "invalid") + " as expected.");
            if (!isValid) {
                System.out.println("Error message: " + result);
            }
            numPassed++;
        } else {
            System.out.println("***Failure***: Input \"" + inputText + "\" is " +
                    (isValid ? "valid" : "invalid") + " but expected " +
                    (expectedValid ? "valid" : "invalid") + ".");
            if (!result.isEmpty()) {
                System.out.println("Error message: " + result);
            }
            numFailed++;
        }
    }
    
    /**
     * Tests the Question.checkQuestionTitleInput method.
     *
     * @param testCase the test case number
     * @param inputText the title input to test
     * @param expectedValid true if the input should be considered valid; false otherwise
     */
    private static void performTestCaseForQuestionTitle(int testCase, String inputText, boolean expectedValid) {
        System.out.println("______________________________________");
        System.out.println("\nTest case " + testCase + " for Question.checkQuestionTitleInput:");
       
        String displayInput = (inputText.length() > 50) ? inputText.substring(0, 50) + "..." : inputText;
        System.out.println("Input: \"" + displayInput + "\"");
        Question question = new Question();
        String result = question.checkQuestionTitleInput(inputText);
        boolean isValid = result.isEmpty();
        if (isValid == expectedValid) {
            System.out.println("***Success***: Input is " + (isValid ? "valid" : "invalid") + " as expected.");
            if (!isValid) {
                System.out.println("Error message: " + result);
            }
            numPassed++;
        } else {
            System.out.println("***Failure***: Input is " + (isValid ? "valid" : "invalid") +
                    " but expected " + (expectedValid ? "valid" : "invalid") + ".");
            if (!result.isEmpty()) {
                System.out.println("Error message: " + result);
            }
            numFailed++;
        }
    }
    
    /**
     * Tests the Question.checkQuestionBodyInput method.
     *
     * @param testCase the test case number
     * @param inputText the body input to test
     * @paramexpectedValid true if the input should be considered valid; false otherwise
     */
    private static void performTestCaseForQuestionBody(int testCase, String inputText, boolean expectedValid) {
        System.out.println("______________________________________");
        System.out.println("\nTest case " + testCase + " for Question.checkQuestionBodyInput:");
        String displayInput = (inputText.length() > 50) ? inputText.substring(0, 50) + "..." : inputText;
        System.out.println("Input: \"" + displayInput + "\"");
        Question question = new Question();
        String result = question.checkQuestionBodyInput(inputText);
        boolean isValid = result.isEmpty();
        if (isValid == expectedValid) {
            System.out.println("***Success***: Input is " + (isValid ? "valid" : "invalid") + " as expected.");
            if (!isValid) {
                System.out.println("Error message: " + result);
            }
            numPassed++;
        } else {
            System.out.println("***Failure***: Input is " + (isValid ? "valid" : "invalid") +
                    " but expected " + (expectedValid ? "valid" : "invalid") + ".");
            if (!result.isEmpty()) {
                System.out.println("Error message: " + result);
            }
            numFailed++;
        }
    }
    
    /**
     * Tests the Answer.checkAnswerInput method.
     *
     * @param testCase the test case number
     * @param inputText the answer input to test
     * @param expectedValid true if the input should be considered valid; false otherwise
     */
    private static void performTestCaseForAnswerInput(int testCase, String inputText, boolean expectedValid) {
        System.out.println("______________________________________");
        System.out.println("\nTest case " + testCase + " for Answer.checkAnswerInput:");
        String displayInput = (inputText.length() > 50) ? inputText.substring(0, 50) + "..." : inputText;
        System.out.println("Input: \"" + displayInput + "\"");
        Answer answer = new Answer();
        String result = answer.checkAnswerInput(inputText);
        boolean isValid = result.isEmpty();
        if (isValid == expectedValid) {
            System.out.println("***Success***: Input is " + (isValid ? "valid" : "invalid") + " as expected.");
            if (!isValid) {
                System.out.println("Error message: " + result);
            }
            numPassed++;
        } else {
            System.out.println("***Failure***: Input is " + (isValid ? "valid" : "invalid") +
                    " but expected " + (expectedValid ? "valid" : "invalid") + ".");
            if (!result.isEmpty()) {
                System.out.println("Error message: " + result);
            }
            numFailed++;
        }
    }
    
    /**
     * Tests the Answers.addAnswer method.
     *
     * @param testCase the test case number
     * @param answerText the answer text to add
     * @param questionID the question ID associated with the answer
     * @param expectedValid true if the operation should succeed; false otherwise
     */
    private static void performTestCaseForAddAnswer(int testCase, String answerText, int questionID, boolean expectedValid) {
        System.out.println("______________________________________");
        System.out.println("\nTest case " + testCase + " for Answers.addAnswer:");
        System.out.println("Answer Text: \"" + answerText + "\", Question ID: " + questionID);
        
        
        databasePart1.DummyDatabaseHelper dummyDBHelper = new databasePart1.DummyDatabaseHelper();
        
        User dummyUser = new User("dummyUser", "dummyPass", new boolean[]{true, false, false, false, false},
                                  "dummy@example.com", "Dummy", "User");
        
        Answers answers = new Answers(dummyDBHelper, dummyUser);
        
        Answer dummyAnswer = new Answer("dummyUser", "Dummy", "User", answerText);
        
        int newAnswerId = answers.addAnswer(answerText, dummyAnswer, dummyUser, questionID);
        boolean success = newAnswerId > 0;
        if (success == expectedValid) {
            System.out.println("***Success***: New Answer ID is " + newAnswerId + ", as expected.");
            numPassed++;
        } else {
            System.out.println("***Failure***: New Answer ID is " + newAnswerId +
                    ", but expected a positive ID for a valid answer.");
            numFailed++;
        }
    }
}
