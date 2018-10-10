package com.example.nathan.movieknight.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.nathan.movieknight.fragments.EventTabFragment;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.Profile;

import java.util.ArrayList;
import java.util.Vector;


public class EventListActivity extends NavigationDrawer {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    ArrayAdapter<String> goingAdapter;
    ArrayAdapter<String> invitedAdapter;
    Vector<String> invitedEvents;
    Vector<String> goingEvents;
    String filterText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

         MovieKnightAppli application = (MovieKnightAppli) getApplication();
        Object[] objects ={"Profile Request", application.getUserName()};
        ClientListener cl= application.getClisten();
        Profile prof = null;
        if(cl!= null){
            prof = (Profile) cl.clientRequest(objects);
        }
        invitedEvents = new Vector<String>();
        invitedEvents = prof.getEvents();
        goingEvents = new Vector<String>();
        goingEvents = prof.getEventRequests();


        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new EventTabFragment(this)).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setGoingAdapter(ArrayAdapter<String> adapter){
        goingAdapter = adapter;
    }
    public void setInvitedAdapter(ArrayAdapter<String> adapter){
        invitedAdapter = adapter;
    }
    public Vector<String> getInvitedEvents(){
        return invitedEvents;
    }
    public Vector<String> getGoingEvents(){
        return goingEvents;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_movies, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) EventListActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(EventListActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String text) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String text) {
                    filterText = text;
                    updateAdapters();
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }
    public void updateAdapters(){
        if(goingAdapter != null)
            goingAdapter.getFilter().filter(filterText);
        if(invitedAdapter != null)
            invitedAdapter.getFilter().filter(filterText);

    }
}

