package com.example.nathan.movieknight.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.activities.MovieActivity;
import com.example.nathan.movieknight.activities.MovieListActivity;
import com.example.nathan.movieknight.models.MovieList;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Nathan on 3/17/2016.
 */
public class ComingSoonFragment  extends Fragment  {
    MovieList adapter;
    ListView list;
    Vector<String> movieList;
    Vector<String> movieImages;
    Vector<Integer> movieID;
    final MovieListActivity movieListActivity;
    @SuppressLint("ValidFragment")
    public ComingSoonFragment(MovieListActivity ma){
        super();
        movieListActivity = ma;
        movieList = new Vector<String>();
        movieImages = new Vector<String>();
        movieID = new Vector<Integer>();
        MovieKnightAppli application = (MovieKnightAppli)ma.getApplication();
        ArrayList<String> ml = application.getMovieListUpcoming();
        for (String s : ml)
            movieList .add(s);
        ArrayList<String> mi = application.getMovieImagesUpcoming();
        for (String s : mi)
            movieImages.add(s);
        ArrayList<Integer> mid = application.getMovieIDUpcoming();
        for (Integer i : mid)
            movieID.add(i);
    }
    public ComingSoonFragment(){
        super();
        movieListActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.coming_soon_layout,null);

        MovieKnightAppli application = (MovieKnightAppli)movieListActivity.getApplication();
        application.setCurrentContext(inflater.getContext());

        list=(ListView) view.findViewById(R.id.comingsoonlistView);
        adapter = new
                MovieList(movieListActivity, movieList, movieImages);
        movieListActivity.setComingAdapter((adapter));
        if(list != null) {

            if (adapter != null) {
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent in = new Intent(movieListActivity.getApplicationContext(), MovieActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("movieID", movieID.get(position));
                        in.putExtras(bundle);
                        startActivity(in);
                 //       movieListActivity.finish();
                    }
                });
            }
        } else{
            System.out.println("null");
        }

        return view;
    }
}
