package request;

 /** The LoginRequest class holds the data from the JSON login request body, a username and password.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class LoginRequest {
    private String userName;
    private String password;

    public LoginRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

