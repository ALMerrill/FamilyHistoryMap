package result;

/** The LoadResult class holds a success message if successful, or an error message if unsuccessful.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class LoadResult {
    private String message;

    /**  Initializes a LoadResult object with a successMessage with the number of users, people and events added, and an error message.
     *
     * @param numUsers          The number of users added to the database.
     * @param numPeople         The number of people added to the database.
     * @param numEvents         The number of events added to the database.
     * @return      Success message if successful, error message otherwise.
     */
    public LoadResult(int numUsers, int numPeople, int numEvents) {
        String message = "Successfully added " + numUsers + " users, " + numPeople + " persons and " + numEvents + " events to the database.";
        this.message = message;
    }

    public LoadResult(String errorMessage){
        message = errorMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
