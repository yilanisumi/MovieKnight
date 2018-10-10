package com.example.nathan.movieknight.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.PasswordEncryptor;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.MovieInfo;
import com.example.nathan.movieknight.models.Profile;
import com.example.nathan.movieknight.tmdb.TmdbConnector;
import com.example.nathan.movieknight.tmdb.TmdbService;

import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity  {



    // UI references.
    private EditText mUsername;
    private EditText mPasswordView;


    TmdbConnector tmdbConnector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MovieKnightAppli application = (MovieKnightAppli)getApplication();
     //   application.getClisten().start();
      application.setCurrentContext(this);
        // Set up the login form.
        mUsername = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                return false;
            }
        });

        Button logInButton = (Button) findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString();
                String password = mPasswordView.getText().toString();
                TextView errorText = (TextView) findViewById(R.id.errorText);
                if (username.equals("") || password.equals("")) {
                    errorText.setText("Fill in all the forms");
                } else {
                    MovieKnightAppli application = (MovieKnightAppli) getApplication();
                    Object[] objects = {"Login", username, PasswordEncryptor.encrypt(password)};
                    ClientListener cl= application.getClisten();
                    if(cl != null){
                        Profile prof = (Profile) cl.clientRequest(objects);
                        if(prof != null){
                            application.setUserProfile(prof);
                            application.setUserName(username);


                            setupUser();
                            if(!cl.isAlive())
                                cl.start();
                            startActivity(new Intent(getApplicationContext(), MovieListActivity.class));
                            finish();
                        } else{
                            errorText.setText("Log in failed");
                        }
                    }

                }
            }
        });

        Button registerbutton = (Button) findViewById(R.id.register_button);
        registerbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });

        Button guestbutton = (Button) findViewById(R.id.guest_button);
        guestbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MovieKnightAppli) getApplication()).setIsGuest(true);
                startActivity(new Intent(getApplicationContext(), MovieListActivity.class));
                finish();
            }
        });


        tmdbConnector = new TmdbConnector((MovieKnightAppli) getApplication());

        tmdbConnector.getMovies("TopRated");
        tmdbConnector.getMovies("Upcoming");
        tmdbConnector.getMovies("NowPlaying");

    }

    private void setupUser() {
        Profile prof = ((MovieKnightAppli)getApplication()).getUserProfile();
        Vector<Integer> movieID = prof.getWatched();
        for (Integer i : movieID)
            getMovieInfo(i, 0);
        movieID = prof.getToWatch();
        for (Integer i : movieID)
            getMovieInfo(i, 1);
        movieID = prof.getLiked();
        for(Integer i : movieID)
            getMovieInfo(i,2);

    }
    public void FriendRequestPopUp(){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Friend Request");
        helpBuilder.setMessage("You received a friend request!");
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

    private void getMovieInfo(int id, final int w) {
        TmdbService movieService = ((MovieKnightAppli)getApplication()).getMovieService();
//        Log.d("movieID is", ""+id);

        Call<MovieInfo> infoCall = movieService.getMovieDetails(id, TmdbConnector.API_KEY);

        infoCall.enqueue(new Callback<MovieInfo>() {
            @Override
            public void onResponse(Response<MovieInfo> response) {
                MovieInfo info = response.body();
                if(info != null) {
                    if (w == 0) {
                        Vector<String> s = ((MovieKnightAppli) getApplication()).getUserProfile().getWatchedName();
                        if (s == null) {
                            Vector<String> t = new Vector<String>();
                            t.add(info.getTitle());
                            ((MovieKnightAppli) getApplication()).getUserProfile().setWatchedName(t);
                        }
                        else
                            s.add(info.getTitle());
                    }
                    else if(w==1){
                        Vector<String> s = ((MovieKnightAppli) getApplication()).getUserProfile().getToWatchName();
                        if (s == null) {
                            Vector<String> t = new Vector<String>();
                            t.add(info.getTitle());
                            ((MovieKnightAppli) getApplication()).getUserProfile().setToWatchName(t);
                        }
                        else
                            s.add(info.getTitle());
                    } else{
                        Vector<String> s = ((MovieKnightAppli) getApplication()).getUserProfile().getLikedName();
                        if (s == null) {
                            Vector<String> t = new Vector<String>();
                            t.add(info.getTitle());
                            ((MovieKnightAppli) getApplication()).getUserProfile().setLikedName(t);
                        }
                        else
                            s.add(info.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}

