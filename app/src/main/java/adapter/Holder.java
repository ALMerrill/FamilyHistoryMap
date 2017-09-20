package adapter;

import android.content.Intent;
import android.view.View;

import com.example.andrew1.familymapserver.MapActivity;
import com.example.andrew1.familymapserver.PersonActivity;

import java.util.List;
import java.util.Map;

import model.Event;
import model.Model;
import model.Person;

/**
 * Created by Andrew1 on 6/15/17.
 */

public class Holder {
    public Holder() {}

    public void goToNextPersonOrEvent(String ID, String bottomText, View v) {       //used in the person activity and the search activity
        if(bottomText.equals(("Father")) || bottomText.equals("Mother") || bottomText.equals("Spouse") || bottomText.equals("Child") || bottomText.equals("")){
            //layout clicked is person
            Person nextPerson = Model.instance().getPersonWithID(ID);
            Model.instance().setCurrentPerson(nextPerson);
            Intent personIntent = new Intent(v.getContext(), PersonActivity.class);
            v.getContext().startActivity(personIntent);
        }
        else{
            //layout clicked is event
            Event nextEvent = Model.instance().getEventWithID(ID);
            Model.instance().setCurrentEvent(nextEvent);
            Model.instance().setEventMarker(Model.instance().getMarkersByID().get(ID));
            Intent mapIntent = new Intent(v.getContext(), MapActivity.class);
            v.getContext().startActivity(mapIntent);
        }
    }
}
