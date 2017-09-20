package result;

/** The ClearResult class holds a success message if successful, or an error message if unsuccessful.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class ClearResult {
    private String message;

    public ClearResult(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
