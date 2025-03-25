package application;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private boolean[] role;
    private String email;
    private String firstName;
    private String lastName;
  
    // Constructor to initialize a new User object with userName, password, and role.
    public User(String userName, String password, boolean[] role, String email, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Sets the role of the user.
    public void setRole(boolean[] role) {
    	this.role=role;
    }
    public void setPassword(String password) {
    	this.password = password;
    }
    
    public void printUser() {
    	System.out.println(this.userName + " " + this.firstName + " " + this.lastName + " " + this.email + " " + this.role);
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public boolean[] getRole() { return role; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
