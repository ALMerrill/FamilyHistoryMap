package result;
import model.AuthToken;

/** The RegisterResult class holds the data from the JSON register result body when successful, and AuthToken, username and person ID, or an error message if unsuccessful.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class RegisterResult {
    private String token;
    private String username;
    private String personID;
    private String errorMessage;

    public RegisterResult(String token, String username, String personID) {
        this.token = token;
        this.username = username;
        this.personID = personID;
    }

    public RegisterResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
