package com.example.nathan.movieknight.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.activities.MovieActivity;
import com.example.nathan.movieknight.activities.ProfileMovieListActivity;

import com.example.nathan.movieknight.models.MovieList;

import java.util.Vector;



/**
 * Created by Nathan on 3/17/2016.
 */
public class WatchedFragment  extends Fragment  {
    ListView list;
    Vector<Integer> movieID;
    Vector<String> movieList;
    Vector<String> movieImages;
    final ProfileMovieListActivity profileMovieListActivity;
    @SuppressLint("ValidFragment")
    public WatchedFragment(ProfileMovieListActivity ea){
        super();
        profileMovieListActivity = ea;
    }
    public WatchedFragment(){
        super();
        profileMovieListActivity = null;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.in_theaters_layout,null);
        movieID = ((MovieKnightAppli)profileMovieListActivity.getApplication()).getUserProfile().getWatched();
        movieList = ((MovieKnightAppli)profileMovieListActivity.getApplication()).getUserProfile().getWatchedName();
        if(movieList == null){
            movieList = new Vector<String>();
        }
        ArrayAdapter<String> adapter = new
                ArrayAdapter<String>(profileMovieListActivity, android.R.layout.simple_list_item_1,movieList);
        MovieKnightAppli application = (MovieKnightAppli)profileMovieListActivity.getApplication();
        application.setCurrentContext(inflater.getContext());

        list=(ListView)view.findViewById(R.id.intheaterslistView);
        profileMovieListActivity.setGoingToWatchAdapter((adapter));

        if(list != null) {

            if (adapter != null) {
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent(  profileMovieListActivity.getApplicationContext(), MovieActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("movieID", movieID.get(position));
                        in.putExtras(b);
                        startActivity(in);
                   //  profileMovieListActivity.finish();
                    }
                });
            }
        } else{
            System.out.println("null");
        }

        return view;
    }
}
