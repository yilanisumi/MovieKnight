package com.example.nathan.movieknight.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.FriendRequestsList;
import com.example.nathan.movieknight.models.Profile;

import java.util.Vector;

/**
 * Created by natha on 4/6/2016.
 */
public class FriendRequestsActivity extends NavigationDrawer {
    ListView lv;
    SearchView sv;
    Vector<String> friendsList;
    Integer[] imageId;
    boolean movieMode = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);
        setContentView(R.layout.activity_friend_requests);
        lv = (ListView)findViewById(R.id.listView);
        Object[] objects ={"Profile Request", application.getUserName()};
        ClientListener cl= application.getClisten();
        Profile prof = null;
        if(cl!= null){
            prof = (Profile) cl.clientRequest(objects);
        }
        friendsList = new Vector<String>();
        friendsList = prof.getFriendRequests();
        imageId = new Integer[friendsList.size()];
        for (int i = 0; i < friendsList.size(); i++) {
            imageId[i] = R.drawable.movieknight;
        }
        if (imageId != null && friendsList != null) {
            FriendRequestsList adapter = new FriendRequestsList(this, friendsList, imageId);
            lv.setAdapter(adapter);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
