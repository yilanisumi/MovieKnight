package com.example.nathan.movieknight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;

import java.util.Vector;

/**
 * Created by natha on 4/6/2016.
// */
public class SearchActivity extends NavigationDrawer {
    ListView lv;
    SearchView sv;
    Vector<String> friends;

    ArrayAdapter<String> friendAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);
        lv = (ListView)findViewById(R.id.listView);
        sv = (SearchView)findViewById(R.id.searchView2);

        Object[] objects = {"List All Users"};
        friends = new Vector<String>();
        friends = (Vector<String>)((MovieKnightAppli)getApplication()).getClisten().clientRequest(objects);

        friendAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends);
        lv.setAdapter(friendAdapter);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                    friendAdapter.getFilter().filter(text);

                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle b = new Bundle();
                Intent in;
                in = new Intent(getApplicationContext(), ProfileActivity.class);
                String name = friendAdapter.getItem(position);
                if(name.equals(application.getUserName())){
                    b.putBoolean("user", true);
                } else{
                    b.putString("friend", friendAdapter.getItem(position));
                    b.putBoolean("user", false);
                }

                in.putExtras(b);
                startActivity(in);
            //    finish();
            }
        });

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
