package com.example.nathan.movieknight.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.MovieInfo;
import com.example.nathan.movieknight.tmdb.TmdbConnector;
import com.example.nathan.movieknight.tmdb.TmdbService;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class MovieActivity extends NavigationDrawer {

    private TextView movieName;
    private ImageView moviePoster;
    private TextView movieRating;
    private TextView movieReleaseDate;
    private TextView movieRuntime;
    private TextView movieSynopsis;
    private String movieNameString;
    int movieID;
    private ProgressDialog progress_dialog;

    private DateFormat movieDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

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
        movieID = b.getInt("movieID");

        setupMoviePage();
        displayProgressMessage();
        getMovieInfo(movieID);

        Button makeeventbutton = (Button)findViewById(R.id.makeEventButton);
        makeeventbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if(application.isGuest()){
                            PopUp();

                        } else{
                            //open up the movie list activity
                            Bundle b = new Bundle();
                            b.putInt("movieID", movieID);
                            b.putString("movieName", movieNameString);
                            Intent in = new Intent(getApplicationContext(), MakeEventActivity.class);
                            in.putExtras(b);

                            startActivity(in);

                        //    finish();
                        }

                    }
                }
        );
        Button addFavoritesButton = (Button) findViewById(R.id.addFavoritesButton);
        addFavoritesButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        if(application.isGuest()){
                            PopUp();
                        } else {
                            MovieKnightAppli mka = ((MovieKnightAppli)getApplication());
                            Object[] objects = { "Add To Liked", movieID, mka.getUserName(), movieNameString};
                            mka.getClisten().clientRequest(objects);
                            mka.getUserProfile().getLiked().add(movieID);
                            if (mka.getUserProfile().getLikedName() != null)
                                mka.getUserProfile().getLikedName().add(movieNameString);
                            else {
                                Vector<String> s = new Vector<String>();
                                s.add(movieNameString);
                                mka.getUserProfile().setLikedName(s);
                            }
                            Bundle b = new Bundle();
                            b.putBoolean("user", true);
                            Intent in = new Intent(getApplicationContext(), ProfileActivity.class);
                            in.putExtras(b);
                            startActivity(in);
                        //    finish();
                        }
                    }
                }
        );
        Button addWatchListButton = (Button) findViewById(R.id.addWatchlistButton);
        addWatchListButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        if(application.isGuest()){
                            PopUp();
                        } else {
                            AskForDest();
                        }
                    }
                }
        );
    }

    void AskForDest() {
        AlertDialog.Builder movielistBuilder = new AlertDialog.Builder(this);
        movielistBuilder.setTitle("Add Movie As...");
        movielistBuilder.setPositiveButton("Watched",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MovieKnightAppli mka = ((MovieKnightAppli)getApplication());
                        Object[] objects = { "Add To Watched", movieID, mka.getUserName(), movieNameString};
                        mka.getClisten().clientRequest(objects);
                        mka.getUserProfile().getWatched().add(movieID);
                        if (mka.getUserProfile().getWatchedName() != null)
                            mka.getUserProfile().getWatchedName().add(movieNameString);
                        else {
                            Vector<String> s = new Vector<String>();
                            s.add(movieNameString);
                            mka.getUserProfile().setWatchedName(s);
                        }
                        startActivity(new Intent(getApplicationContext(), ProfileMovieListActivity.class));
                    }
                });
        movielistBuilder.setNegativeButton("To Watch",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MovieKnightAppli mka = ((MovieKnightAppli)getApplication());
                        Object[] objects = { "Add To To Watch", movieID, mka.getUserName(), movieNameString};
                        mka.getClisten().clientRequest(objects);
                        mka.getUserProfile().getToWatch().add(movieID);
                        if (mka.getUserProfile().getToWatchName() != null)
                            mka.getUserProfile().getToWatchName().add(movieNameString);
                        else {
                            Vector<String> s = new Vector<String>();
                            s.add(movieNameString);
                            mka.getUserProfile().setToWatchName(s);
                        }
                        startActivity(new Intent(getApplicationContext(), ProfileMovieListActivity.class));
                    }
                });
        AlertDialog movielistDialog = movielistBuilder.create();
        movielistDialog.show();
    }

    void PopUp(){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("YOU ARE A GUEST");
        helpBuilder.setMessage("Cannot access as guest. Please log in first.");
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    private void setupMoviePage() {

        movieName = (TextView) findViewById(R.id.movieTitle);

        moviePoster = (ImageView) findViewById(R.id.moviePoster);

        movieRating = (TextView) findViewById(R.id.movieRated);

        movieReleaseDate = (TextView) findViewById(R.id.movieReleaseDate);

        movieRuntime = (TextView) findViewById(R.id.movieRuntime);

        movieSynopsis = (TextView) findViewById(R.id.movieSynopsis);

        movieDateFormat = android.text.format.DateFormat.getDateFormat(this);
    }


    private void getMovieInfo(int id) {

        TmdbService movieService = ((MovieKnightAppli)getApplication()).getMovieService();
//        Log.d("movieID is", ""+id);

        Call<MovieInfo> infoCall = movieService.getMovieDetails(id,TmdbConnector.API_KEY);

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

        movieName.setText(info.getTitle());
        movieNameString = info.getTitle();
        Log.d("MVNameString",movieNameString);
        movieRating.setText("Rating: "+info.getVoteAverage() + "/10" + "("+info.getVoteCount()+" votes)");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date movDate = sdf.parse(info.getReleaseDate());
            String releaseDate = movieDateFormat.format(movDate);
            movieReleaseDate.setText("Release Date: " + releaseDate);
        } catch (java.text.ParseException e) {
        }

        movieRuntime.setText("Runtime: "+info.getRuntime() + " min");

        movieSynopsis.setText(info.getOverview());

        Picasso.with(this)
                .load(info.getPosterPath())
                .into(moviePoster);

        progress_dialog.dismiss();
    }


    private void displayProgressMessage() {
        progress_dialog = new ProgressDialog(MovieActivity.this);
        progress_dialog.setMessage("Loading..");
        progress_dialog.show();
    }

}
