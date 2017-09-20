package com.example.andrew1.familymapserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.FilterAdapter;
import adapter.PersonChild;
import adapter.PersonExpandableAdapter;
import adapter.PersonParent;
import model.Event;
import model.Model;
import model.Person;

/**
 * Created by Andrew1 on 6/9/17.
 */

public class PersonActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PersonExpandableAdapter mAdapter;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mRecyclerView = (RecyclerView) findViewById(R.id.person_recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new PersonExpandableAdapter(this, generateChildren());
        mAdapter.setCustomParentAnimationViewId(R.id.parent_person_item_expand_arrow);
        mAdapter.setParentClickableViewAnimationDefaultDuration();
        mAdapter.setParentAndIconExpandOnClick(true);
        mRecyclerView.setAdapter(mAdapter);
        Person curPerson = Model.instance().getCurrentPerson();
        mFirstName = (TextView) findViewById(R.id.person_first);
        mFirstName.setText(curPerson.getFirstName());
        mLastName = (TextView) findViewById(R.id.person_last);
        mLastName.setText(curPerson.getLastName());
        mGender = (TextView) findViewById(R.id.person_gender);
        String gender = "Unknown";
        switch(curPerson.getGender()){
            case 'm':
                gender = "Male";
                break;
            case 'f':
                gender = "Female";
                break;
        }
        mGender.setText(gender);
    }

    private ArrayList<ParentObject> generateChildren() {
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        Model model = Model.instance();
        Person currentPerson = model.getCurrentPerson();

        //Life events expandable list
        PersonParent eventParent = new PersonParent("LIFE EVENTS");
        ArrayList<Object> eventChildList = new ArrayList<>();
        List<String> orderedEvents = model.orderPersonEvents(currentPerson);
        for (String eventID : orderedEvents) {
            Event event = model.getEvents().get(eventID);
            String eventText = null;
            if(event.getYear() == 0)
                eventText = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry();
            else
                eventText = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
            String nameText = currentPerson.getFirstName() + " " + currentPerson.getLastName();
            eventChildList.add(new PersonChild(R.drawable.marker, eventText, nameText, eventID));
        }
        eventParent.setChildObjectList(eventChildList);
        parentObjects.add(eventParent);


        //Family expandable list
        PersonParent familyParent = new PersonParent("FAMILY");
        ArrayList<Object> familyChildList = new ArrayList<>();
        model.fillPersonChildren();
        Map<String, Person> people = Model.instance().getPeople();
        List<Person> family = new ArrayList<>();
        Person father = people.get(currentPerson.getFather());
        if (father != null)
            family.add(people.get(currentPerson.getFather()));
        Person mother = people.get(currentPerson.getMother());
        if (mother != null)
            family.add(people.get(currentPerson.getMother()));
        Person spouse = people.get(currentPerson.getSpouse());
        if (spouse != null)
            family.add(people.get(currentPerson.getSpouse()));
        for (Person person : model.getCurrentPersonChildren()){
            family.add(person);
        }
        for(Person person: family){
            String personID = person.getPerson();
            int iconResource;
            switch(person.getGender()){
                case 'm':
                    iconResource = R.drawable.man;
                    break;
                case 'f':
                    iconResource = R.drawable.woman;
                    break;
                default:
                    iconResource = R.drawable.person;
                    break;
            }
            String nameText = person.getFirstName() + " " + person.getLastName();
            String relationText = null;

            if(personID.equals(currentPerson.getFather()))
                relationText = "Father";
            else if(personID.equals(currentPerson.getMother()))
                relationText = "Mother";
            else if(personID.equals(currentPerson.getSpouse()))
                relationText = "Spouse";
            else
                relationText = "Child";
            familyChildList.add(new PersonChild(iconResource, nameText, relationText, personID));
        }
        familyParent.setChildObjectList(familyChildList);
        parentObjects.add(familyParent);

        return parentObjects;
    }
}
