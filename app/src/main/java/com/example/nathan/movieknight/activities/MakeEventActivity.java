package com.example.nathan.movieknight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.InvitedFriendList;
import com.example.nathan.movieknight.models.MovieEvent;
import com.example.nathan.movieknight.models.Profile;

import java.util.Vector;

/**
 * Created by natha on 4/6/2016.
 */
public class MakeEventActivity extends NavigationDrawer {
    ListView list;
    Vector<String> friendList;
    Vector<String> checkedList;
    Integer[] imageId ;
    public void updateCheckList(int pos, String username){
        int index = checkedList.indexOf(username);
        if(index == -1){
            checkedList.add(username);
        } else{
            checkedList.remove(username);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_event);

        Bundle b = getIntent().getExtras();
        final int movieID = b.getInt("movieID");
        final String movieName = b.getString("movieName");
        TextView nameTextView = (TextView)findViewById(R.id.movieText);
        nameTextView.setText(movieName);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);


        friendList = new Vector<String>();
        checkedList = new Vector<String>();
       // checkedList.add(((MovieKnightAppli)getApplication()).getUserName());

        Object[] objects ={"Profile Request", application.getUserName()};
        ClientListener cl= application.getClisten();
        Profile prof = null;
        if(cl!= null){
            prof = (Profile) cl.clientRequest(objects);
        }
        friendList = prof.getFriends();
        imageId = new Integer[friendList.size()];
        for(int i = 0; i < friendList.size();i++){
            imageId[i] = R.drawable.movieknight;
        }

        final InvitedFriendList adapter = new
                InvitedFriendList(this, friendList, imageId);

        list=(ListView)findViewById(R.id.friendlistView);
        //list not showing
        //  list=(ListView) LayoutInflater.from(getApplication()).inflate(R.layout.coming_soon_layout, null);
        if(list != null) {

            if (adapter != null) {
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                    }
                });
            }
        } else{
            System.out.println("null");
        }




        final Button makeeventbutton = (Button)findViewById(R.id.make_event);
        makeeventbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String owner = ((MovieKnightAppli)getApplication()).getUserName();
                        boolean public_private = ((RadioGroup)findViewById(R.id.privacy_radio_group)).indexOfChild(((RadioGroup)findViewById(R.id.privacy_radio_group)).findViewById(((RadioGroup)findViewById(R.id.privacy_radio_group)).getCheckedRadioButtonId())) == 0;
                        String EventTitle = movieName;
                        String time = ((EditText)findViewById(R.id.dateTime)).getText().toString();
                        String location = ((EditText)findViewById(R.id.location)).getText().toString();
                        Vector<String> invitations = new Vector<String>(checkedList);
                        MovieEvent me = null;
                        MovieKnightAppli application = (MovieKnightAppli) getApplication();
                        Object[] objects = {"Make Event", owner, movieID, EventTitle, public_private, time, location, invitations};
                        ClientListener cl= application.getClisten();

                        if(cl != null){
                            me = (MovieEvent) cl.clientRequest(objects);
                            if (me != null) {
                                makeeventbutton.setVisibility(View.GONE);
                                String eid = me.getEventID();
                                Bundle b = new Bundle();
                                Intent in = new Intent(getApplicationContext(), EventActivity.class);
                                b.putString("eventID", eid);
                                in.putExtras(b);
                                startActivity(in);
                               // finish();
                            }
                        }




                    }
                }
        );
    }
}
