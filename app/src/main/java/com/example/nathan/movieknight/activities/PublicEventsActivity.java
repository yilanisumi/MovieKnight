package com.example.nathan.movieknight.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.nathan.movieknight.models.EventList;
import com.example.nathan.movieknight.models.MovieEvent;

import java.util.List;
import java.util.Vector;

public class PublicEventsActivity extends NavigationDrawer
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);

        Object[] objects ={"Get Public Events Request"};
        ClientListener cl= application.getClisten();
        Vector<String> eventIDs = new Vector<String>();
        Vector<String> eventList = new Vector<String>();
        Vector<String> eventDates = new Vector<String>();
        Vector<String> eventTheaters = new Vector<String>();
        if(cl!= null){
           eventIDs = (Vector<String>) cl.clientRequest(objects);
        }

        for(String id: eventIDs){
            Object[] objects2 = {"Movie Event Request", id};
            if (cl != null) {
                MovieEvent movieEvent = (MovieEvent) cl.clientRequest(objects2);
                if (movieEvent != null) {
                    eventList.add(movieEvent.getDescription());
                    eventDates.add(movieEvent.getMovieTime());
                    eventTheaters.add(movieEvent.getTheater());
                }
            }
        }

        EventList adapter = new
                EventList(this, eventList,eventDates,eventTheaters);


        ListView list=(ListView)findViewById(R.id.publicEventsList);
        final Vector<String> movieEventIDs = eventIDs;

        if(list != null) {

            if (adapter != null) {
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent(getApplicationContext(), EventActivity.class);
                        Bundle b = new Bundle();
                        b.putString("eventID", movieEventIDs.get(position));
                        in.putExtras(b);
                        startActivity(in);
                    //    finish();
                    }
                });
            }
        } else{
            System.out.println("null");
        }


    }


}
