package result;

/** The FillResult class holds a success message if successful, or an error message if unsuccessful.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class FillResult {
    private String message;


    /**  Initializes a FillResult object with a successMessage with the number of people and events added, and an error message.
     *
     * @param numPeople         The number of people added to the database.
     * @param numEvents         The number of events added to the database.
     * @return      Success message if successful, error message otherwise.
     */
    public FillResult(int numPeople, int numEvents) {
        message = "Successfully added " + numPeople + " persons and " + numEvents + " events to the database.";
    }

    public FillResult(String message){
        this.message = message;
    }

    public String message() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
