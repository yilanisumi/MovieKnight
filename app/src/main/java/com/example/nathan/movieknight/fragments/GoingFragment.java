package com.example.nathan.movieknight.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.models.EventList;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.activities.EventActivity;
import com.example.nathan.movieknight.activities.EventListActivity;
import com.example.nathan.movieknight.models.MovieEvent;
import com.example.nathan.movieknight.models.MovieInfo;
import com.example.nathan.movieknight.tmdb.TmdbConnector;
import com.example.nathan.movieknight.tmdb.TmdbService;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Nathan on 3/17/2016.
 */
public class GoingFragment  extends Fragment  {
    ListView list;
    Vector<String> eventList;
    Vector<String> eventIDs;
    final EventListActivity eventListActivity;

    Vector<String> eventMovieTitles;
    Vector<String> eventDates;
    Vector<String> eventTheaters;
    @SuppressLint("ValidFragment")
    public GoingFragment(EventListActivity ea, Vector<String> goingEvents){
        super();
        eventListActivity = ea;
        eventIDs = goingEvents;
        eventList = new Vector<String>();
        eventDates = new Vector<String>();
        eventTheaters = new Vector<String>();
    }
    public GoingFragment(){
        super();
        eventListActivity = null;
        eventList = new Vector<String>();
        eventDates = new Vector<String>();
        eventTheaters = new Vector<String>();
        eventMovieTitles = new Vector<String>();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.in_theaters_layout, null);
        MovieKnightAppli application = (MovieKnightAppli)eventListActivity.getApplication();
        application.setCurrentContext(inflater.getContext());
        for(String id: eventIDs){


            Object[] objects = {"Movie Event Request", id};
            ClientListener cl = application.getClisten();
            if (cl != null) {
                MovieEvent movieEvent = (MovieEvent) cl.clientRequest(objects);
                if (movieEvent != null) {
                }
                    eventList.add(movieEvent.getDescription());
                eventDates.add(movieEvent.getMovieTime());
                eventTheaters.add(movieEvent.getTheater());
                }
        }

        EventList adapter = new
                EventList(eventListActivity, eventList,eventDates,eventTheaters);


        list=(ListView)view.findViewById(R.id.intheaterslistView);
        eventListActivity.setGoingAdapter((adapter));
        //list not showing
        //  list=(ListView) LayoutInflater.from(getApplication()).inflate(R.layout.coming_soon_layout, null);
        if(list != null) {

            if (adapter != null) {
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent(eventListActivity.getApplicationContext(), EventActivity.class);
                        Bundle b = new Bundle();
                        b.putString("eventID", eventIDs.get(position));
                        in.putExtras(b);
                        startActivity(in);
              //          eventListActivity.finish();
                    }
                });
            }
        } else{
            System.out.println("null");
        }

        return view;
    }


}
