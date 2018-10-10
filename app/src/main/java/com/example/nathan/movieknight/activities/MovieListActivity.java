package com.example.nathan.movieknight.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.fragments.MovieTabFragment;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.MovieBox;
import com.example.nathan.movieknight.models.MovieResults;
import com.example.nathan.movieknight.tmdb.TmdbConnector;
import com.example.nathan.movieknight.tmdb.TmdbService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


public class MovieListActivity extends NavigationDrawer {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    ArrayAdapter<String> bluAdapter;
    ArrayAdapter<String> comingAdapter;
    ArrayAdapter<String> theatersAdapter;
    //new
    ArrayList<String> movieNames;
    ArrayList<String> movieImages;
    ArrayList<Integer> movieIDs;
    List<MovieBox> moviesFound;

    private ProgressDialog progress_dialog;

    String filterText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //new
        movieNames = new ArrayList<String>();
        movieImages = new ArrayList<String>();
        movieIDs = new ArrayList<Integer>();

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new MovieTabFragment(this)).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setBluAdapter(ArrayAdapter<String> adpater){
        bluAdapter = adpater;
    }
    public void setComingAdapter(ArrayAdapter<String> adpater){
        comingAdapter = adpater;
    }
    public void setTheatersAdapter(ArrayAdapter<String> adpater){
        theatersAdapter = adpater;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_movies, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MovieListActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MovieListActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String text) {
                    displayProgressMessage();
                    getMovieResults(text);

                    Intent in = new Intent(getApplicationContext(), MoviesSearchActivity.class);
                    Bundle bundle = new Bundle();
                    //only create activity if there are movies to send
                    if(movieIDs.size() > 0) {
                        bundle.putIntegerArrayList("movieIDs", movieIDs);
                        bundle.putStringArrayList("movieNames", movieNames);
                        bundle.putStringArrayList("movieImages", movieImages);
                        in.putExtras(bundle);
                        startActivity(in);
                    }
                    progress_dialog.dismiss();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String text) {
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }
    public void updateAdapters(){
        if(bluAdapter != null)
            bluAdapter.getFilter().filter(filterText);
        if(comingAdapter != null)
            comingAdapter.getFilter().filter(filterText);
        if(theatersAdapter != null)
            theatersAdapter.getFilter().filter(filterText);
    }


    //new, called in onCreate to setup API request
    private void getMovieResults(String movieName) {

        TmdbService movieService = ((MovieKnightAppli)getApplication()).getMovieService();

        Call<MovieResults> infoCall = movieService.searchMovies(TmdbConnector.API_KEY, movieName);

        infoCall.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Response<MovieResults> response) {
                MovieResults results = response.body();
                if(results != null) {
                    setUpDetails(results);
                } else {
                    Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    //new, called by getMovieResults to populate ArrayLists
    private void setUpDetails(MovieResults results) {
        moviesFound = results.getMovies();

        if(moviesFound.size() == 0) {
            Toast.makeText(getApplicationContext(), "No results found!", Toast.LENGTH_LONG).show();
        } else {
            String works = moviesFound.get(0).getTitle();
            //populating movies Arraylists
            for (int i = 0; i < moviesFound.size(); i++) {
                movieNames.add(moviesFound.get(i).getTitle());
                movieIDs.add(moviesFound.get(i).getId());
                movieImages.add(moviesFound.get(i).getPosterPath());
            }
        }
    }

    private void displayProgressMessage() {
        progress_dialog = new ProgressDialog(MovieListActivity.this);
        progress_dialog.setMessage("Loading..");
        progress_dialog.show();
    }

}

