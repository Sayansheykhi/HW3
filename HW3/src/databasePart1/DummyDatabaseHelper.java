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
 * DummyDatabaseHelper simulates database operations for testing purposes.
 * It extends DatabaseHelper and overrides the addAnswers method to return a dummy answer ID.
 */
public class DummyDatabaseHelper extends DatabaseHelper {
    
    /**
     * Overrides the addAnswers method to simulate a successful database insert.
     *
     * @param answerText The text of the answer.
     * @param answer The Answer object.
     * @param student The User posting the answer.
     * @param questionID The question ID to which this answer belongs.
     * @return A dummy positive answer ID (e.g., 1).
     */
    @Override
    public int addAnswers(String answerText, Answer answer, User student, int questionID) {
        // Simply simulate a successful insert by returning a dummy answer ID.
        return 1;
    }
}
