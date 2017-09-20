package model;

import android.graphics.Color;
import android.util.ArraySet;

import com.example.andrew1.familymapserver.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import adapter.SearchRow;

/**
 * Created by Andrew1 on 6/3/17.
 */

public class Model {
    private static Model _instance = new Model();

    public static Model instance(){
        return _instance;
    }

    private Person user; //current user
    private String authToken; //current authToken of user
    private String host;
    private int port;
    private Set<String> paternalAncestors;
    private Set<String> maternalAncestors;
    private List<Event> paternalEvents;
    private List<Event> maternalEvents;
    private List<Event> maleEvents;
    private List<Event> femaleEvents;
    private int numGenerations;
    private Map<String, Person> people;
    private Map<String, Event> events;
    private Set<Event> curEvents; //events not being filtered out
    private Map<String, List<Event>> eventsByType;
    private Map<String, List<Event>> eventsByPerson;
    private Set<String> eventTypes; //set of the existing eventTypes
    private List<Person> currentPersonChildren;
    private Event currentEvent; //the event that is currently selected
    private Marker eventMarker; //the marker that will be focused in the map activity
    private Person currentPerson; //current person being viewed in person activity

    //Map features
    private Map<String, Marker> markersByID;
    private Marker selectedMarker; //marker that is being viewed in the main activity
    private List<Polyline> polylines;
    private Set<PolylineInfo> polylineInfoSet;
    private String mapType;
    private int mapTypeID; //for the mapType spinner
    private Map<String, Integer> markerColorByEventType;
    private float[] markerColors;

    //Settings
    private boolean lifeLinesSwitch; //on or off
    private boolean familyLinesSwitch;
    private boolean spouseLinesSwitch;
    private int lifeLinesColor; //value of spinners
    private int familyLinesColor;
    private int spouseLinesColor;
    private int lifeLinesColorID;
    private int familyLinesColorID;
    private int spouseLinesColorID;

    //Filters
    private List<Boolean> filterSwitches;
    private List<String> filters; //For the FilterActivity recyclerview
    private List<String> filterTypes; //For setting filters
    private Set<String> eventFilters; //active event filters
    private Set<String> sideFilters; //active side filters
    private Set<String> genderFilters; //active gender filters

    //Search
    private List<Person> searchResultPeople;
    private List<Event> searchResultEvents;
    private List<SearchRow> searchResultRows;


    private Model(){}

    public void clearLocalData() {
        currentEvent = null;
        currentPerson = null;
        eventMarker = null;
        selectedMarker = null;
    }

    public void resetSettings() { //for logout and resync
        lifeLinesSwitch = false;
        familyLinesSwitch = false;
        spouseLinesSwitch = false;
        lifeLinesColor = Color.GREEN;
        familyLinesColor = Color.BLUE;
        spouseLinesColor = Color.RED;
        lifeLinesColorID = 0;
        familyLinesColorID = 0;
        spouseLinesColorID = 0;
        for(int i = 0; i < filterSwitches.size(); i++){
            filterSwitches.set(i, true);
        }
        filters = new ArrayList<>();
        filterTypes = new ArrayList<>();
        eventFilters = new HashSet<>();
        sideFilters = new HashSet<>();
        genderFilters = new HashSet<>();
        mapType = "Normal";
        mapTypeID = 0;
    }

    public Event[] formatEventTypes(Event[] events) { //standardize all eventTypes to the first letter upper case and the rest lower.
        Event[] formattedEvents = new Event[events.length]; //Ex: "BAPTISM" -> "Baptism"
        for (int i = 0; i < events.length; i++) {
            Event event = events[i];
            String eventTypeLower = event.getEventType().toLowerCase();
            String eventType = eventTypeLower.substring(0, 1).toUpperCase() + eventTypeLower.substring(1);
            event.setEventType(eventType);
            formattedEvents[i] = event;
        }
        return formattedEvents;
    }

    public void fillEventTypes() {      //Get all of the event types from the events.
        Set<String> eventTypes = new HashSet<>();

        Iterator it = _instance.getEvents().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Event event = (Event)pair.getValue();
            String eventTypeLower = event.getEventType().toLowerCase();
            String eventType = eventTypeLower.substring(0, 1).toUpperCase() + eventTypeLower.substring(1);
            eventTypes.add(eventType);
        }
        _instance.setEventTypes(eventTypes);
    }

    public void fillEventsByType() {
        Map<String, List<Event>> eventTypeMap = new HashMap<>();
        List<Event> events;
        Vector<String> eventTypesVec = new Vector<String>();
        Set<String> eventTypes = _instance.getEventTypes();
        for(String eventType: eventTypes){
            eventTypesVec.add(eventType);
        }
        for(int i = 0; i < eventTypesVec.size(); i++){
            Iterator it = _instance.getEvents().entrySet().iterator();
            List<Event> cur = new ArrayList<>();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
                Event event = (Event)pair.getValue();
                if(event.getEventType().equals(eventTypesVec.elementAt(i))){
                    cur.add(event);
                }
                //it.remove(); // avoids a ConcurrentModificationException
            }
            eventTypeMap.put(eventTypesVec.elementAt(i), cur);
        }
        _instance.setEventsByType(eventTypeMap);
    }

    public void fillEventsByPerson() {
        Map<String, List<Event>> eventPersonMap = new HashMap<>();
        Iterator personIt = _instance.getPeople().entrySet().iterator();
        while(personIt.hasNext()) {
            Map.Entry personPair = (Map.Entry)personIt.next();
            String personID = (String)personPair.getKey();
            int ten;
            if(personID.equals("425f2mxak"))
                ten = 10;
            Iterator eventIt = _instance.getEvents().entrySet().iterator();
            List<Event> cur = new ArrayList<>();
            while(eventIt.hasNext()) {
                Map.Entry eventPair = (Map.Entry)eventIt.next();
                Event event = (Event)eventPair.getValue();
                if(event.getPersonID().equals(personID)){
                    cur.add(event);
                }
            }
            eventPersonMap.put(personID, cur);
        }
        _instance.setEventsByPerson(eventPersonMap);
    }

    public void fillMaternalAncestors(Person[] people) {
        maternalAncestors = new HashSet<>();
        maternalAncestors.add(people[0].getPerson());
        for(int i = 1; i <= numGenerations; i++){//iterate through each generation
            int length = (int)Math.pow(2,i -1);
            int startIndex = (int)(Math.pow(2,i) - 1) + length;
            int finishIndex = startIndex + length;
            for(int j = startIndex; j < finishIndex; j++){
                maternalAncestors.add(people[j].getPerson());
            }
        }
        int ten = 10;
    }

    public void fillPaternalAncestors(Person[] people) {
        paternalAncestors = new HashSet<>();
        paternalAncestors.add(people[0].getPerson());
        for(int i = 1; i <= numGenerations; i++){//iterate through each generation
            int startIndex = (int)Math.pow(2,i) - 1;
            int finishIndex = startIndex + (int)Math.pow(2,i - 1);

            for(int j = startIndex; j < finishIndex; j++){
                paternalAncestors.add(people[j].getPerson());
            }
        }
        int ten = 10;
    }

    public void fillPaternalEvents() {
        List<Event> paternalEventsTemp = new ArrayList<>();
        Iterator itr = paternalAncestors.iterator();
        while(itr.hasNext()){
            String personID = (String)itr.next();
            List<Event> events = eventsByPerson.get(personID);
            Iterator listItr = events.iterator();
            while(listItr.hasNext()){
                Event event = (Event)listItr.next();
                paternalEventsTemp.add(event);
            }
        }
        _instance.setPaternalEvents(paternalEventsTemp);
    }

    public void fillMaternalEvents() {
        List<Event> maternalEventsTemp = new ArrayList<>();
        Iterator itr = maternalAncestors.iterator();
        while(itr.hasNext()){
            String personID = (String)itr.next();
            List<Event> events = eventsByPerson.get(personID);
            Iterator listItr = events.iterator();
            while(listItr.hasNext()){
                Event event = (Event)listItr.next();
                maternalEventsTemp.add(event);
            }
        }
        _instance.setMaternalEvents(maternalEventsTemp);
    }

    public void fillMaleEvents() {
        maleEvents = new ArrayList<>();
        Iterator itr = people.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry pair = (Map.Entry)itr.next();
            Person person = (Person)pair.getValue();
            List<Event> personEvents;
            if(person.getGender() == 'm'){
                personEvents = eventsByPerson.get(person.getPerson());
                Iterator eventItr = personEvents.iterator();
                while(eventItr.hasNext()){
                    Event event = (Event)eventItr.next();
                    maleEvents.add(event);
                }
            }
        }
    }

    public void fillFemaleEvents() {
        femaleEvents = new ArrayList<>();
        Iterator itr = people.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry pair = (Map.Entry)itr.next();
            Person person = (Person)pair.getValue();
            List<Event> personEvents;
            if(person.getGender() == 'f'){
                personEvents = eventsByPerson.get(person.getPerson());
                Iterator eventItr = personEvents.iterator();
                while(eventItr.hasNext()){
                    Event event = (Event)eventItr.next();
                    femaleEvents.add(event);
                }
            }
        }
    }

    public void fillPersonChildren() {      //Get the current person's children.
        currentPersonChildren = new ArrayList<>();
        String parentID = currentPerson.getPerson();
        Iterator itr = people.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry pair = (Map.Entry)itr.next();
            Person person = (Person)pair.getValue();
            String fatherID = person.getFather();
            if(fatherID != null && fatherID.equals(parentID)){
                currentPersonChildren.add(person);
                continue;
            }
            String motherID = person.getMother();
            if(motherID != null && motherID.equals(parentID)) {
                currentPersonChildren.add(person);
                continue;
            }
        }
    }

    public void fillPolylines(Marker marker) {      //make all of the polylines for the current marker, depending on which are active.
        polylines = new ArrayList<>();
        polylineInfoSet = new HashSet<>();
        Event markerEvent = events.get(marker.getTag());
        Person markerPerson = people.get(markerEvent.getPersonID());

        //spouse
        if(spouseLinesSwitch && markerPerson.getSpouse() != null) {
            Person spouse = people.get(markerPerson.getSpouse());
            Vector<String> orderedEvents = orderPersonEvents(spouse);
            String eventID = orderedEvents.get(0);  //order the spouse's events and grab the first one, which will be the earliest.
            Marker spouseMarker = null;
            if (eventID != null) {    //if there weren't any, no polyline will be made.
                spouseMarker = markersByID.get(eventID);
                PolylineInfo polyline = new PolylineInfo(10, spouseLinesColor, marker.getPosition(), spouseMarker.getPosition());
                polylineInfoSet.add(polyline);
            }
        }

        //parents
        if(familyLinesSwitch){
            int width = 25;
            nextGenPolylines(marker, markerPerson, width);
        }

        //life
        if(lifeLinesSwitch){
            Vector<String> orderedEvents = orderPersonEvents(markerPerson);
            Vector<Marker> orderedMarkers = new Vector<>();
            for(int i = 0; i < orderedEvents.size(); i++){
                orderedMarkers.add(markersByID.get(orderedEvents.get(i)));
            }
            for(int i = 0; i < orderedMarkers.size() - 1; i++){
                PolylineInfo polyline = new PolylineInfo(10, lifeLinesColor, orderedMarkers.get(i).getPosition(), orderedMarkers.get(i + 1).getPosition());
                polylineInfoSet.add(polyline);
            }
        }
    }


    public void nextGenPolylines(Marker marker, Person person, int width) { //recursive function that gets the next polylines to the father and mother each time.
        Person father = people.get(person.getFather());
        Person mother = people.get(person.getMother());
        if(father != null) {
            Vector<String> orderedFatherEvents = orderPersonEvents(father);
            String firstEventID = orderedFatherEvents.get(0);
            Marker fatherMarker = markersByID.get(firstEventID);
            if(orderedFatherEvents != null){
                PolylineInfo polyline = new PolylineInfo(width, familyLinesColor, marker.getPosition(), fatherMarker.getPosition());
                polylineInfoSet.add(polyline);
            }
            nextGenPolylines(fatherMarker, father, width - 4);
        }
        if(mother != null) {
            Vector<String> orderedMotherEvents = orderPersonEvents(mother);
            String firstEventID = orderedMotherEvents.get(0);
            Marker motherMarker = markersByID.get(firstEventID);
            if(orderedMotherEvents != null){
                PolylineInfo polyline = new PolylineInfo(width, familyLinesColor, marker.getPosition(), motherMarker.getPosition());
                polylineInfoSet.add(polyline);
            }
            nextGenPolylines(motherMarker, mother, width - 4);
        }

        return;
    }

    public void fillMarkerColorByType() {       //Fill an array of colors for the different event types.
        markerColorByEventType = new HashMap<>();
        Iterator itr = eventTypes.iterator();
        int index = 0;
        while(itr.hasNext()){
            String type = (String)itr.next();
            markerColorByEventType.put(type, index);
            index++;
        }

        markerColors = new float[10];
        markerColors[0] = BitmapDescriptorFactory.HUE_RED;
        markerColors[1] = BitmapDescriptorFactory.HUE_BLUE;
        markerColors[2] = BitmapDescriptorFactory.HUE_GREEN;
        markerColors[3] = BitmapDescriptorFactory.HUE_ORANGE;
        markerColors[4] = BitmapDescriptorFactory.HUE_YELLOW;
        markerColors[5] = BitmapDescriptorFactory.HUE_VIOLET;
        markerColors[6] = BitmapDescriptorFactory.HUE_CYAN;
        markerColors[7] = BitmapDescriptorFactory.HUE_ROSE;
        markerColors[8] = BitmapDescriptorFactory.HUE_AZURE;
        markerColors[9] = BitmapDescriptorFactory.HUE_MAGENTA;
    }

    public Vector<String> orderPersonEvents(Person person) {    //Order this persons event chronologically
        String personID = person.getPerson();
        List<Event> personEvents = new ArrayList<>();   //Get all of the person's events
        Iterator itr = curEvents.iterator();
        while(itr.hasNext()){
            Event event = (Event)itr.next();
            if(event.getPersonID().equals(personID))
                personEvents.add(event);
        }

        Vector<String> eventIDs = new Vector<>(); //array of this person's events, ordered chronologically.
        int size = personEvents.size();
        for(int i = 0; i < personEvents.size(); i++){   //Find the birth event if there is one and put it first
            Event event = personEvents.get(i);
            if(event.getEventType().toLowerCase().equals("birth")) {
                eventIDs.add(event.getEventID());
                personEvents.remove(event);
            }
        }
        Event eventZ = new Event("1", "1", "1", "1", "1", "1", "1", "zzzzzz", 9000); //an event that will is later alphabetically to compare

        Event earliestEvent = eventZ;
        String deathID = null;
        for(int i = 0; i < size; i++){       //get each event with a year and put them in order, and save the death event to put last
            String eventID = null;
            int earliest = 9000;
            for(int j = 0; j < personEvents.size(); j++){
                Event curEvent = personEvents.get(j);
                String curID = curEvent.getEventID();
                if(!eventIDs.contains(curID)) {
                    if(curEvent.getEventType().toLowerCase().equals("death")){
                        deathID = curEvent.getEventID();
                    } else if (curEvent.getYear() != 0) {
                        if (curEvent.getYear() < earliest) {
                            earliest = curEvent.getYear();
                            earliestEvent = curEvent;
                            eventID = curID;
                        } else if (curEvent.getYear() == earliest) {
                            if (curEvent.getEventType().toLowerCase().compareTo(earliestEvent.getEventType().toLowerCase()) < 0) {
                                eventID = curID;
                            } else
                                eventID = earliestEvent.getEventID();
                        }
                    }
                }
            }
            personEvents.remove(events.get(deathID));
            if(eventID != null) {
                eventIDs.add(eventID);
                personEvents.remove(events.get(eventID));
            }
        }
        if(eventIDs.size() == size) //if all of the events are ordered (all had years) then return them.
            return eventIDs;

        //check for events without years, order them.
        String eventID = null;
        for(int i = 0; i < personEvents.size(); i++){
            for(int j = 0; j < personEvents.size(); j++) {
                Event curEvent = personEvents.get(i);
                String curID = curEvent.getEventID();
                if (!eventIDs.contains(curID)) {
                    if (curEvent.getEventType().toLowerCase().compareTo(earliestEvent.getEventType().toLowerCase()) < 0) {
                        eventID = curID;
                    } else
                        eventID = earliestEvent.getEventID();
                }
            }
            if(eventID != null) {
                eventIDs.add(eventID);
                personEvents.remove(events.get(eventID));
            }
        }

        if(deathID != null)         //if there is a death event, add it last
            eventIDs.add(deathID);

        return eventIDs;
    }

    public void addFilter(String filter) {
        char firstLetter = filter.charAt(0);
        if (filter.length() == 1) {
            if (firstLetter == 'm' || firstLetter == 'f')
                addGenderFilter(filter);
            else {
                addEventFilter(filter);
            }
        }
        else if (filter.equals("paternal") || filter.equals("maternal"))
            addSideFilter(filter);
        else {
            addEventFilter(filter);
        }
    }

    public void removeFilter(String filter) {
        char firstLetter = filter.charAt(0);
        if (filter.length() == 1) {
            if (firstLetter == 'm' || firstLetter == 'f')
                removeGenderFilter(filter);
            else {
                removeEventFilter(filter);
            }
        }
        else if (filter.equals("paternal") || filter.equals("maternal"))
            removeSideFilter(filter);
        else {
            removeEventFilter(filter);
        }
    }

    public void filterCurrentEvents() {     //Put all of the events that are not being filtered into curEvents.
        curEvents.clear();
        boolean checkSide = false;
        boolean checkGender = false;
        boolean checkEvent = false;

        Iterator itr = events.entrySet().iterator();
        if(sideFilters.size() > 0)      //check which filters are active.
            checkSide = true;
        if(genderFilters.size() >0)
            checkGender = true;
        if(eventFilters.size() > 0)
            checkEvent = true;
        while(itr.hasNext()) {
            Map.Entry pair = (Map.Entry) itr.next();
            Event event = (Event) pair.getValue();
            boolean validEvent = true;
            if(checkSide) {
                if (sideFilters.contains("paternal") && paternalEvents.contains(event))
                    validEvent = false;
                else if(sideFilters.contains("maternal") && maternalEvents.contains(event))
                    validEvent = false;
            }
            if(checkGender) {
                if(genderFilters.contains("m") && maleEvents.contains(event))
                    validEvent = false;
                if(genderFilters.contains("f") && femaleEvents.contains(event))
                    validEvent = false;
            }
            if(checkEvent) {
                if (eventFilters.contains(event.getEventType()))
                    validEvent = false;
            }
            if(validEvent)
                curEvents.add(event);

        }
    }

    public void fillFilters() {     //For the Filter Activity UI
        filters = new ArrayList<>();
        filterTypes = new ArrayList<>();
        Iterator itr = eventTypes.iterator();
        while(itr.hasNext()){
            String eventType = (String)itr.next();
            filters.add(eventType + " Events");
            filterTypes.add(eventType);
        }
        filters.add("Father's Side");
        filters.add("Mother's Side");
        filters.add("Male Events");
        filters.add("Female Events");
        filterTypes.add("paternal");
        filterTypes.add("maternal");
        filterTypes.add("m");
        filterTypes.add("f");
    }

    public void turnFilterSwitchesOn() {
        filterSwitches = new ArrayList<>();
        for(int i = 0; i < eventTypes.size() + 4; i++){
            filterSwitches.add(true);
        }
    }

    public void fillSearchResultPeopleAndEvents(String text){       //fill a list of people and a list of events that match the searched string
        searchResultPeople = new ArrayList<>();
        searchResultPeople.clear();
        String lowerText = text.toLowerCase();
        Iterator peopleItr = people.entrySet().iterator();
        while(peopleItr.hasNext()){
            Map.Entry pair = (Map.Entry)peopleItr.next();
            Person person = (Person)pair.getValue();
            String name = person.getFirstName().toLowerCase() + " "
                    + person.getLastName().toLowerCase();
            if(name.contains(lowerText))
                searchResultPeople.add(person);
        }

        searchResultEvents = new ArrayList<>();
        searchResultEvents.clear();
        Iterator eventItr = curEvents.iterator();
        while(eventItr.hasNext()){
            Event event = (Event)eventItr.next();
            Person eventPerson = people.get(event.getPersonID());
//            String info = eventPerson.getFirstName().toLowerCase() + " "
//                    + eventPerson.getLastName().toLowerCase() + " "
            String info = event.getCountry().toLowerCase() + " "
                    + event.getCity().toLowerCase() + " "
                    + event.getEventType().toLowerCase() + " "
                    + event.getYear();
            if(info.contains(lowerText))
                searchResultEvents.add(event);
        }
        fillSearchResultRows();
    }

    public void fillSearchResultRows(){         //convert the two arrays of search results to make the rows for the search activity UI
        searchResultRows = new ArrayList<>();
        //put all found people in row array
        for(int i = 0; i < searchResultPeople.size(); i++){
            Person person = searchResultPeople.get(i);
            int iconResource;
            if(person.getGender() == 'm')
                iconResource = R.drawable.man;
            else if(person.getGender() == 'f')
                iconResource = R.drawable.woman;
            else
                iconResource = R.drawable.person;
            String textTop = person.getFirstName() + " " + person.getLastName();
            String textBottom = "";
            String personID = person.getPerson();
            searchResultRows.add(new SearchRow(iconResource, textTop, textBottom, personID));
        }
        //put all found events in row array
        for(int i = 0; i < searchResultEvents.size(); i++){
            Event event = searchResultEvents.get(i);
            int iconResource = R.drawable.marker;
            String textTop;
            if(event.getYear() == 0)
                textTop = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry();
            else
                textTop = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
            Person eventPerson = people.get(event.getPersonID());
            String textBottom = eventPerson.getFirstName() + " " + eventPerson.getLastName();
            String eventID = event.getEventID();
            searchResultRows.add(new SearchRow(iconResource, textTop, textBottom, eventID));
        }
        return;
    }

    public String getHost() {
        return _instance.host;
    }

    public void setHost(String host) {
        _instance.host = host;
    }

    public int getPort() {
        return _instance.port;
    }

    public void setPort(int port) {
        _instance.port = port;
    }

    public String getAuthToken() {
        return _instance.authToken;
    }

    public void setAuthToken(String authToken) {
        _instance.authToken = authToken;
    }


    public Person getUser() {
        return _instance.user;
    }

    public void setUser(Person p) {
        _instance.user = p;
    }

    public boolean hasUser() {
        if(user != null)
            return true;
        else
            return false;
    }

    public void setPaternalAncestors(Set<String> paternalAncestors) {
        _instance.paternalAncestors = paternalAncestors;
    }

    public void addPaternalAncestor(String personID){
        paternalAncestors.add(personID);
    }

    public void setMaternalAncestors(Set<String> maternalAncestors) {
        _instance.maternalAncestors = maternalAncestors;
    }

    public void addMaternalAncestor(String personID){
        maternalAncestors.add(personID);
    }

    public List<Event> getPaternalEvents() {
        return _instance.paternalEvents;
    }

    public void setPaternalEvents(List<Event> paternalEvents) {
        _instance.paternalEvents = paternalEvents;
    }

    public void addPaternalEvent(Event event){
        paternalEvents.add(event);
    }

    public List<Event> getMaternalEvents() {
        return _instance.maternalEvents;
    }

    public void setMaternalEvents(List<Event> maternalEvents) {
        _instance.maternalEvents = maternalEvents;
    }

    public void addMaternalEvent(Event event){
        maternalEvents.add(event);
    }

    public List<Event> getMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(List<Event> maleEvents) {
        _instance.maleEvents = maleEvents;
    }

    public void addMaleEvent(Event event) {
        maleEvents.add(event);
    }

    public List<Event> getFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(List<Event> femaleEvents) {
        _instance.femaleEvents = femaleEvents;
    }

    public void addFemaleEvent(Event event) {
        femaleEvents.add(event);
    }

    public Map<String, Person> getPeople() {
        return _instance.people;
    }

    public void setPeople(Map<String, Person> people) {
        _instance.people = people;
        numGenerations = (int)(Math.log(people.size() + 1)/Math.log(2)) - 1;
    }

    public Map<String, Event> getEvents() {
        return _instance.events;
    }

    public void setEvents(Map<String, Event> events) {
        _instance.events = events;
    }

    public Set<Event> getCurEvents() {
        return _instance.curEvents;
    }

    public void setCurEvents(Set<Event> curEvents) {
        _instance.curEvents = curEvents;
    }

    public Map<String, List<Event>> getEventsByType() {
        return _instance.eventsByType;
    }

    public void setEventsByType(Map<String, List<Event>> eventsByType) {
        _instance.eventsByType = eventsByType;
    }

    public Map<String, List<Event>> getEventsByPerson() {
        return _instance.eventsByPerson;
    }

    public void setEventsByPerson(Map<String, List<Event>> eventsByPerson) {
        _instance.eventsByPerson = eventsByPerson;
    }

    public Set<String> getEventTypes() {
        return _instance.eventTypes;
    }

    public void setEventTypes(Set<String> eventTypes) {
        _instance.eventTypes = eventTypes;
    }

    public Event getCurrentEvent() {
        return _instance.currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        _instance.currentEvent = currentEvent;
    }

    public Marker getEventMarker() {
        return _instance.eventMarker;
    }

    public void setEventMarker(Marker eventMarker) {
        _instance.eventMarker = eventMarker;
    }

    public List<Person> getCurrentPersonChildren() {
        return _instance.currentPersonChildren;
    }

    public void setCurrentPersonChildren(List<Person> currentPersonChildren) {
        _instance.currentPersonChildren = currentPersonChildren;
    }

    public Person getCurrentPerson() {
        return _instance.currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        _instance.currentPerson = currentPerson;
    }

    public void addPerson(Person p) {
        people.put(p.getPerson(), p);
    }

    public Person getPersonWithID(String ID) {
        return people.get(ID);
    }

    public void addEvent(Event e) {
        events.put(e.getEventID(), e);
    }

    public Event getEventWithID(String ID) {
        return events.get(ID);
    }

    public Map<String, Marker> getMarkersByID() {
        return _instance.markersByID;
    }

    public void setMarkersByID(Map<String, Marker> markersByID) {
        _instance.markersByID = markersByID;
    }

    public Marker getSelectedMarker() {
        return _instance.selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        _instance.selectedMarker = selectedMarker;
    }

    public void addMarker(String eventID, Marker marker) {
        _instance.markersByID.put(eventID, marker);
    }

    public List<Polyline> getPolylines() {
        return _instance.polylines;
    }

    public void setPolylines(List<Polyline> polylines) {
        _instance.polylines = polylines;
    }

    public void addPolyline(Polyline line) {
        _instance.polylines.add(line);
    }

    public Set<PolylineInfo> getPolylineInfoSet() {
        return _instance.polylineInfoSet;
    }

    public void setPolylineInfoSet(Set<PolylineInfo> polylineInfoSet) {
        _instance.polylineInfoSet = polylineInfoSet;
    }

    public List<String> getFilters() {
        return _instance.filters;
    }

    public void setFilters(List<String> filters) {
        _instance.filters = filters;
    }

    public List<String> getFilterTypes() {
        return _instance.filterTypes;
    }

    public void setFilterTypes(List<String> filterTypes) {
        _instance.filterTypes = filterTypes;
    }

    public Set<String> getEventFilters() {
        return _instance.eventFilters;
    }

    public void setEventFilters(Set<String> eventFilters) {
        _instance.eventFilters = eventFilters;
    }

    public void addEventFilter(String filter) {
        eventFilters.add(filter);
    }

    public void removeEventFilter(String filter) {
        eventFilters.remove(filter);
    }

    public Set<String> getSideFilters() {
        return _instance.sideFilters;
    }

    public void setSideFilters(Set<String> sideFilters) {
        _instance.sideFilters = sideFilters;
    }

    public void addSideFilter(String filter) {
        sideFilters.add(filter);
    }

    public void removeSideFilter(String filter) {
        sideFilters.remove(filter);
    }

    public Set<String> getGenderFilters() {
        return _instance.genderFilters;
    }

    public void setGenderFilters(Set<String> genderFilters) {
        _instance.genderFilters = genderFilters;
    }

    public void addGenderFilter(String filter) {
        genderFilters.add(filter);
    }

    public void removeGenderFilter(String filter) {
        genderFilters.remove(filter);
    }

    public String getMapType() {
        return _instance.mapType;
    }

    public void setMapType(String mapType) {
        _instance.mapType = mapType;
    }

    public boolean getLifeLinesSwitch() {
        return _instance.lifeLinesSwitch;
    }

    public void setLifeLinesSwitch(boolean lifeLinesSwitch) {
        _instance.lifeLinesSwitch = lifeLinesSwitch;
    }

    public boolean getFamilyLinesSwitch() {
        return _instance.familyLinesSwitch;
    }

    public void setFamilyLinesSwitch(boolean familyLinesSwitch) {
        _instance.familyLinesSwitch = familyLinesSwitch;
    }

    public boolean getSpouseLinesSwitch() {
        return _instance.spouseLinesSwitch;
    }

    public void setSpouseLinesSwitch(boolean spouseLinesSwitch) {
        _instance.spouseLinesSwitch = spouseLinesSwitch;
    }

    public int getLifeLinesColor() {
        return _instance.lifeLinesColor;
    }

    public void setLifeLinesColor(int lifeLinesColor) {
        _instance.lifeLinesColor = lifeLinesColor;
    }

    public int getFamilyLinesColor() {
        return _instance.familyLinesColor;
    }

    public void setFamilyLinesColor(int familyLinesColor) {
        _instance.familyLinesColor = familyLinesColor;
    }

    public int getSpouseLinesColor() {
        return _instance.spouseLinesColor;
    }

    public void setSpouseLinesColor(int spouseLinesColor) {
        _instance.spouseLinesColor = spouseLinesColor;
    }

    public int getLifeLinesColorID() {
        return _instance.lifeLinesColorID;
    }

    public void setLifeLinesColorID(int lifeLinesColorID) {
        _instance.lifeLinesColorID = lifeLinesColorID;
    }

    public int getFamilyLinesColorID() {
        return _instance.familyLinesColorID;
    }

    public void setFamilyLinesColorID(int familyLinesColorID) {
        _instance.familyLinesColorID = familyLinesColorID;
    }

    public int getSpouseLinesColorID() {
        return _instance.spouseLinesColorID;
    }

    public void setSpouseLinesColorID(int spouseLinesColorID) {
        _instance.spouseLinesColorID = spouseLinesColorID;
    }

    public int getMapTypeID() {
        return _instance.mapTypeID;
    }

    public void setMapTypeID(int mapTypeID) {
        _instance.mapTypeID = mapTypeID;
    }

    public Map<String, Integer> getMarkerColorByEventType() {
        return _instance.markerColorByEventType;
    }

    public void setMarkerColorByEventType(Map<String, Integer> markerColorByEventType) {
        _instance.markerColorByEventType = markerColorByEventType;
    }

    public float[] getMarkerColors() {
        return _instance.markerColors;
    }

    public void setMarkerColors(float[] markerColors) {
        _instance.markerColors = markerColors;
    }

    public List<Boolean> getFilterSwitches() {
        return _instance.filterSwitches;
    }

    public void setFilterSwitches(List<Boolean> filterSwitches) {
        _instance.filterSwitches = filterSwitches;
    }

    public void changeFilterSwitch(int index, boolean onOff){
        filterSwitches.set(index, onOff);
    }

    public List<Person> getSearchResultPeople() {
        return _instance.searchResultPeople;
    }

    public void setSearchResultPeople(List<Person> searchResultPeople) {
        _instance.searchResultPeople = searchResultPeople;
    }

    public List<Event> getSearchResultEvents() {
        return _instance.searchResultEvents;
    }

    public void setSearchResultEvents(List<Event> searchResultEvents) {
        _instance.searchResultEvents = searchResultEvents;
    }

    public List<SearchRow> getSearchResultRows() {
        return _instance.searchResultRows;
    }

    public void setSearchResultRows(List<SearchRow> searchResultRows) {
        _instance.searchResultRows = searchResultRows;
    }
}

