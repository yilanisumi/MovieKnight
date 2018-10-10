package com.example.nathan.movieknight.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.MovieEvent;
import com.example.nathan.movieknight.models.MovieInfo;
import com.example.nathan.movieknight.tmdb.TmdbConnector;
import com.example.nathan.movieknight.tmdb.TmdbService;
import com.squareup.picasso.Picasso;

import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class EventActivity extends NavigationDrawer {
    private String eventID;
    private int movieID;
    private ImageView eventImage;
    TextView eventTitle;
    EditText date;
    EditText theater;
    TextView owner;
    TextView movieType;
    Button goingButton;
    Button notGoingButton;
    ListView invitedList;
    ListView goingList;
    ArrayAdapter<String> goingAdapter;
    ArrayAdapter<String> invitedAdapter;
    Vector<String> going;
    Vector<String> invited;
    MovieEvent movieEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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
        //initializing variables
        invitedList = (ListView)findViewById(R.id.invitedListView);
        goingList = (ListView)findViewById(R.id.goingListView);

        eventTitle = (TextView) findViewById(R.id.eventTitle);
        date = (EditText) findViewById(R.id.dateTime);
        theater = (EditText) findViewById(R.id.movieTheater);
        owner = (TextView) findViewById(R.id.movieOwner);
        movieType = (TextView) findViewById(R.id.movieType);
        eventImage = (ImageView) findViewById(R.id.eventImage);

        final Button editbutton = (Button)findViewById(R.id.customizeButton);
        final Button goingButton= (Button)findViewById(R.id.goingButton);
        Button notGoingButton= (Button)findViewById(R.id.notgoingButton);

        //getting event from bundle
        Bundle b = getIntent().getExtras();
        if(b != null) {
            eventID = b.getString("eventID");
            Object[] objects = {"Movie Event Request", eventID};
            ClientListener cl = application.getClisten();
            if (cl != null) {
                //request event from the server
                movieEvent = (MovieEvent) cl.clientRequest(objects);
                if (movieEvent != null) {
                    //populate page with information
                    eventTitle.setText(movieEvent.getDescription());
                    date.setText(movieEvent.getMovieTime());
                    theater.setText(movieEvent.getTheater());
                    movieID = movieEvent.getGoingToWatch();
                    if(movieEvent.isPublic_private()){
                        movieType.setText("Public Event");
                    }else{
                        movieType.setText("Private Event");
                    }
                    getMovieInfo(movieID);
                    if(movieEvent.getOwner().equals(application.getUserName())){
                        goingButton.setVisibility(View.GONE);
                        notGoingButton.setVisibility(View.GONE);
                    }
                    invited =  movieEvent.getInvited();
                    invitedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, invited);
                    invitedList.setAdapter(invitedAdapter);
                    going =  movieEvent.getParticipants();
                    owner.setText("Owner: " + movieEvent.getOwner());
                    //set visibility of edit button to false if not the owner
                    if(!application.getUserName().equals(owner.getText().toString().substring(7))){
                        editbutton.setVisibility(View.GONE);
                    }
                    goingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, going);
                    goingList.setAdapter(goingAdapter);
                }
            }
        }
        //set going/not going button visibility false if is guest or owner
        if(going.contains(application.getUserName())){
            goingButton.setVisibility(View.GONE);
        }
        if(application.isGuest()){
            goingButton.setVisibility(View.GONE);
            notGoingButton.setVisibility(View.GONE);
        }
        //edit button action
        date.setFocusable(false);
        theater.setFocusable(false);
        final MovieEvent finalMovieEvent = movieEvent;
        editbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        date.setTextIsSelectable(true);
                        date.setFocusableInTouchMode(true);
                        theater.setTextIsSelectable(true);
                        theater.setFocusableInTouchMode(true);
                        //can edit date and theater
                        if(editbutton.getText().equals("Edit"))
                        {
                            date.setFocusable(true);
                            date.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInputFromWindow(date.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                }
                            });
                            theater.setFocusable(true);
                            theater.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInputFromWindow(date.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                }
                            });
                            editbutton.setText("Finish Editing");
                        }
                        //can no longer edit and sends update to server
                        else{
                            date.setFocusable(false);
                            date.setOnClickListener(null);
                            theater.setFocusable(false);
                            theater.setOnClickListener(null);
                            editbutton.setText("Edit");
                            MovieKnightAppli application = (MovieKnightAppli) getApplication();
                            Log.d("date", date.getText().toString());
                            MovieEvent me = new MovieEvent();
                            me.setEventID(eventID);
                            me.setOwner(finalMovieEvent.getOwner());
                            Log.d("ID", ""+finalMovieEvent.getGoingToWatch());
                            me.setGoingToWatch(finalMovieEvent.getGoingToWatch());
                            me.setDescription(finalMovieEvent.getDescription());
                            me.setPublic_private(finalMovieEvent.isPublic_private());
                            me.setMovieTime(date.getText().toString());
                            me.setTheater(theater.getText().toString());
                            me.setInvited( finalMovieEvent.getInvited());
                            Object[] objects = {"Edit Movie Request", me};
                            ClientListener cl= application.getClisten();
                            cl.clientRequest(objects);
                        }
                    }
                }
        );


        goingButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //update server
                        MovieKnightAppli application = (MovieKnightAppli) getApplication();
                        Object[] objects = {"Event Reply Request", eventID, application.getUserName(),true};
                        ClientListener cl= application.getClisten();
                        cl.clientRequest(objects);
                        //update lists
                        invited.remove(application.getUserName());
                        going.add(application.getUserName());
                        goingAdapter.notifyDataSetChanged();
                        invitedAdapter.notifyDataSetChanged();
                    }
                }
        );

        notGoingButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //update server
                        MovieKnightAppli application = (MovieKnightAppli) getApplication();
                        Object[] objects = {"Event Reply Request", eventID, application.getUserName(), false};
                        ClientListener cl= application.getClisten();
                        cl.clientRequest(objects);
                        //go back to event list page
                        startActivity(new Intent(getApplicationContext(), EventListActivity.class));
                       // finish();
                    }
                }
        );


        //something something Comment area functionality
    }


    private void getMovieInfo(int id) {

        TmdbService movieService = ((MovieKnightAppli)getApplication()).getMovieService();
//        Log.d("movieID is", ""+id);

        Call<MovieInfo> infoCall = movieService.getMovieDetails(id, TmdbConnector.API_KEY);

        infoCall.enqueue(new Callback<MovieInfo>() {
            @Override
            public void onResponse(Response<MovieInfo> response) {
                MovieInfo info = response.body();
                if(info != null)
                    setupDetails(info);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    private void setupDetails(MovieInfo info) {
        Picasso.with(this)
                .load(info.getPosterPath())
                .into(eventImage);
    }
}
