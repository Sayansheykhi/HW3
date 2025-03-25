# HW3 Automated Testing & Documentation

This repository contains my HW3 assignment submission for CSE 360. The project demonstrates automated testing of methods from our TP2 submission along with professional Javadoc documentation.

## Overview

The goal of this project is to:
- Ensure every team member is familiar with automated testing (JUnit is optional).
- Validate and test methods from the existing TP2 submission, including:
  - `Question.checkQuestionReplyInput()`
  - `Question.checkQuestionTitleInput()`
  - `Question.checkQuestionBodyInput()`
  - `Answer.checkAnswerInput()`
  - `Answers.addAnswer()`
- Generate professional-looking Javadoc documentation inspired by Oracle's Java API.
- Produce a screencast showing the tests running and explaining the implementation.

## Project Structure


## Automated Tests

The main class, `HW3.java`, executes automated tests for the following:
1. **Question.checkQuestionReplyInput()**  
   - Valid reply input returns no error.
   - Empty input or input with control characters returns appropriate error messages.
2. **Question.checkQuestionTitleInput()**  
   - Valid title input returns no error.
   - Empty, too short, too long, or invalid characters result in error messages.
3. **Question.checkQuestionBodyInput()**  
   - Valid body input returns no error.
   - Empty or malformed inputs are correctly flagged.
4. **Answer.checkAnswerInput()**  
   - Valid answer text passes the check.
   - Empty or invalid text returns the corresponding error.
5. **Answers.addAnswer()**  
   - Using a dummy database helper, adding a valid answer returns a positive answer ID.

Each test outputs detailed information, including the test case number, input, expected result, and actual outcome.

## Javadoc Documentation

I have updated the internal documentation to produce professional Javadoc output. My Javadoc inspiration is taken from [Oracle’s Java Collections Framework documentation](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html).

To generate the Javadoc, run the following command in the root directory:
```bash
javadoc -d docs application/*.java
