package com.example.nathan.movieknight.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.nathan.movieknight.ClientListener;
import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.models.FriendList;
import com.example.nathan.movieknight.models.InvitedFriendList;
import com.example.nathan.movieknight.models.Profile;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by natha on 4/6/2016.
 */
public class FriendListActivity extends AppCompatActivity {
    ListView lv;
    SearchView sv;
    Vector<String> friendsList;
    Integer[] imageId;
    FriendList friendAdapter;
    boolean movieMode = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        MovieKnightAppli application = (MovieKnightAppli)getApplication();
        application.setCurrentContext(this);
        lv = (ListView)findViewById(R.id.listView);
        sv = (SearchView)findViewById(R.id.searchView2);
        Bundle b = getIntent().getExtras();
        String username = b.getString("username");
        Object[] objects ={"Profile Request", username};
        ClientListener cl= application.getClisten();
        Profile prof = null;

        prof = (Profile) cl.clientRequest(objects);

        friendsList = new Vector<String>();
        friendsList = prof.getFriends();
        imageId = new Integer[friendsList.size()];
        for (int i = 0; i < friendsList.size(); i++) {
            imageId[i] = R.drawable.movieknight;
        }
        if (friendsList != null && imageId != null) {
            friendAdapter = new FriendList(this, friendsList, imageId);
            lv.setAdapter(friendAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent in = new Intent( getApplicationContext(), ProfileActivity.class);
                    Bundle b = new Bundle();
                    b.putString("friend", friendsList.get(position));
                    b.putBoolean("user", false);
                    in.putExtras(b);
                    startActivity(in);
                   // finish();
                }
            });
        }

        if (sv != null)
            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String text) {
                return false;
            }

                @Override
                public boolean onQueryTextChange(String text) {
                    friendAdapter.getFilter().filter(text);
                    return false;
                }
            });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }
}
