package com.example.nathan.movieknight.models;

/**
 * Created by nathan on 4/6/2016.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.nathan.movieknight.R;

import java.util.ArrayList;
import java.util.Vector;

public class EventList extends ArrayAdapter<String> implements Filterable {
    private Filter eventFilter;
    private final Activity context;
    private Vector<String> eventNames;
    private Vector<String> oldeventNames;
    private Vector<String> eventDate;
    private Vector<String> eventTheaters;
    public EventList(Activity context,
                     Vector<String> eventNames,  Vector<String> eventDate, Vector<String> eventTheaters) {

        super(context, R.layout.list_single_event, eventNames);

        this.context = context;
        this.eventNames = eventNames;
        oldeventNames = new Vector<String>();
        for(String event : eventNames){
            oldeventNames.add(event);
        }
        this.eventDate = eventDate;
        this.eventTheaters = eventTheaters;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_event, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listeventtxt);
        TextView txtdate = (TextView)rowView.findViewById(R.id.listeventdateTime);
        TextView txttheater = (TextView)rowView.findViewById(R.id.listeventmovieTheater);
        if(position<eventNames.size()){
            txtTitle.setText(eventNames.get(position));

            txtdate.setText(eventDate.get(position));
            txttheater.setText(eventTheaters.get(position));
        }


        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (eventFilter == null)
            eventFilter = new eventFilter();

        return eventFilter;
    }

    private class eventFilter extends Filter {
        ArrayList<Integer> positions;
        boolean noMatch = true;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> meventList = new ArrayList<String>();
            positions = new ArrayList<Integer>();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                noMatch = false;
                // No filter implemented we return all the list
                results.values = oldeventNames;
                results.count = oldeventNames.size();
            }
            else {
                noMatch = true;
                // We perform filtering operation
                int count = 0;
                for (String event : oldeventNames) {
                    if (event.toUpperCase().startsWith(constraint.toString().toUpperCase())){
                        meventList.add(event);
                        positions.add(count);
                    }
                    count++;
                }

                results.values = meventList;
                results.count = meventList.size();

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
                    addAll(oldeventNames);
                    eventNames = oldeventNames;

                }

            }
            else {
                clear();
                eventNames = (Vector<String>) results.values;
                addAll(eventNames);
                notifyDataSetChanged();

            }

        }

    }




}
