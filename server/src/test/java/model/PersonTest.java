package model;
import org.junit.Test;

import access.PersonDao;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class PersonTest {

    @Test
    public void testGetsAndSets(){
        Person person = new Person("", "", "", "" , 'm', "" , "", "");
        person.setPerson("person");
        assertEquals("person", person.getPerson());
        person.setDescendant("descendant");
        assertEquals("descendant", person.getDescendant());
        person.setFirstName("first");
        assertEquals("first", person.getFirstName());
        person.setLastName("last");
        assertEquals("last", person.getLastName());
        person.setGender('f');
        assertEquals('f', person.getGender());
        person.setFather("father");
        assertEquals("father", person.getFather());
        person.setMother("mother");
        assertEquals("mother", person.getMother());
    }
}
