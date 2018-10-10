package com.example.nathan.movieknight.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

     if(id == R.id.nav_search){
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        //    finish();
        } else if (id == R.id.nav_profile) {
            MovieKnightAppli application = (MovieKnightAppli)getApplication();
            if(application.isGuest()){
                 PopUp();

            }else{

                Bundle b = new Bundle();
                b.putBoolean("user", true);
                Intent in = new Intent(getApplicationContext(), ProfileActivity.class);
                in.putExtras(b);
                startActivity(in);
          //      finish();
            }

        } else if (id == R.id.nav_movies) {
            startActivity(new Intent(getApplicationContext(), MovieListActivity.class));
          //  finish();
        } else if (id == R.id.nav_events) {
            MovieKnightAppli application = (MovieKnightAppli)getApplication();
            if(application.isGuest()){
                PopUp();
            }else {
                startActivity(new Intent(getApplicationContext(), EventListActivity.class));
         //      finish();
            }

        }  else if(id == R.id.nav_friend_requests){
            MovieKnightAppli application = (MovieKnightAppli)getApplication();
            if(application.isGuest()){
             PopUp();

            }else {
                startActivity(new Intent(getApplicationContext(), FriendRequestsActivity.class));
         //       finish();
            }
        } else if(id == R.id.nav_public_events){
            startActivity(new Intent(getApplicationContext(), PublicEventsActivity.class));

        } else if(id == R.id.nav_log_out){
         startActivity(new Intent(getApplicationContext(), LoginActivity.class));
         finish();
     }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        helpDialog.show();;
    }
}
