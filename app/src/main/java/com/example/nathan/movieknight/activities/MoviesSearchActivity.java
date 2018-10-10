package com.example.nathan.movieknight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.MovieList;

import java.util.ArrayList;
import java.util.Vector;

public class MoviesSearchActivity extends NavigationDrawer {


    MovieList adapter;
    ListView list;

    Vector<String> movieNames;
    Vector<String> movieImages;
    Vector<Integer> movieIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        movieNames = new Vector<String>();
        movieImages = new Vector<String>();
        movieIDs = new Vector<Integer>();
        MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);

        Bundle b = getIntent().getExtras();
        ArrayList<Integer> mid = b.getIntegerArrayList("movieIDs");
        for (Integer i : mid)
            movieIDs.add(i);
        ArrayList<String> mn = b.getStringArrayList("movieNames");
        for (String s : mn)
            movieNames.add(s);
        ArrayList<String> mi = b.getStringArrayList("movieImages");
        for (String s : mi)
            movieImages.add(s);

        View view = this.findViewById(android.R.id.content);
        list = (ListView) view.findViewById(R.id.movieListView);


        adapter = new MovieList(this, movieNames, movieImages);

        if (list != null) {

            if (adapter != null) {
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent(getApplicationContext(), MovieActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("movieID", movieIDs.get(position));
                        in.putExtras(bundle);
                        startActivity(in);
                    }
                });
            }
        } else {
            Log.d("TopRatedFragment", "null");
        }
    }

}