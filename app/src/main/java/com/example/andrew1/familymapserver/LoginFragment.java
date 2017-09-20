package com.example.andrew1.familymapserver;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import net.ServerProxy;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import model.Event;
import model.Model;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class LoginFragment extends Fragment {
    private Button mLoginButton;
    private Button mRegisterButton;
    private EditText mHostEdit;
    private EditText mPortEdit;
    private EditText mUserEdit;
    private EditText mPasswordEdit;
    private EditText mFirstEdit;
    private EditText mLastEdit;
    private EditText mEmailEdit;
    private RadioButton mMaleRadio;
    private RadioButton mFemaleRadio;
    private boolean radioClicked = false;
    private boolean male = false;
    private boolean female = false;
    private static final String TAG = "LoginFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mHostEdit = (EditText)v.findViewById(R.id.host_edit);
        //mHostEdit.setText("10.14.176.191");
        mHostEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                Model.instance().setHost(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                enableRightButtons();
            }
        });
        mPortEdit = (EditText)v.findViewById(R.id.port_edit);
        //mPortEdit.setText("8080");
        mPortEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if(s.toString().matches("^-?\\d+$"))    //if it is a digit.
                    Model.instance().setPort(Integer.parseInt(s.toString()));
            }
            @Override
            public void afterTextChanged(Editable s) {
                enableRightButtons();
            }
        });

        mUserEdit = (EditText)v.findViewById(R.id.user_edit);
        //mUserEdit.setText("user");
        mUserEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                enableRightButtons();
            }
        });

        mPasswordEdit = (EditText)v.findViewById(R.id.password_edit);
        //mPasswordEdit.setText("pass");
        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                enableRightButtons();
            }
        });

        mFirstEdit = (EditText)v.findViewById(R.id.first_edit);
        mFirstEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //blank
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }
            @Override
            public void afterTextChanged(Editable s) {
                //blank
                enableRightButtons();
            }
        });

        mLastEdit = (EditText)v.findViewById(R.id.last_edit);
        mLastEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                enableRightButtons();
            }
        });

        mEmailEdit = (EditText)v.findViewById(R.id.email_edit);
        mEmailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
            @Override
            public void afterTextChanged(Editable s) {
                enableRightButtons();
            }
        });
        mMaleRadio = (RadioButton)v.findViewById(R.id.radio_male);
        mMaleRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        mFemaleRadio = (RadioButton)v.findViewById(R.id.radio_female);
        mFemaleRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        mLoginButton = (Button)v.findViewById(R.id.login_button);
        mLoginButton.setEnabled(false);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });
        mRegisterButton = (Button)v.findViewById(R.id.register_button);
        mRegisterButton.setEnabled(false);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryRegister();
            }
        });
        return v;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        radioClicked = true;
        enableRightButtons();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    male = true;
                    //user is male
                    break;
            case R.id.radio_female:
                if (checked)
                    female = true;
                    //user is female
                    break;
        }
    }

    public void enableRightButtons() {          //enables login button when top 4 are filled and register when all are filled
        if(!mHostEdit.getText().toString().equals("")) {
            if (!mPortEdit.getText().toString().equals("")) {
                if (!mUserEdit.getText().toString().equals("")) {
                    if (!mPasswordEdit.getText().toString().equals("")) {
                        mLoginButton.setEnabled(true);
                        if(!mFirstEdit.getText().toString().equals("")){
                            if(!mLastEdit.getText().toString().equals("")){
                                if(!mEmailEdit.getText().toString().equals("")){
                                    if(radioClicked){
                                        mRegisterButton.setEnabled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public char getGender() {
        if(male)
            return 'm';
        else
            return 'f';
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void tryLogin() {
        LoginRequest lRequest = new LoginRequest(mUserEdit.getText().toString(), mPasswordEdit.getText().toString());
        LoginTask task = new LoginTask();
        task.execute(lRequest);
    }

    private void tryRegister() {
        RegisterRequest rRequest = new RegisterRequest(mUserEdit.getText().toString(), mPasswordEdit.getText().toString(),
                                                        mEmailEdit.getText().toString(), mFirstEdit.getText().toString(),
                                                        mLastEdit.getText().toString(), getGender());
        RegisterTask task = new RegisterTask();
        task.execute(rRequest);
    }

    public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResult> {

        @Override
        protected LoginResult doInBackground(LoginRequest... request) {
            String host = Model.instance().getHost();
            int port = Model.instance().getPort();
            ServerProxy sp = new ServerProxy(host, port);
            LoginResult lResult = null;
            try {
                lResult = sp.login(request[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return lResult;
        }

        @Override
        protected void onPostExecute(LoginResult result) {
            if(result == null){
                System.out.println("No login result");
            }
            else if(result.getErrorMessage() == null){
                //login succeeded
                Model.instance().setAuthToken(result.getToken());
                EventTask eventTask = new EventTask();
                eventTask.execute(result.getToken());
                PersonTask personTask = new PersonTask();
                personTask.execute(result.getToken());
            }
            else{
                Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                //login failed
            }
        }
    }

    public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResult> {

        @Override
        protected RegisterResult doInBackground(RegisterRequest... request) {
            String host = Model.instance().getHost();
            int port = Model.instance().getPort();
            ServerProxy sp = new ServerProxy(host, port);
            RegisterResult rResult = null;
            try {
                rResult = sp.register(request[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return rResult;
        }

        @Override
        protected void onPostExecute(RegisterResult result) {
            if(result == null){
                System.out.println("No register result");
            }
            else if(result.getErrorMessage() == null){
                //register succeeded
                Toast.makeText(getActivity(), "Registered " + result.getUsername(), Toast.LENGTH_SHORT).show(); //put first and last name of user or error message
                Model.instance().setAuthToken(result.getToken());
                EventTask eventTask = new EventTask();
                eventTask.execute(result.getToken());
                PersonTask personTask = new PersonTask();
                personTask.execute(result.getToken());
            }
            else{
                Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show(); //put first and last name of user or error message
                //register failed
            }
        }
    }

    public class EventTask extends AsyncTask<String, Void, Event[]> {

        @Override
        protected Event[] doInBackground(String... token) {    //get all the events associated with the user
            String host = Model.instance().getHost();
            int port = Model.instance().getPort();
            ServerProxy sp = new ServerProxy(host, port);
            Event[] events = null;
            try {
                events = (Event[]) sp.getAllData(token[0], "event");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return events;
        }

        @Override
        protected void onPostExecute(Event[] events) {
            if(events == null){
                System.out.println("No events received");
                Toast.makeText(getActivity(), "Failed getting events", Toast.LENGTH_SHORT).show();
            }
            else {
                Model model = Model.instance();
                Event[] formattedEvents = model.formatEventTypes(events);
                System.out.println("Success getting events");
                Map<String, Event> eventMap = new HashMap<String, Event>();
                for(int i = 0; i < formattedEvents.length; i++){
                    eventMap.put(formattedEvents[i].getEventID(), formattedEvents[i]);
                }
                model.setEvents(eventMap);
                model.fillEventTypes();
                model.turnFilterSwitchesOn();
            }
        }
    }

    public class PersonTask extends AsyncTask<String, Void, Person[]> { //get all of the people associated with the user

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
            if(people == null){
                System.out.println("No events received");
                Toast.makeText(getActivity(), "Failed getting people", Toast.LENGTH_SHORT).show();
            }
            else {
                System.out.println("Success getting events");
                Map<String, Person> personMap = new HashMap<String, Person>();
                for(int i = 0; i < people.length; i++){
                    personMap.put(people[i].getPerson(), people[i]);
                }
                Model model = Model.instance();
                model.setPeople(personMap);
                model.setUser(people[0]);
                model.fillMaternalAncestors(people);
                model.fillPaternalAncestors(people);
                Toast.makeText(getActivity(), people[0].getFirstName() + " " + people[0].getLastName() + " logged in.", Toast.LENGTH_SHORT).show();
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.changeFragments();
            }
        }
    }
}

