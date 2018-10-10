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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nathan.movieknight.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

public class MovieList extends ArrayAdapter<String> implements Filterable {
    private Filter movieFilter;
    private final Activity context;
    private Vector<String> movieNames;
    private Vector<String> oldMovieNames;
    private  Vector<String> movieImages;
    private  Vector<String> oldMovieImages;
    public MovieList(Activity context,
                     Vector<String> movieNames, Vector<String> movieImages) {

        super(context, R.layout.list_single_movie, movieNames);
        this.context = context;
        this.movieNames = movieNames;
        oldMovieNames = new Vector<String>();

        for (String movie : this.movieNames) {
            oldMovieNames.add(movie);
        }

        this.movieImages = movieImages;
        oldMovieImages = new Vector<String>();
        for(String movieimg : this.movieImages){
            oldMovieImages.add(movieimg);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_movie, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listmovietxt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.listmovieimg);

        if(position<movieNames.size())
            txtTitle.setText(movieNames.get(position));
        if(position < movieImages.size())
            Picasso.with(context)
                    .load(movieImages.get(position))
                    .into(imageView);

        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (movieFilter == null)
            movieFilter = new MovieFilter();

        return movieFilter;
    }

    private class MovieFilter extends Filter {
        ArrayList<Integer> positions;
        boolean noMatch = true;
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String> mMovieList = new ArrayList<String>();
            positions = new ArrayList<Integer>();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                noMatch = false;
                // No filter implemented we return all the list
                results.values = oldMovieNames;
                results.count = oldMovieNames.size();
                movieImages.clear();

                for(int i = 0; i < oldMovieImages.size(); i++){
                    movieImages.add(oldMovieImages.get(i));
                }

            }
            else {
                noMatch = true;
                // We perform filtering operation
                int count = 0;
                for (String movie : oldMovieNames) {
                    if (movie.toUpperCase().startsWith(constraint.toString().toUpperCase())){
                        mMovieList.add(movie);
                        positions.add(count);
                    }
                    count++;
                }

                results.values = mMovieList;
                results.count = mMovieList.size();

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
                    addAll(oldMovieNames);
                    movieNames = oldMovieNames;

                    movieImages.clear();

                    for(int i = 0; i < oldMovieImages.size(); i++){
                        movieImages.add(oldMovieImages.get(i));
                    }
                }

            }
            else {
                clear();
                movieNames = (Vector<String>) results.values;

                addAll(movieNames);

                if(positions.size() > 0){
                    movieImages.clear();

                    for(int i = 0; i < oldMovieImages.size(); i++){
                        movieImages.add(oldMovieImages.get(i));
                    }
                }
                notifyDataSetChanged();
            }
        }
    }
}
