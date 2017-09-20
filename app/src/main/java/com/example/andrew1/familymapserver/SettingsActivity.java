package com.example.andrew1.familymapserver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import net.ServerProxy;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import model.Event;
import model.Model;
import model.Person;

/**
 * Created by Andrew1 on 6/9/17.
 */

public class SettingsActivity extends AppCompatActivity {
    private Context mContext = this;
    private Spinner mLifeLinesSpin;
    private Switch mLifeLinesSwitch;
    private Spinner mFamilyLinesSpin;
    private Switch mFamilyLinesSwitch;
    private Spinner mSpouseLinesSpin;
    private Switch mSpouseLinesSwitch;
    private Spinner mMapType;
    private LinearLayout mResync;
    private LinearLayout mLogout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mLifeLinesSpin = (Spinner)findViewById(R.id.settings_life_story_spinner);
        mLifeLinesSpin.setSelection(Model.instance().getLifeLinesColorID());
        mLifeLinesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = mLifeLinesSpin.getSelectedItem().toString();
                if(text.equals("Green"))
                    Model.instance().setLifeLinesColor(Color.GREEN);
                if(text.equals("Blue"))
                    Model.instance().setLifeLinesColor(Color.BLUE);
                if(text.equals("Red"))
                    Model.instance().setLifeLinesColor(Color.RED);
                Model.instance().setLifeLinesColorID(mLifeLinesSpin.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        mLifeLinesSwitch = (Switch) findViewById(R.id.settings_life_story_switch);
        mLifeLinesSwitch.setChecked(Model.instance().getLifeLinesSwitch());
        mLifeLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Model.instance().setLifeLinesSwitch(isChecked);
            }
        });
        mFamilyLinesSpin = (Spinner)findViewById(R.id.settings_family_tree_spinner);
        mFamilyLinesSpin.setSelection(Model.instance().getFamilyLinesColorID());
        mFamilyLinesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = mFamilyLinesSpin.getSelectedItem().toString();
                if(text.equals("Green"))
                    Model.instance().setFamilyLinesColor(Color.GREEN);
                if(text.equals("Blue"))
                    Model.instance().setFamilyLinesColor(Color.BLUE);
                if(text.equals("Red"))
                    Model.instance().setFamilyLinesColor(Color.RED);
                Model.instance().setFamilyLinesColorID(mFamilyLinesSpin.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        mFamilyLinesSwitch = (Switch) findViewById(R.id.settings_family_tree_switch);
        mFamilyLinesSwitch.setChecked(Model.instance().getFamilyLinesSwitch());
        mFamilyLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Model.instance().setFamilyLinesSwitch(isChecked);
            }
        });
        mSpouseLinesSpin = (Spinner)findViewById(R.id.settings_spouse_spinner);
        mSpouseLinesSpin.setSelection(Model.instance().getSpouseLinesColorID());
        mSpouseLinesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = mSpouseLinesSpin.getSelectedItem().toString();
                if(text.equals("Green"))
                    Model.instance().setSpouseLinesColor(Color.GREEN);
                if(text.equals("Blue"))
                    Model.instance().setSpouseLinesColor(Color.BLUE);
                if(text.equals("Red"))
                    Model.instance().setSpouseLinesColor(Color.RED);
                Model.instance().setSpouseLinesColorID(mSpouseLinesSpin.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        mSpouseLinesSwitch = (Switch) findViewById(R.id.settings_spouse_switch);
        mSpouseLinesSwitch.setChecked(Model.instance().getSpouseLinesSwitch());
        mSpouseLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Model.instance().setSpouseLinesSwitch(isChecked);
            }
        });
        mMapType = (Spinner)findViewById(R.id.settings_map_spinner);
        mMapType.setSelection(Model.instance().getMapTypeID());
        mMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String text = mMapType.getSelectedItem().toString();
                Model.instance().setMapTypeID(mMapType.getSelectedItemPosition());
                Model.instance().setMapType(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
        mResync = (LinearLayout) findViewById(R.id.settings_resync);
        mResync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResync.setBackgroundColor(Color.parseColor("#767272")); //dark gray
                Model.instance().clearLocalData();
                EventTask eventTask = new EventTask();
                eventTask.execute(Model.instance().getAuthToken());
                PersonTask personTask = new PersonTask();
                personTask.execute(Model.instance().getAuthToken());
            }
        });
        mLogout = (LinearLayout) findViewById(R.id.settings_logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogout.setBackgroundColor(Color.parseColor("#767272")); //dark gray
                Model.instance().setUser(null);
                Model.instance().clearLocalData();
                Model.instance().resetSettings();
                Intent intent = new Intent( mContext, MainActivity.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity( intent );
            }
        });
    }

    public class EventTask extends AsyncTask<String, Void, Event[]> {

        @Override
        protected Event[] doInBackground(String... token) {
            String host = Model.instance().getHost();
            int port = Model.instance().getPort();
            ServerProxy sp = new ServerProxy(host, port);
            Event[] events = null;
            try {
                events = (Event[])sp.getAllData(token[0], "event");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return events;
        }

        @Override
        protected void onPostExecute(Event[] events) {
            if(events == null){
                System.out.println("No events received");
                Toast.makeText(mContext, "Failed getting events", Toast.LENGTH_SHORT).show();
            }
            else {
                System.out.println("Success getting events");
                Map<String, Event> eventMap = new HashMap<String, Event>();
                for(int i = 0; i < events.length; i++){
                    eventMap.put(events[i].getEventID(), events[i]);
                }
                Model model = Model.instance();
                model.setEvents(eventMap);
                model.fillEventTypes();
                model.turnFilterSwitchesOn();
            }
        }
    }

    public class PersonTask extends AsyncTask<String, Void, Person[]> {

        @Override
        protected Person[] doInBackground(String... token) {
            String host = Model.instance().getHost();
            int port = Model.instance().getPort();
            ServerProxy sp = new ServerProxy(host, port);
            Person[] people = null;
            try {
                people = (Person[]) sp.getAllData(token[0], "person");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return people;
        }

        @Override
        protected void onPostExecute(Person[] people) {
            if (people == null) {
                System.out.println("No events received");
                Toast.makeText(mContext, "Failed getting people", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("Success getting events");
                Map<String, Person> personMap = new HashMap<String, Person>();
                for (int i = 0; i < people.length; i++) {
                    personMap.put(people[i].getPerson(), people[i]);
                }
                Model model = Model.instance();
                model.setPeople(personMap);
                model.setUser(people[0]);
                model.fillMaternalAncestors(people);
                model.fillPaternalAncestors(people);
                Toast.makeText(mContext, "Resync succeeded", Toast.LENGTH_SHORT).show();
                Intent mapIntent = new Intent(mContext, MainActivity.class);
                startActivity(mapIntent);
            }
        }
    }
}


