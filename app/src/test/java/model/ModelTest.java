package model;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 6/13/17.
 */

public class ModelTest {
    private Model model;

    @Before
    public void build() {
        model = Model.instance();

    }

    @After
    public void destroy() {

    }

    @Test
    public void testFillPolylines() {
        Person Criselda = new Person("gqg56q4lp", "user", "Criselda", "Marko", 'f', "z1x2a4qcm", "x26k7tl98", "nk07hsvwy");
        Event CriseldaBirth = new Event("02vlrwirlfs", "user", "gqg56q4lp", "22.1667", "113.55", "China", "Macau", "Birth", 1903);
        Event CriseldaBaptism = new Event("3obu0xy2ujv", "user", "gqg56q4lp", "29.5667", "106.5667", "China", "Chongqing", "Baptism", 1906);
        Event CriseldaMarriage = new Event("kl940r1tbon", "user", "gqg56q4lp", "52.1", "5.1167", "Netherlands", "Utrecht", "Marriage", 1933);
        Event CriseldaDeath = new Event("w7yhtm5vw86", "user", "gqg56q4lp", "7.7167", "81.7", "Sri Lanka", "Batticaloa", "Death", 1945);
        Person Houston = new Person("0inyhmtph", "user", "Houston", "Mcdougald", 'm', null, null, "xw2840xzx");
        Event HoustonBirth = new Event("rbwtdgflzr1", "user", "0inyhmtph", "12.05", "-60.25", "Grenada", "St. George's", "Birth", 1888);
        Event HoustonBaptism = new Event("1vm0jd8gscm", "user", "0inyhmtph", "46.3", "-78.55", "Canada", "North Bay", "Baptism", 0);
        Event HoustonMarriage = new Event("0zbdfjtnjln", "user", "0inyhmtph", "-5.8333", "35.75", "Tanzania", "Dodoma", "Marriage", 1913);
        Event HoustonDeath = new Event("cfgsyix7uwy", "user", "0inyhmtph", "48.1333", "11.5667", "Germany", "Munich", "Death", 1936);
        Model.instance().addPerson(Criselda);
        Model.instance().addPerson(Houston);
        Model.instance().addEvent(CriseldaBirth);
        Model.instance().addEvent(CriseldaBaptism);
        Model.instance().addEvent(CriseldaMarriage);
        Model.instance().addEvent(CriseldaDeath);
        Model.instance().addEvent(HoustonBirth);
        Model.instance().addEvent(HoustonBaptism);
        Model.instance().addEvent(HoustonMarriage);
        Model.instance().addEvent(HoustonDeath);


    }

    @Test
    public void testGettersSetters() {
        Model model = Model.instance();
        //host
        String host = "host";
        model.setHost(host);
        assertEquals("host", model.getHost());
        //port
        int port = 8080;
        model.setPort(port);
        assertEquals(8080, model.getPort());
        //authToken
        String authToken = "token";
        model.setAuthToken(authToken);
        assertEquals("token", model.getAuthToken());
        //user
        Person person = new Person("1", "", "", "", 'm');
        model.setUser(person);
        assertEquals("1", model.getUser().getPerson());
        assertEquals(true, model.hasUser());
        //maternalEvents and paternalEvents
        Event event = new Event("1", "", "", "", "", "", "", "", 0);
        Event event2 = new Event("2", "", "", "", "", "", "", "", 0);
        List<Event> maternalEvents = new ArrayList<>();
        List<Event> paternalEvents = new ArrayList<>();
        maternalEvents.add(event); maternalEvents.add(event2);
        paternalEvents.add(event); paternalEvents.add(event2);
        model.setMaternalEvents(maternalEvents);
        model.setPaternalEvents(paternalEvents);
        assertEquals("1", model.getMaternalEvents().get(0).getEventID());
        assertEquals("2", model.getPaternalEvents().get(1).getEventID());
        //people
        Person person2 = new Person("2", "", "", "" , 'm');
        List<Person> people = new ArrayList<>();
        people.add(person); people.add(person2);
        Map<String, Person> personMap = new HashMap<String, Person>();
        for(int i = 0; i < people.size(); i++){
            personMap.put(people.get(i).getPerson(), people.get(i));
        }
        model.setPeople(personMap);
        assertEquals("1", model.getPeople().get("1").getPerson());
        //events
        List<Event> events = new ArrayList<>();
        events.add(event); events.add(event2);
        Map<String, Event> eventMap = new HashMap<String, Event>();
        for(int i = 0; i < events.size(); i++){
            eventMap.put(events.get(i).getEventID(), events.get(i));
        }
        model.setEvents(eventMap);
        assertEquals("1", model.getEvents().get("1").getEventID());
        //curEvents
        Set<Event> curEvents = new HashSet<>();
        curEvents.add(event);
        model.setCurEvents(curEvents);
        assertEquals(curEvents, model.getCurEvents());
        //eventsByType
        Map<String, List<Event>> eventsByType = new HashMap<>();
        List<Event> typeEvents = new ArrayList<>();
        typeEvents.add(event);
        eventsByType.put("type1", typeEvents);
        model.setEventsByType(eventsByType);
        assertEquals(eventsByType, model.getEventsByType());
        //eventTypes
        Set<String> types = new HashSet<>();
        types.add("Birth"); types.add("Death");
        model.setEventTypes(types);
        assertEquals(types, model.getEventTypes());
        //currentEvent
        model.setCurrentEvent(event);
        assertEquals(event, model.getCurrentEvent());
        //currentPersonChildren
//        Person father = new Person("father", "", "", "", 'm', "", "", "spouse");
//        Person spouse = new Person("spouse", "", "", "", 'm', "", "", "father");
        Person child1 = new Person("child1", "", "", "", 'm', "father", "", "");
        Person child2 = new Person("child2", "", "", "", 'm', "father", "", "");
        List<Person> children = new ArrayList<>();
        children.add(child1); children.add(child2);
        model.setCurrentPersonChildren(children);
        assertEquals(children, model.getCurrentPersonChildren());
        //currentPerson
        model.setCurrentPerson(person);
        assertEquals(person, model.getCurrentPerson());
        //addPerson, getPersonWithID
        model.addPerson(child1);
        assertEquals(child1, model.getPersonWithID("child1"));
        //addEvent, getEventWithID
        Event event3 = new Event("3", "", "", "", "", "", "", "", 0);
        model.addEvent(event3);
        assertEquals(event3, model.getEventWithID("3"));
        //Filters and FilterTypes
        List<String> filters = new ArrayList<>();
        filters.add("filter1"); filters.add("filter2");
        model.setFilters(filters);
        model.setFilterTypes(filters);
        assertEquals(filters, model.getFilters());
        assertEquals(filters, model.getFilterTypes());
        //eventFilters, sideFilters, genderFilters
        model.setEventFilters(types);
        model.setSideFilters(types);
        model.setGenderFilters(types);
        assertEquals(types, model.getEventFilters());
        assertEquals(types, model.getSideFilters());
        assertEquals(types, model.getGenderFilters());
        //mapType
        String type = "terrain";
        model.setMapType(type);
        assertEquals(type, model.getMapType());
        //switches
        boolean witch = true;
        model.setLifeLinesSwitch(witch);
        model.setFamilyLinesSwitch(witch);
        model.setSpouseLinesSwitch(witch);
        assertEquals(witch, model.getLifeLinesSwitch());
        assertEquals(witch, model.getFamilyLinesSwitch());
        assertEquals(witch, model.getSpouseLinesSwitch());
        //line colors
        model.setLifeLinesColor(1);
        model.setFamilyLinesColor(2);
        model.setSpouseLinesColor(3);
        assertEquals(1, model.getLifeLinesColor());
        assertEquals(2, model.getFamilyLinesColor());
        assertEquals(3, model.getSpouseLinesColor());
        //line color IDs
        model.setLifeLinesColorID(4);
        model.setFamilyLinesColorID(5);
        model.setSpouseLinesColorID(6);
        assertEquals(4, model.getLifeLinesColorID());
        assertEquals(5, model.getFamilyLinesColorID());
        assertEquals(6, model.getSpouseLinesColorID());
        //mapTypeID
        model.setMapTypeID(7);
        assertEquals(7, model.getMapTypeID());
        //markerColors
        float[] colors = {BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_BLUE};
        model.setMarkerColors(colors);
        assertEquals(colors, model.getMarkerColors());
    }

    @Test
    public void testFillers(){
        Model model = Model.instance();
        //TODO:fill events first
        model.fillEventTypes();
        //TODO: fill event types first
        model.fillMarkerColorByType();
        float color = model.getMarkerColorByEventType().get("Birth");
        assert(color == BitmapDescriptorFactory.HUE_RED);


        Person father = new Person("father", "", "", "", 'm', "", "", "spouse");
        Person spouse = new Person("spouse", "", "", "", 'm', "", "", "father");
        Person child1 = new Person("child1", "", "", "", 'm', "father", "", "");
        Person child2 = new Person("child2", "", "", "", 'm', "father", "", "");
    }

    @Test
    public void testOrderPersonEvents() {
        Person person = new Person("person", "", "", "", 'm');
        Event event = new Event("1", "", "person", "", "", "", "", "a", 1900); //same year, a should be first
        Event event2 = new Event("2", "", "person", "", "", "", "", "d", 1900);
        Event event3 = new Event("3", "", "person", "", "", "", "", "c", 1600);
        Event event4 = new Event("4", "", "person", "", "", "", "", "b", 1800); //b comes first alphabetically, but happened after, should show up after.
        Event event5 = new Event("5", "", "person", "", "", "", "", "death", 2000);
        Event event6 = new Event("6", "", "person", "", "", "", "", "birth", 0); //birth without a year, should still come first
        Event event7 = new Event("7", "", "person", "", "", "", "", "a", 0); //same alphabetically as a, but has no year, so it should come right before death.
        //order should be: event6, event3, event4, event, event2, event7, event5
        Set<Event> events = new HashSet<>();
        events.add(event); events.add(event2); events.add(event3); events.add(event4); events.add(event5); events.add(event6); events.add(event7);
        model.setCurEvents(events);
        Map<String, Event> eventsMap = new HashMap<>();
        eventsMap.put("1", event); eventsMap.put("2", event2); eventsMap.put("3", event3); eventsMap.put("4", event4);
        eventsMap.put("5", event5); eventsMap.put("6", event6); eventsMap.put("7", event7);
        model.setEvents(eventsMap);
        List<String> orderedEventIDs = model.orderPersonEvents(person);
        assertEquals(7, orderedEventIDs.size());
        assertEquals("6", orderedEventIDs.get(0));
        assertEquals("3", orderedEventIDs.get(1));
        assertEquals("4", orderedEventIDs.get(2));
        assertEquals("1", orderedEventIDs.get(3));
        assertEquals("2", orderedEventIDs.get(4));
        assertEquals("7", orderedEventIDs.get(5));
        assertEquals("5", orderedEventIDs.get(6));
    }

    @Test
    public void testSearch() {
        //first names allow for searching and getting certain people back ("a" will return just person, "g" will return all of them)
        //last names give each person a unique letter that will just return them.
        Person person = new Person("1", "", "abcdefg", "n", 'm');
        Person person2 = new Person("2", "", "bcdefgh", "o", 'm');
        Person person3 = new Person("3", "", "cdefghi", "p", 'm');
        Person person4 = new Person("4", "", "defghij", "q", 'm');
        Person person5 = new Person("5", "", "efghijk", "r", 'm');
        Person person6 = new Person("6", "", "fghijkl", "s", 'm');
        Person person7 = new Person("7", "", "ghijklm", "t", 'm');
        //country acts as first name above
        //city acts as last name above
        Event event = new Event("1", "", "1", "", "", "0123456", "a", "a", 1);
        Event event2 = new Event("2", "", "2", "", "", "1234567", "b", "b", 2);
        Event event3 = new Event("3", "", "3", "", "", "2345678", "c", "c", 3);
        Event event4 = new Event("4", "", "4", "", "", "3456789", "d", "d", 4);
        Event event5 = new Event("5", "", "5", "", "", "456789u", "e", "e", 5);
        Event event6 = new Event("6", "", "6", "", "", "56789vw", "f", "f", 6);
        Event event7 = new Event("7", "", "7", "", "", "6789xyz", "g", "g", 7);
        Map<String, Person> personMap = new HashMap<>();
        personMap.put("1", person); personMap.put("2", person2); personMap.put("3", person3); personMap.put("4", person4);
        personMap.put("5", person5); personMap.put("6", person6); personMap.put("7", person7);
        model.setPeople(personMap);
        Set<Event> events = new HashSet<>();
        events.add(event); events.add(event2); events.add(event3); events.add(event4); events.add(event5); events.add(event6); events.add(event7);
        model.setCurEvents(events);

        model.fillSearchResultPeopleAndEvents("a");     //for each search, add the expected results into arrays and compare with the newly filled model arrays
        List<Person> resultPeople = new ArrayList<>();
        List<Event> resultEvents = new ArrayList<>();
        resultPeople.add(person);
        resultEvents.add(event);
        assertEquals(resultPeople, model.getSearchResultPeople());
        assertEquals(resultEvents , model.getSearchResultEvents());

        model.fillSearchResultPeopleAndEvents("g");
        resultPeople.clear();
        resultEvents.clear();
        resultPeople.add(person); resultPeople.add(person2); resultPeople.add(person3); resultPeople.add(person4); resultPeople.add(person5); resultPeople.add(person6); resultPeople.add(person7);
        assertEquals(resultPeople.size(), model.getSearchResultPeople().size()); //all 7
        assertEquals(event7, model.getSearchResultEvents().get(0)); //just event7

        model.fillSearchResultPeopleAndEvents("6");
        resultPeople.clear();
        resultEvents.clear();
        resultEvents.add(event); resultEvents.add(event2); resultEvents.add(event3); resultEvents.add(event4); resultEvents.add(event5); resultEvents.add(event6); resultEvents.add(event7);
        assertEquals(0, model.getSearchResultPeople().size()); //none
        assertEquals(resultEvents.size() , model.getSearchResultEvents().size()); //all 7

        model.fillSearchResultPeopleAndEvents("d");
        resultPeople.clear();
        resultEvents.clear();
        resultPeople.add(person); resultPeople.add(person2); resultPeople.add(person3); resultPeople.add(person4);
        assertEquals(resultPeople.size(), model.getSearchResultPeople().size()); //person - person4
        assertEquals(event4, model.getSearchResultEvents().get(0)); //just event4

        model.fillSearchResultPeopleAndEvents("4");
        resultPeople.clear();
        resultEvents.clear();
        resultEvents.add(event); resultEvents.add(event2); resultEvents.add(event3); resultEvents.add(event4); resultEvents.add(event5);
        assertEquals(0, model.getSearchResultPeople().size()); //none
        assertEquals(resultEvents.size(), model.getSearchResultEvents().size()); //event - event5
    }

    @Test
    public void testFillCurrentPersonChildren(){
        Person father = new Person("father", "", "", "", 'm', "", "", "mother");
        Person mother = new Person("mother", "", "", "", 'm', "", "", "father");
        Person stranger1 = new Person("stranger1", "", "", "", 'm', "", "", "stranger2");
        Person stranger2 = new Person("stranger2", "", "", "", 'm', "", "", "stranger1");
        Person child1 = new Person("child1", "", "", "", 'm', "father", "mother", "");
        Person child2 = new Person("child2", "", "", "", 'm', "father", "mother", "");
        Person child3 = new Person("child3", "", "", "", 'm', "father", "mother", "");
        Person child4 = new Person("child4", "", "", "", 'm', "father", "mother", "");
        Map<String, Person> people = new HashMap<>();
        people.put("father", father); people.put("mother", mother); people.put("stranger1", stranger1); people.put("stranger2", stranger2);
        people.put("child1", child1); people.put("child2", child2); people.put("child3", child3); people.put("child4", child4);
        model.setPeople(people);
        model.setCurrentPerson(father);
        model.fillPersonChildren();
        assertEquals(true, model.getCurrentPersonChildren().contains(child1));
        assertEquals(true, model.getCurrentPersonChildren().contains(child2));
        assertEquals(true, model.getCurrentPersonChildren().contains(child3));
        assertEquals(true, model.getCurrentPersonChildren().contains(child4));
        assertEquals(false, model.getCurrentPersonChildren().contains(mother));
        assertEquals(false, model.getCurrentPersonChildren().contains(father));
        assertEquals(false, model.getCurrentPersonChildren().contains(stranger1));
        assertEquals(false, model.getCurrentPersonChildren().contains(stranger2));
        model.setCurrentPerson(mother);
        model.fillPersonChildren();
        assertEquals(true, model.getCurrentPersonChildren().contains(child1));
        assertEquals(true, model.getCurrentPersonChildren().contains(child2));
        assertEquals(true, model.getCurrentPersonChildren().contains(child3));
        assertEquals(true, model.getCurrentPersonChildren().contains(child4));
        assertEquals(false, model.getCurrentPersonChildren().contains(mother));
        assertEquals(false, model.getCurrentPersonChildren().contains(father));
        assertEquals(false, model.getCurrentPersonChildren().contains(stranger1));
        assertEquals(false, model.getCurrentPersonChildren().contains(stranger2));
    }

    @Test
    public void testFindSpouseAndParents() {
        Person father = new Person("father", "", "", "", 'm', "", "", "mother");
        Person mother = new Person("mother", "", "", "", 'm', "", "", "father");
        Person stranger1 = new Person("stranger1", "", "", "", 'm', "", "", "stranger2");
        Person stranger2 = new Person("stranger2", "", "", "", 'm', "", "", "stranger1");
        Person child1 = new Person("child1", "", "", "", 'm', "father", "mother", "");
        Map<String, Person> people = new HashMap<>();
        people.put("father", father); people.put("mother", mother); people.put("stranger1", stranger1);
        people.put("stranger2", stranger2); people.put("child1", child1);
        Iterator itr = people.entrySet().iterator();
        Person child1Father = null; //person found as child1's father
        Person child1Mother = null; //person found as child1's mother
        Person fatherSpouse = null; //person found as the father's spouse
        Person motherSpouse = null; //person found as the mother's spouse
        while(itr.hasNext()){
            Map.Entry pair = (Map.Entry) itr.next();
            Person person = (Person)pair.getValue();
            String personID = person.getPerson();
            if(personID.equals("father")) {
                child1Father = person;
                motherSpouse = person;
            }
            if(personID.equals("mother")) {
                child1Mother = person;
                fatherSpouse = person;
            }
        }
        assertEquals(father, motherSpouse);
        assertEquals(mother, fatherSpouse);
        assertEquals(father, child1Father);
        assertEquals(mother, child1Mother);
    }

//    private void fillEverythingForTestFilters() {
//        Person father = new Person("father", "", "", "", 'm', "grandma", "", "mother"); //paternal, male
//        Person mother = new Person("mother", "", "", "", 'f', "grandpa", "", "father"); //maternal, female
//        Person paternalGrandma = new Person("grandma", "", "", "", 'f', "", "", "father"); //paternal, female
//        Person maternalGrandpa = new Person("grandpa", "", "", "", 'm', "", "", "father"); //maternal, male
//        Person user = new Person("user", "", "", "", 'm', "father", "mother", "");
//        Event event = new Event("1", "", "user", "", "", "", "", "Birth", 1); //male, Birth
//        Event event2 = new Event("2", "", "father", "", "", "", "", "Death", 3); //paternal, male, Death
//        Event event3 = new Event("3", "", "father", "", "", "", "", "Marriage", 4); //paternal, male, Marriage
//        Event event4 = new Event("4", "", "mother", "", "", "", "", "Marriage", 5); //maternal, female, Marriage
//        Event event5 = new Event("5", "", "mother", "", "", "", "", "Baptism", 6); //maternal, female, Baptism
//        Event event6 = new Event("6", "", "grandma", "", "", "", "", "Birth", 7); //paternal, female, Birth
//        Event event7 = new Event("7", "", "grandma", "", "", "", "", "Baptism", 8); //paternal, female, Baptism
//        Event event8 = new Event("8", "", "grandpa", "", "", "", "", "Victory", 9); //maternal, male, Victory
//        Event event9 = new Event("9", "", "grandpa", "", "", "", "", "Death", 10); //maternal, male, Death
//        Map<String, Person> people = new HashMap<>();
//        people.put("father", father); people.put("mother", mother); people.put("grandma", paternalGrandma);
//        people.put("grandpa", maternalGrandpa); people.put("user", user);
//        model.setPeople(people);
//        Map<String, Event> events = new HashMap<>();
//        events.put("1", event); events.put("2", event2); events.put("3", event3); events.put("4", event4); events.put("5", event5);
//        events.put("6", event6); events.put("7", event7); events.put("8", event8); events.put("9", event9);
//        model.setEvents(events);
//        model.setMaleEvents(new ArrayList<Event>());
//        model.addMaleEvent(event); model.addMaleEvent(event2); model.addMaleEvent(event3); model.addMaleEvent(event8); model.addMaleEvent(event9);
//        model.setFemaleEvents(new ArrayList<Event>());
//        model.addFemaleEvent(event4); model.addFemaleEvent(event5); model.addFemaleEvent(event6); model.addFemaleEvent(event7);
//        model.setPaternalEvents(new ArrayList<Event>());
//        model.addPaternalEvent(event2); model.addPaternalEvent(event3); model.addPaternalEvent(event6); model.addPaternalEvent(event7);
//        model.setMaternalEvents(new ArrayList<Event>());
//        model.addMaternalEvent(event4); model.addMaternalEvent(event5); model.addMaternalEvent(event8); model.addMaternalEvent(event9);
//    }

    @Test
    public void testFilters() {
        Person father = new Person("father", "", "", "", 'm', "grandma", "", "mother"); //paternal, male
        Person mother = new Person("mother", "", "", "", 'f', "grandpa", "", "father"); //maternal, female
        Person paternalGrandma = new Person("grandma", "", "", "", 'f', "", "", "father"); //paternal, female
        Person maternalGrandpa = new Person("grandpa", "", "", "", 'm', "", "", "father"); //maternal, male
        Person user = new Person("user", "", "", "", 'm', "father", "mother", "");
        Event event = new Event("1", "", "user", "", "", "", "", "Birth", 1); //male, Birth
        Event event2 = new Event("2", "", "father", "", "", "", "", "Death", 3); //paternal, male, Death
        Event event3 = new Event("3", "", "father", "", "", "", "", "Marriage", 4); //paternal, male, Marriage
        Event event4 = new Event("4", "", "mother", "", "", "", "", "Marriage", 5); //maternal, female, Marriage
        Event event5 = new Event("5", "", "mother", "", "", "", "", "Baptism", 6); //maternal, female, Baptism
        Event event6 = new Event("6", "", "grandma", "", "", "", "", "Birth", 7); //paternal, female, Birth
        Event event7 = new Event("7", "", "grandma", "", "", "", "", "Baptism", 8); //paternal, female, Baptism
        Event event8 = new Event("8", "", "grandpa", "", "", "", "", "Victory", 9); //maternal, male, Victory
        Event event9 = new Event("9", "", "grandpa", "", "", "", "", "Death", 10); //maternal, male, Death
        Map<String, Person> people = new HashMap<>();
        people.put("father", father); people.put("mother", mother); people.put("grandma", paternalGrandma);
        people.put("grandpa", maternalGrandpa); people.put("user", user);
        model.setPeople(people);
        Map<String, Event> events = new HashMap<>();
        events.put("1", event); events.put("2", event2); events.put("3", event3); events.put("4", event4); events.put("5", event5);
        events.put("6", event6); events.put("7", event7); events.put("8", event8); events.put("9", event9);
        model.setEvents(events);
        model.setMaleEvents(new ArrayList<Event>());
        model.addMaleEvent(event); model.addMaleEvent(event2); model.addMaleEvent(event3); model.addMaleEvent(event8); model.addMaleEvent(event9);
        model.setFemaleEvents(new ArrayList<Event>());
        model.addFemaleEvent(event4); model.addFemaleEvent(event5); model.addFemaleEvent(event6); model.addFemaleEvent(event7);
        model.setPaternalEvents(new ArrayList<Event>());
        model.addPaternalEvent(event2); model.addPaternalEvent(event3); model.addPaternalEvent(event6); model.addPaternalEvent(event7);
        model.setMaternalEvents(new ArrayList<Event>());
        model.addMaternalEvent(event4); model.addMaternalEvent(event5); model.addMaternalEvent(event8); model.addMaternalEvent(event9);
        model.setSideFilters(new HashSet<String>());
        model.setEventFilters(new HashSet<String>());
        model.setGenderFilters(new HashSet<String>());
        model.setCurEvents(new HashSet<Event>());
        //debugged and made sure everything was filled correctly

        //1. add filters: "paternal", "maternal", "m", "f", "Birth", "Death", "Marriage", "Baptism", "Victory"
        //2. filter current events
        //3. check current events for filtered events.

        //male filtered out
        model.addFilter("m");
        model.filterCurrentEvents();
        List<Event> maleEvents = model.getMaleEvents();
        for(int i = 0; i < maleEvents.size(); i++){
            assertEquals(false, model.getCurEvents().contains(maleEvents.get(i)));
        }
        List<Event> femaleEvents = model.getFemaleEvents();
        for(int i = 0; i < femaleEvents.size(); i++){
            assertEquals(true, model.getCurEvents().contains(femaleEvents.get(i)));
        }

        //male, paternal filtered out
        model.addFilter("paternal");
        model.filterCurrentEvents();
        assertEquals(false, model.getCurEvents().contains(event2)); //male, paternal
        assertEquals(false, model.getCurEvents().contains(event3)); //male, paternal
        assertEquals(true, model.getCurEvents().contains(event5)); //female, maternal

        //male, paternal, Death filtered out
        model.addFilter("Death");
        model.filterCurrentEvents();
        assertEquals(false, model.getCurEvents().contains(event2)); //male, paternal, Death
        assertEquals(true, model.getCurEvents().contains(event4)); //maternal, female, Marriage

        //paternal filtered out
        model.removeFilter("Death"); model.removeFilter("m");
        model.filterCurrentEvents();
        List<Event> paternalEvents = model.getPaternalEvents();
        for(int i = 0; i < paternalEvents.size(); i++){
            assertEquals(false, model.getCurEvents().contains(paternalEvents.get(i)));
        }
        List<Event> maternalEvents = model.getMaternalEvents();
        for(int i = 0; i < femaleEvents.size(); i++){
            assertEquals(true, model.getCurEvents().contains(maternalEvents.get(i)));
        }

        //male, maternal filtered out
        model.removeFilter("paternal");
        model.addFilter("m");
        model.addFilter("maternal");
        model.filterCurrentEvents();
        assertEquals(true, model.getCurEvents().contains(event6)); //female, paternal
        assertEquals(true, model.getCurEvents().contains(event7)); //female, paternal
        assertEquals(false, model.getCurEvents().contains(event5)); //female, maternal
        assertEquals(false, model.getCurEvents().contains(event3)); //male, paternal

        //male, maternal, Death filtered out
        model.addFilter("Death");
        model.filterCurrentEvents();
        assertEquals(true, model.getCurEvents().contains(event6));
        assertEquals(false, model.getCurEvents().contains(event9)); //male, maternal, Death
        model.removeFilter("m"); model.removeFilter("maternal"); model.removeFilter("Death");





        //female filtered out
        model.addFilter("f");
        model.filterCurrentEvents();
        for(int i = 0; i < maleEvents.size(); i++){
            assertEquals(true, model.getCurEvents().contains(maleEvents.get(i)));
        }
        for(int i = 0; i < femaleEvents.size(); i++){
            assertEquals(false, model.getCurEvents().contains(femaleEvents.get(i)));
        }

        //female, paternal filtered out
        model.addFilter("paternal");
        model.filterCurrentEvents();
        assertEquals(false, model.getCurEvents().contains(event6)); //female, paternal
        assertEquals(true, model.getCurEvents().contains(event8)); //male, maternal

        //female, paternal, Death filtered out
        model.addFilter("Birth");
        model.filterCurrentEvents();
        assertEquals(false, model.getCurEvents().contains(event6)); //female, paternal, Birth
        assertEquals(true, model.getCurEvents().contains(event9)); //maternal, male, Death
        model.removeFilter("Death"); model.removeFilter("f");
        model.filterCurrentEvents();

        //female, maternal filtered out
        model.removeFilter("paternal");
        model.addFilter("f");
        model.addFilter("maternal");
        model.filterCurrentEvents();
        assertEquals(false, model.getCurEvents().contains(event6)); //female, paternal
        assertEquals(false, model.getCurEvents().contains(event7)); //female, paternal
        assertEquals(false, model.getCurEvents().contains(event5)); //female, maternal
        assertEquals(true, model.getCurEvents().contains(event3)); //male, paternal

        //female, maternal, Baptism filtered out
        model.addFilter("Baptism");
        model.filterCurrentEvents();
        assertEquals(false, model.getCurEvents().contains(event6)); //female, maternal, Baptism
        assertEquals(true, model.getCurEvents().contains(event3)); //male, paternal, Marriage
    }
}

