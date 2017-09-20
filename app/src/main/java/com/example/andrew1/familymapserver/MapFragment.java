package com.example.andrew1.familymapserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.Event;
import model.Model;
import model.Person;
import model.PolylineInfo;


public class MapFragment extends Fragment implements OnMapReadyCallback{
    private View mView;
    private MapView mMapView;
    private GoogleMap googleMap;
    private ImageView mFigure;
    private TextView mName_view;
    private TextView mEvent_view;
    private LinearLayout mDescription_area;

    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        Context context = getActivity();
        if(context.getClass() == MainActivity.class)
            inflater.inflate(R.menu.menu_map, menu);
        if(context.getClass() == MapActivity.class)
            inflater.inflate(R.menu.menu_deep, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                //start search activity
                return true;
            case R.id.item_filter:
                Intent filterIntent = new Intent(getActivity(), FilterActivity.class);
                startActivity(filterIntent);
                //start filter activity
                return true;
            case R.id.item_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                //start settings activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mView = view;
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        mFigure = (ImageView)view.findViewById(R.id.description_figure);
        mName_view = (TextView)view.findViewById(R.id.description_name);
        mEvent_view = (TextView)view.findViewById(R.id.description_event);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                doMarkerClick(marker);
                return false;
            }
        });

        fillModel();
        setMapType();
        setUpMap();
    }

    public void doMarkerClick(Marker marker) {  //used when a marker is clicked and also when returning to map to go to selected event marker
        List<Polyline> polylinesToDelete = Model.instance().getPolylines();
        if(polylinesToDelete != null) {
            for (int i = 0; i < polylinesToDelete.size(); i++) {
                polylinesToDelete.get(i).remove();
            }
        }
        String eventID = (String)marker.getTag();
        Event event = Model.instance().getEvents().get(eventID);
        Person person = Model.instance().getPeople().get(event.getPersonID());

        if(Model.instance().getCurEvents().contains(event)) {
            if(getActivity().getClass() == MainActivity.class)
                Model.instance().setSelectedMarker(marker);
            Model.instance().setCurrentPerson(person);
            if (person.getGender() == 'm')
                mFigure.setImageResource(R.drawable.man);
            else if (person.getGender() == 'f')
                mFigure.setImageResource(R.drawable.woman);
            else
                mFigure.setImageResource(R.drawable.person);
            mName_view.setText(person.getFirstName() + " " + person.getLastName());
            if (event.getYear() == 0)
                mEvent_view.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry());
            else
                mEvent_view.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");

            mDescription_area = (LinearLayout) mView.findViewById(R.id.description_area);
            mDescription_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent personIntent = new Intent(getActivity(), PersonActivity.class);
                    startActivity(personIntent);
                }
            });
            Model.instance().fillPolylines(marker);
            Set<PolylineInfo> polylines = Model.instance().getPolylineInfoSet();
            Iterator itr = polylines.iterator();
            while (itr.hasNext()) {
                PolylineInfo info = (PolylineInfo) itr.next();
                Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                        .width(info.getWidth())
                        .color(info.getColor())
                        .add(info.getSelectedLatLng(), info.getOtherLatLng()));
                Model.instance().addPolyline(polyline);
            }
        }
        else
            Model.instance().setSelectedMarker(null);
    }

    public void fillModel() {       //use people and events to fill the model data structures
        Model model = Model.instance();
        model.fillFilters();
        model.fillEventsByType();
        model.fillEventsByPerson();
        model.fillMaternalEvents();
        model.fillPaternalEvents();
        model.fillMaleEvents();
        model.fillFemaleEvents();
        model.fillMarkerColorByType();
    }

    public void setUpMap() {        //set up map with unfiltered events and move the camera to the correct event.
        Model model = Model.instance();
        model.setMarkersByID(new HashMap<String, Marker>());
        if(model.getCurEvents() == null) {
            model.setCurEvents(new HashSet<Event>());
            model.setEventFilters(new HashSet<String>());
            model.setSideFilters(new HashSet<String>());
            model.setGenderFilters(new HashSet<String>());
        }
        Model.instance().filterCurrentEvents();
        showCurrentEvents();
        Marker selectedMarker = Model.instance().getSelectedMarker();
        Marker eventMarker = Model.instance().getEventMarker();
        if(getActivity().getClass() == MainActivity.class && selectedMarker != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedMarker.getPosition(), 5));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(4), 2000, null);
            doMarkerClick(selectedMarker);
        }
        else if(getActivity().getClass() == MapActivity.class && eventMarker != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventMarker.getPosition(), 5));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(4), 2000, null);
            doMarkerClick(eventMarker);
        }
    }

    public void showCurrentEvents() {      //put just the events not being filtered.
        Set<Event> curEvents = Model.instance().getCurEvents();
        Iterator itr = curEvents.iterator();
        while (itr.hasNext()) {
            Event event = (Event)itr.next();
            LatLng cur = new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));
            Marker marker = googleMap.addMarker(new MarkerOptions().position(cur));
            int index = Model.instance().getMarkerColorByEventType().get(event.getEventType());
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(Model.instance().getMarkerColors()[index]));
            marker.setTag(event.getEventID());
            Model.instance().addMarker(event.getEventID(), marker);
        }
    }


    public void setMapType() {
        String mapType = Model.instance().getMapType();
        if(mapType != null) {
            switch (mapType) {
                case "Normal":
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case "Hybrid":
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case "Satellite":
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case "Terrain":
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                default:
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        }
    }
}


