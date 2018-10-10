package com.example.nathan.movieknight.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.fragments.ProfileMovieListTabFragment;


public class ProfileMovieListActivity extends AppCompatActivity {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    ArrayAdapter<String> goingToWatchAdapter;
    ArrayAdapter<String> watchedAdapter;

    String filterText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                        .replace(R.id.containerView, new ProfileMovieListTabFragment(this))
                        .commit();
    }

    public void setGoingToWatchAdapter(ArrayAdapter<String> adapter){
        goingToWatchAdapter = adapter;
    }
    public void watchedAdapter(ArrayAdapter<String> adapter){
        watchedAdapter = adapter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_movies, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) ProfileMovieListActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ProfileMovieListActivity.this.getComponentName()));
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
        if(goingToWatchAdapter != null)
            goingToWatchAdapter.getFilter().filter(filterText);
        if(watchedAdapter != null)
            watchedAdapter.getFilter().filter(filterText);

    }
}

