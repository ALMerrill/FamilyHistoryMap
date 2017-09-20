package model;

/** The Person class stores each Person's information. Each user has a person object, as well as multiple person objects connected to them to represent their ancestors.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class Person {
    private String personID;
    private String descendant;
    private String firstName;
    private String lastName;
    private char gender;
    private String father;
    private String mother;
    private String spouse;

    public Person(String person, String descendant, String firstName, String lastName, char gender, String father, String mother, String spouse) {
        this.personID = person;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public Person(String person, String descendant, String firstName, String lastName, char gender) {
        this.personID = person;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getPerson() {
        return personID;
    }

    public void setPerson(String person) {
        this.personID = person;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}
