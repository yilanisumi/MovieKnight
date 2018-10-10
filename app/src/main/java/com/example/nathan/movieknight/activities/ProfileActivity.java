package com.example.nathan.movieknight.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.Profile;

import java.util.Vector;

public class ProfileActivity extends NavigationDrawer {
    TextView username;
    EditText description;
    MovieKnightAppli application;
    Profile userProfile;
    boolean isUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);
        Bundle b = getIntent().getExtras();
        if(b != null){
            isUser = b.getBoolean("user");
        }
        username = (TextView) findViewById(R.id.profile_name);
        description = (EditText) findViewById(R.id.profile_description);
        description.setFocusable(false);
        //this button should apppear for other users but not self
        //ie getIntent.getExtras.getBoolean(friend) == true
        Button addfriendbutton = (Button)findViewById(R.id.addFriendButton);
        addfriendbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //sends a friend request
                        //makes a popup notifying success
                        Object[] objects ={"Friend Request", application.getUserName(),username.getText().toString()};
                        ClientListener cl= application.getClisten();
                        boolean friendRequest;
                        if(cl!= null){
                            friendRequest = (Boolean) cl.clientRequest(objects);
                        }


                    }
                }
        );

        //this button should apppear for self but not other users
        //ie getIntent.getExtras.getBoolean(friend) == false
        final Button editbutton = (Button)findViewById(R.id.editButton);
        editbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        description.setTextIsSelectable(true);
                        description.setFocusableInTouchMode(true);
                        if(editbutton.getText().equals("Edit"))
                        {
                            description.setFocusable(true);
                               description.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInputFromWindow(description.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                }
                            });
                            editbutton.setText("Finish Editing");
                        }
                        else{
                            description.setFocusable(false);
                            description.setOnClickListener(null);
                            editbutton.setText("Edit");
                            MovieKnightAppli application = (MovieKnightAppli) getApplication();
                            Object[] objects = {"Update Personal Description", description.getText().toString(), username.getText().toString()};
                            ClientListener cl= application.getClisten();
                            cl.clientRequest(objects);
                        }

                    }
                }
        );

        Button friendlistbutton = (Button)findViewById(R.id.friendListButton);
        friendlistbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(), FriendListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username", username.getText().toString());
                        in.putExtras(bundle);
                        startActivity(in);
                    }
                }
        );


        Button movielistbutton = (Button)findViewById(R.id.movieListButton);
        movielistbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        //open up the movie list activity
                        startActivity(new Intent(getApplicationContext(), ProfileMovieListActivity.class));
                    }
                }
        );
        Object[] objects ={"Profile Request", application.getUserName()};
        ClientListener cl= application.getClisten();
        Profile prof = null;
        if(cl!= null){
           prof = (Profile) cl.clientRequest(objects);
        }

        //checks if it's the user
        if(isUser){
            if(prof != null) {
                application.setUserProfile(prof);
            }
            userProfile = application.getUserProfile();
            username.setText(userProfile.getUsername());
            description.setText(userProfile.getDescription());
            addfriendbutton.setVisibility(View.GONE);
            ListView favList = (ListView)findViewById(R.id.favMoviesList);
            Vector<String> favoriteMoviesNames = userProfile.getLikedName();

            final Vector<Integer> favoriteMovieIDs = userProfile.getLiked();
            if(favoriteMoviesNames== null){
                favoriteMoviesNames = new Vector<String>();
            }

           ArrayAdapter<String>  favoriteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, favoriteMoviesNames);
            if(favList!= null && favoriteAdapter != null && favoriteMovieIDs != null){

                favList.setAdapter(favoriteAdapter);
                favList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent(getApplicationContext(), MovieActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("movieID", favoriteMovieIDs.get(position));
                        in.putExtras(bundle);
                        startActivity(in);
                      //  finish();
                    }
                });
            }
        } else{
            editbutton.setVisibility(View.GONE);
            //change this once we get movie list names on server side
            movielistbutton.setVisibility(View.GONE);
            MovieKnightAppli mka = ((MovieKnightAppli)getApplication());
            if(b!= null){
                String friendUsername = b.getString("friend");
                //make add friend button invisible
                if(application.isGuest()){
                    addfriendbutton.setVisibility(View.GONE);
                } else {
                    Vector<String> friends = application.getUserProfile().getFriends();
                    if(friends.contains(friendUsername)){
                        addfriendbutton.setVisibility(View.GONE);
                    }
                }

                Object[] objects2 = { "Profile Request", friendUsername };
                userProfile = (Profile)mka.getClisten().clientRequest(objects2);
                username.setText(userProfile.getUsername());
                description.setText(userProfile.getDescription());
            }

        }

    }


}
