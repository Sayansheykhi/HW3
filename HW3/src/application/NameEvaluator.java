package application;


public class NameEvaluator {

	public static String NameEvaluatorErrorMessage = "";	// The error message text
	public static String NameEvaluatorInput = "";			// The input being processed
	public static int NameEvaluatorIndexofError = -1;		// The index of error location
	private static String inputLine = "";				// The input line
	private static char currentChar;					// The current character in the line
	private static int currentCharNdx;					// The index of the current character
	private static boolean running;						// The flag that specifies if the FSM is running
	private static int nameSize = 0;			// A numeric value may not exceed 32 characters
	
	// Private method to move to the next character within the limits of the input line
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}
	
	public static String checkForValidName(String input, String nameType) {
		// Check to ensure that there is input to process
		String firstNameArg = "firstNameType";
		String lastNameArg = "lastNameType";
		
		
		if(input.length() <= 0) {
			NameEvaluatorIndexofError = 0;	// Error at first character;
			if (nameType.equals(firstNameArg)) {
				NameEvaluatorErrorMessage = "Error Name: The first name field is empty!\n";
			}
			else if (nameType.equals(lastNameArg)) {
				NameEvaluatorErrorMessage = "Error Name: The last name field is empty!\n";
			}
			running = false;
			return NameEvaluatorErrorMessage;
		}
		else {
		
			inputLine = input;					// Save the reference to the input line as a global
			currentCharNdx = 0;					// The index of the current character
			currentChar = input.charAt(0);		// The current character from above indexed position
	
	
			NameEvaluatorInput = input;	// Save a copy of the input
			running = true;						// Start the loop
			
			
			// This is the place where semantic actions for a transition to the initial state occur
			
			nameSize = 0;					// Initialize the UserName size
	
			while (running) {
				if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
						(currentChar >= 'a' && currentChar <= 'z' )) {	// Check for a-z
					
					// Count the character 
					nameSize++;
				}
				else { // invalid character
					running = false;
					if (nameType.equals(firstNameArg)) {
						NameEvaluatorErrorMessage = "Error Name: A first name character may only contain the characters A-Z, a-z.\n";
					}
					
					else if (nameType.equals(lastNameArg)) {
						NameEvaluatorErrorMessage = "Error Name: A last name character may only contain the characters A-Z, a-z.\n";
					}
					return NameEvaluatorErrorMessage;
				}
				
				
				if (running) {
					moveToNextCharacter();
				}
				
			}
			System.out.println("The loop has ended.");
			NameEvaluatorIndexofError = currentCharNdx;	// Set index of a possible error;
			
			if (nameSize < 3 || nameSize > 32) {
				// firstName or lastName is too small
				if (nameType.equals(firstNameArg)) {
					NameEvaluatorErrorMessage = "Error Name: First name is too short or too long, must be between 3 and 32 characters.\n";
				}
				else if (nameType.equals(lastNameArg)) {
					NameEvaluatorErrorMessage = "Error Name: Last name is too short or too long, must be between 3 and 32 characters.\n";
				}
				return NameEvaluatorErrorMessage;
	
			}
			else {
				// first or last name is valid
				NameEvaluatorIndexofError = -1;
				NameEvaluatorErrorMessage = "";
				return NameEvaluatorErrorMessage;
			}
		}
	}
}



