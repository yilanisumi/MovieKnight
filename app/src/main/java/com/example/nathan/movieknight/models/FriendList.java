package com.example.nathan.movieknight.models;

/**
 * Created by nathan on 4/6/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nathan.movieknight.R;
import com.example.nathan.movieknight.activities.MakeEventActivity;

import java.util.ArrayList;
import java.util.Vector;

public class FriendList extends ArrayAdapter<String> implements Filterable {
    private Filter friendFilter;
    private final Activity context;
    private Vector<String> friendNames;
    private Vector<String> oldFriendNames;
    private  Integer[] friendImage;
    private  Integer[] oldFriendImage;
    public FriendList(Activity context,
                      Vector<String> friendNames, Integer[] friendImage) {

        super(context, R.layout.list_single_friendlist, friendNames);

        this.context = context;
        this.friendNames = friendNames;
        oldFriendNames = new Vector<String>();
        for(String friend : friendNames){
            oldFriendNames.add(friend);
        }
        oldFriendImage = new Integer[friendImage.length];
        this.friendImage = friendImage;
        for(int i = 0; i < friendImage.length;i++){
            oldFriendImage[i] = friendImage[i];
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_friendlist, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listfriendtxt);
        final int pos = position;
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listfriendimg);

        if(position<friendNames.size())
            txtTitle.setText(friendNames.get(position));
        if(position < friendImage.length)
            imageView.setImageResource(friendImage[position]);
        return rowView;
    }


    @Override
    public Filter getFilter() {
        if (friendFilter == null)
            friendFilter = new FriendFilter();

        return friendFilter;
    }

    private class FriendFilter extends Filter {
        ArrayList<Integer> positions;
        boolean noMatch = true;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> mFriendList = new ArrayList<String>();
            positions = new ArrayList<Integer>();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                noMatch = false;
                // No filter implemented we return all the list
                results.values = oldFriendNames;
                results.count = oldFriendNames.size();
                for(int i = 0; i < oldFriendImage.length; i++){
                    friendImage[i] = oldFriendImage[i];
                }
            }
            else {
                noMatch = true;
                // We perform filtering operation
                int count = 0;
                for (String friend : oldFriendNames) {
                    if (friend.toUpperCase().startsWith(constraint.toString().toUpperCase())){
                        mFriendList.add(friend);
                        positions.add(count);
                    }
                    count++;
                }
                results.values = mFriendList;
                results.count = mFriendList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0){
                notifyDataSetInvalidated();
                clear();
                if(!noMatch){
                    addAll(oldFriendNames);
                    friendNames = oldFriendNames;
                    friendImage = new Integer[oldFriendImage.length];
                    for(int i = 0; i < oldFriendImage.length; i++){
                        friendImage[i] = oldFriendImage[i];
                    }
                }
            }
            else {
                clear();
                friendNames = (Vector<String>) results.values;

                addAll(friendNames);
                if(positions.size() > 0){
                    friendImage = new Integer[positions.size()];
                    for(int i = 0; i < positions.size(); i++){
                        friendImage[i] = oldFriendImage[positions.get(i)];
                    }
                }
                notifyDataSetChanged();

            }

        }

    }




}
