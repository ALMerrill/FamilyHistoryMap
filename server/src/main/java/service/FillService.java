package service;
import java.sql.Connection;
import java.util.Random;
import java.util.Vector;

import handler.FillHandler;
import model.Event;
import model.Location;
import model.Locations;
import model.Person;
import model.User;
import result.FillResult;
import request.FillRequest;
import access.*;

/** The FillService class uses the Access classes and the Model classes to fill the database with generated family history data for a specific User.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class FillService {

    public FillService(){

    }

    /**  Populates the server's database with generated data for the specified user name.
     *
     * @param r          The fill request holding the username and number of generations. It must be a user already registered with the server.
     *                   If there is any data in the database already associated with the given user name, it is deleted.
     *                   The optional “generations” parameter lets the caller specify the number of generations of ancestors
                         to be generated, and must be a non-negative integer. The default is 4 generations.
     * @return           The fill result containing a success or error message.
     */
    public FillResult fill(FillRequest r){
        FillResult result = null;
        EventDao eDao = new EventDao();
        PersonDao pDao = new PersonDao();
        UserDao uDao = new UserDao();
        FillService service = new FillService();
        String username = r.getUsername();
        int generations = r.getGenerations();
        if(generations < 0) {
            result = new FillResult("Invalid generations parameter");
            return result;
        }
        int people = (int)Math.pow(2,generations + 1) - 1;      //formula for number of people using the number of generations created.
        int events = (4*people) - 2;        //4 events per person, except for the user, who isn't married and hasn't died.

        Access access = new Access();
        Connection connection = access.connect();
        boolean allDone = true;
        User cur = uDao.getUser(username, connection);
        if(cur != null) {
            if(!eDao.clearUserEvents(username, connection))
                allDone = false;
            if(!pDao.clearUserPeople(username, connection))
                allDone = false;
            Person replace = service.createPerson(cur.getPersonID(), cur.getGender(), username, cur.getLastName());
            if(!service.createAllGenerations(replace, generations, connection))
                allDone = false;
            result = new FillResult(people, events);
        }
        else {
            allDone = false;
            result = new FillResult("Username does not exist");
        }
        access.commitRoll(allDone);
        return result;
    }

    /**  Creates one generation of person data.
     *
     * @param previousGen          An array of the previous generation.
     * @param generationNum         The generation being made (1 = just parents, 2 = just grandparents, 3 = just great-grandparents, etc.)
     * @return                      An array of the people created
     */
    public Person[] createGeneration(Person[] previousGen, int generationNum) {
        //I need a way to finish all creation, and then adding to database, so that all parent IDs are correct. Make tree of created people?
        PersonDao pDao = new PersonDao();
        Person[] generation = new Person[(int)Math.pow(2,generationNum)];
        String descendant = previousGen[0].getDescendant();
        int prevGenIndex = -1;                  //current index of the person in the previous generation, to get the personID
        for(int i = 0; i < Math.pow(2,generationNum); i++){ //Each loop will be a new person
            char gender = (i % 2 == 0 ? 'm' : 'f');         //Every other person is male, starting with male
            String personID = pDao.generateString(9);
            if(i % 2 == 0) {
                prevGenIndex++;                //every two people, it is a new child in the previous generation, to get the personID from.
                generation[i] = createPerson(personID, gender, descendant, previousGen[prevGenIndex].getLastName());
            }
            else
                generation[i] = createPerson(personID, gender, descendant, null);
        }
        return generation;
    }

    /**  Creates and inserts X generations
     *
     * @param numGenerations         The number of generations being made (1 = with parents, 2 = with grandparents, 3 = with great-grandparents, etc.)
     * @param person             The person for which the generations are being created.
     * @return                      An array of the people created
     */
    public boolean createAllGenerations(Person person, int numGenerations, Connection connection){
        PersonDao pDao = new PersonDao();
        EventDao eDao = new EventDao();
        UserDao uDao = new UserDao();
        int size = (int)Math.pow(2,numGenerations + 1) - 1;
        Person[] allPeople = new Person[size];
        Person[] prevGen = {person};
        Person[] curGen = {person};
        allPeople[0] = person;

        for(int i = 1; i <= numGenerations; i++){    //each loop is a generation (1 - 4 for four generations)

            //prevGen = new Person[(int)Math.pow(2,i-1)]; //The size of the previous generation is 2^(cur generation - 1)
            prevGen = curGen;                   //make generation last created the previous generation.
            curGen = createGeneration(prevGen, i); //current generation now created.

            if(i != 0) {
                int j = (int)Math.pow(2,i) - 1;
                for (int k = 0; k < curGen.length; k++) { //adds current generation of people to allPeople array
                    allPeople[j] = curGen[k];
                    j++;
                }
            }

        }

        for(int j = 0; j < allPeople.length - (Math.pow(2,numGenerations)); j++){   //all people made except last generation
            allPeople[j].setFather(allPeople[(j * 2) + 1].getPerson()); //personID of person at index of father.
            allPeople[j].setMother(allPeople[(j * 2) + 2].getPerson()); //personID of perons at index of mother.
        }
        for(int j = 1; j < allPeople.length; j++) {   //everyone but the root person, who is not married
            if(j % 2 == 1 && j != 0)    //index of male that is not first person in "tree" array (who is not married)
                allPeople[j].setSpouse(allPeople[j + 1].getPerson());
            else if(j != 0)
                allPeople[j].setSpouse(allPeople[j - 1].getPerson());
        }
        for(int j = 0; j < allPeople.length; j++){
            pDao.insertPerson(allPeople[j], connection);
        }

        String[] firstEventIDs = {pDao.generateString(11), pDao.generateString(11), pDao.generateString(11), pDao.generateString(11)};
        Vector<String> birthEventIDs = new Vector<String>();    //holds all of the birth event IDs, for acceptable years on events.
        birthEventIDs.add(firstEventIDs[0]);
        createEvents(allPeople[0].getPerson(), firstEventIDs, allPeople[0].getDescendant(), 1, connection); //all registered users will be born in 2000
        for(int i = 1; i < allPeople.length; i++){ //each loop makes the events for one person.
            String[] eventIDs = {pDao.generateString(11), pDao.generateString(11), pDao.generateString(11), pDao.generateString(11)};
            birthEventIDs.add(eventIDs[0]);
            Person curPerson = allPeople[i];
            String childBirthID = null;
            if(i % 2 == 0)
                childBirthID = birthEventIDs.elementAt((i / 2) - 1); //eventID of the current person's child's birth.
            else
                childBirthID = birthEventIDs.elementAt(((i + 1) / 2) - 1);
            Event childBirth = null;
            try {
                childBirth = eDao.getEvent(childBirthID, connection);
            } catch (IDao.DatabaseException e) {
                e.printStackTrace();
                return false;
            }
            createEvents(curPerson.getPerson(), eventIDs, curPerson.getDescendant(), childBirth.getYear(), connection);
        }
        return true;
    }

    /**  Creates one person
     *
     * @param personID          ID of person to be created
     * @param gender            Gender of person to be created
     * @param descendant        Username of person to which the person created is related.
     * @return                      The person created
     */
    public Person createPerson(String personID, char gender, String descendant, String lastName) {
        FillHandler filler = new FillHandler();
        filler.initializeData();
        Random rng = new Random();
        String[] fNames = filler.getFnames().getData();
        String[] mNames = filler.getMnames().getData();
        String firstName = null;
        if(gender == 'm')
            firstName = mNames[rng.nextInt(mNames.length)];
        else if(gender == 'f')
            firstName = fNames[rng.nextInt(fNames.length)];
        String[] sNames = filler.getSnames().getData();
        if(lastName == null)
            lastName = sNames[rng.nextInt(fNames.length)];
        return new Person(personID, descendant, firstName, lastName, gender);
    }

    /**  Creates a set of events for one person and inserts them into the database
     *
     * @param personID          ID of person the events belong to
     * @param eventIDs          Randomly generated event IDs.
     * @param descendant        Username of person to which the person to which this event belongs is related.
     * @param birthOfChild      The birth year of the person's child.
     * @return                      The person created
     */
    public void createEvents(String personID, String[] eventIDs, String descendant, int birthOfChild, Connection connection) {
        FillHandler filler = new FillHandler();
        filler.initializeData();
        Location[] locations = filler.getLocations().getData();
        EventDao eDao = new EventDao();
        Random rng = new Random();
        String eventID = null;
        Location location = null;
        String eventType = null;
        int birthYear = 0;
        int baptismYear = 0;
        int marriageYear = 0;
        int deathYear = 0;

        //birth
        eventType = "Birth";
        location = locations[rng.nextInt(locations.length)];
        eventID = eventIDs[0];
        if(birthOfChild == 1)  //used to make the person of a user that just registered. All will be born in 2000.
            birthYear = 2000;
        else
            birthYear = birthOfChild - (rng.nextInt(11) + 25); //Parents birth is 25-35 years before child.  (# from 0-10 plus 25)
        eDao.insertEvent(new Event(eventID, descendant, personID, location.getLatitude(), location.getLongitude(),
                            location.getCountry(), location.getCity(), eventType, birthYear), connection);

        //baptism
        eventType = "Baptism";
        location = locations[rng.nextInt(locations.length)];
        eventID = eventIDs[1];
        baptismYear = rng.nextInt(11) + birthYear; //Baptism 0-10 years after birth.
        eDao.insertEvent(new Event(eventID, descendant, personID, location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), eventType, baptismYear), connection);

        //marriage
        if(birthYear != 2000){   //not currently registering person
            eventType = "Marriage";
            location = locations[rng.nextInt(locations.length)];
            eventID = eventIDs[2];
            marriageYear = birthOfChild - 2; //Marriage 2 years before child's birth
            eDao.insertEvent(new Event(eventID, descendant, personID, location.getLatitude(), location.getLongitude(),
                    location.getCountry(), location.getCity(), eventType, marriageYear), connection);
        }

        //death
        if(birthYear < 1980){   //old enough to be dead according to this program.....
            eventType = "Death";
            location = locations[rng.nextInt(locations.length)];
            eventID = eventIDs[3];
            deathYear = rng.nextInt(60) + 36 + birthYear; //Death 36-95 years after birth
            eDao.insertEvent(new Event(eventID, descendant, personID, location.getLatitude(), location.getLongitude(),
                    location.getCountry(), location.getCity(), eventType, deathYear), connection);
        }
    }


}
