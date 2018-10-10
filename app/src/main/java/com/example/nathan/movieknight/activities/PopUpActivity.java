package com.example.nathan.movieknight.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.nathan.movieknight.R;

public class PopUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
        Bundle b = getIntent().getExtras();
        if(b != null){
           boolean popup = b.getBoolean("popup");
            if(popup){
                FriendRequestPopUp();
            } else{
                EventListPopUp();
            }
        }

    }

    private void FriendRequestPopUp(){
        AlertDialog.Builder movielistBuilder = new AlertDialog.Builder(this);
        movielistBuilder.setTitle("Someone add you as a friend!");
        movielistBuilder.setPositiveButton("Go to Friend Requests",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), FriendRequestsActivity.class));
                        //finish();
                    }
                });
        movielistBuilder.setNegativeButton("Go back",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      finish();
                    }
                });
        AlertDialog movielistDialog = movielistBuilder.create();
        movielistDialog.show();
    }
    private void EventListPopUp(){
        AlertDialog.Builder movielistBuilder = new AlertDialog.Builder(this);
        movielistBuilder.setTitle("Someone invited you to an event!");
        movielistBuilder.setPositiveButton("Go to Events Page",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), EventListActivity.class));
                    }
                });
        movielistBuilder.setNegativeButton("Go Back",
                new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog movielistDialog = movielistBuilder.create();
        movielistDialog.show();
    }
}
