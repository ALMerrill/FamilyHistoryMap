package model;

/** The AuthToken class stores each AuthToken with the person to which it belongs, and a timestamp to measure when each AuthToken expires.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class AuthToken {
    private String token;
    private String personID;
    private String userName;
    private long timeStamp;

    public AuthToken(){}

    public AuthToken(String token, String personID, String userName, long timeStamp){
        this.token = token;
        this.personID = personID;
        this.userName = userName;
        this.timeStamp = timeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
