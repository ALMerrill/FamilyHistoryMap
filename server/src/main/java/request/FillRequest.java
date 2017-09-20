package request;

/** The FillRequest class holds the data from the JSON fill request body, the username to fill and the number of generations.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class FillRequest {
    private String userName;
    private int generations;

    public FillRequest(String username, int generations) {
        this.userName = username;
        this.generations = generations;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
