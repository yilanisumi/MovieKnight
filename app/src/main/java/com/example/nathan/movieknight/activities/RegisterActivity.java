package com.example.nathan.movieknight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.PasswordEncryptor;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.Profile;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);
        Button guestbutton = (Button)findViewById(R.id.guest_button);
        guestbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        application.setIsGuest(true);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
        );

        Button existingbutton = (Button)findViewById(R.id.existing_button);
        existingbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
        );

        Button registerbutton = (Button)findViewById(R.id.register_button);
        registerbutton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        String dname = ((EditText)findViewById(R.id.displayName)).getText().toString();
                        String zcode = ((EditText)findViewById(R.id.zipcode)).getText().toString();
                        String pword = ((EditText)findViewById(R.id.password)).getText().toString();
                        String repword = ((EditText)findViewById(R.id.re_password)).getText().toString();
                        Boolean isInt = true;
                        if(zcode.equals("")){
                            isInt = false;
                        }
                        for(int i = 0; i < zcode.length(); i++){
                            if(zcode.charAt(i) < '0' || zcode.charAt(i) > '9'){
                                isInt = false;
                            }
                        }
                        int zipcode = -1;
                        if(isInt)
                            zipcode = Integer.parseInt(zcode);
                        TextView Error = (TextView)findViewById(R.id.errorText);
                        if(!isInt){
                            Error.setText("Zipcode not a number");
                        }else if(dname.equals("")  || zcode.equals("") || pword.equals("") || repword.equals("")){
                            Error.setText("Fill in all the forms");
                        } else if (!pword.equals(repword)) {
                            //display "Error: passwords do not match"
                            Error.setText("Passwords do not match");
                        } else{
                            Object[] objects = {"Register", dname, PasswordEncryptor.encrypt(pword), zipcode};

                            ClientListener cl = application.getClisten();
                            if(cl != null){
                                Profile prof = (Profile) cl.clientRequest(objects);
                                if(prof != null){
                                    application.setUserProfile(prof);
                                    application.setUserName(dname);
                                    if(!cl.isAlive())
                                        cl.start();
                                    startActivity(new Intent(getApplicationContext(), MovieListActivity.class));
                                    finish();
                                } else{
                                    Error.setText("Username already taken");
                                }
                            }

                        }

                        //if all the fields are not null
                        //send a request to the server with relevant information and register the user
                    }
                }
        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
