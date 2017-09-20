package service;

import java.sql.Connection;

import access.Access;
import access.AuthTokenDao;
import access.EventDao;
import access.IDao;
import access.PersonDao;
import access.UserDao;
import model.AuthToken;
import model.Person;
import model.User;
import result.RegisterResult;
import request.RegisterRequest;

/** The RegisterService class uses the Access classes and the Model classes to register a User for Family Map and give them an AuthToken
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class RegisterService {
    private Person person;

    public RegisterService(){

    }

    /** Utilizes the UserDao to register the user.
     *
     * @param req                     The register request containing all registration information.
     * @return			            The register result containing the AuthToken, username and person ID.
     */
    public RegisterResult register(RegisterRequest req){
        RegisterResult result = null;
        FillService service = new FillService();
        User temp = new User(req.getUserName(), req.getPassword(), req.getEmail(), req.getFirstName(), req.getLastName(), req.getGender(), null);
        UserDao uDao = new UserDao();
        AuthTokenDao aTDao = new AuthTokenDao();
        PersonDao pDao = new PersonDao();
        EventDao eDao = new EventDao();
        AuthToken token = null;
        Access access = new Access();
        Connection connection = access.connect();
        boolean allDone = true;
        try {
            token = uDao.register(temp, connection);
            if(token == null) {
                allDone = false;
                access.commitRoll(allDone);
                result = new RegisterResult("Username already taken");
                return result;
            }
            if(!aTDao.insertAuthToken(token, connection))
                allDone = false;
            person = service.createPerson(temp.getPersonID(), temp.getGender(), temp.getUserName(), temp.getLastName());
            person.setFirstName(temp.getFirstName());
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
            result = new RegisterResult("Database error");
            return result;
        }
        result = new RegisterResult(token.getToken(),token.getUserName(), token.getPersonID());
        access.commitRoll(allDone);
        return result;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
