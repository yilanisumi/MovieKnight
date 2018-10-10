package com.example.nathan.movieknight.models;

/**
 * Created by nathan on 4/6/2016.
 */

import android.content.Intent;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.activities.FriendListActivity;
import com.example.nathan.movieknight.activities.FriendRequestsActivity;

import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

public class FriendRequestsList extends ArrayAdapter<String>  {
    private final Activity context;
    private Vector<String> friendNames;
    private  Integer[] friendImage;

    public FriendRequestsList(Activity context,
                              Vector<String> friendNames, Integer[] friendImage) {

        super(context, R.layout.list_single_friend_request, friendNames);
        this.context = context;
        this.friendNames = friendNames;
        this.friendImage = friendImage;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_friend_request, null, true);
        final TextView txtTitle = (TextView) rowView.findViewById(R.id.listfriendtxt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.listfriendimg);
        if(position<friendNames.size())
            txtTitle.setText(friendNames.get(position));
        if(position < friendImage.length)
            imageView.setImageResource(friendImage[position]);

        Button acceptButton = (Button) rowView.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieKnightAppli mka = ((MovieKnightAppli)context.getApplication());
                Object[] objects = { "Friend Request Reply", mka.getUserName(), txtTitle.getText().toString(), true };
                mka.getClisten().clientRequest(objects);
                //friendNames.remove(txtTitle.getText().toString());

                context.startActivity(new Intent(context.getApplicationContext(), FriendRequestsActivity.class));
                context.finish();
            }
        });
        Button ignoreButton = (Button) rowView.findViewById(R.id.ignoreButton);
        ignoreButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieKnightAppli mka = ((MovieKnightAppli)context.getApplication());
                Object[] objects = {"Friend Request Reply", mka.getUserName(), txtTitle.getText().toString(), false };
                mka.getClisten().clientRequest(objects);
                context.startActivity(new Intent(context.getApplicationContext(), FriendRequestsActivity.class));
                context.finish();
            }
        });
        return rowView;
    }





}
