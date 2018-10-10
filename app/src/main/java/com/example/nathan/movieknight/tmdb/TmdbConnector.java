package com.example.nathan.movieknight.tmdb;


import android.util.Log;

import com.example.nathan.movieknight.MovieKnightAppli;
import com.example.nathan.movieknight.models.MovieBox;
import com.example.nathan.movieknight.models.MovieInfo;
import com.example.nathan.movieknight.models.MovieResults;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by chaitanyap7 on 4/13/16.
 */
public class TmdbConnector {

    private static String TAG = "TmdbConnector";
    public static final String API_KEY = "85380ca322256320bbf2c30c06d6c080";
    public static final String API_URL = "http://api.themoviedb.org";
    List<MovieBox> moviesReceived;
    List<MovieBox> theatersReceivedList;
    List<MovieBox> topRatedReceivedList;
    List<MovieBox> comingSoonReceivedList;

    TmdbService movieService;

    MovieKnightAppli application;

    public TmdbConnector(MovieKnightAppli app) {
        application = app;
        Retrofit retro = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieService = retro.create(TmdbService.class);
        setService();

        Runnable theaters = new Runnable() {
            public void run() {

                while(theatersReceivedList == null ){

                }

                for(int i = 0; i < theatersReceivedList.size(); i++) {
                    application.getMovieListIn().add(theatersReceivedList.get(i).getTitle());
                    application.getMovieIDIn().add(theatersReceivedList.get(i).getId());
                    application.getMovieImagesIn().add(theatersReceivedList.get(i).getPosterPath());

                }

            }
        };
        Runnable comingSoon = new Runnable() {
            public void run() {

                while(comingSoonReceivedList == null){

                }

                for(int i = 0; i < comingSoonReceivedList.size(); i++) {
                  application.getMovieListUpcoming().add(comingSoonReceivedList.get(i).getTitle());
                   application.getMovieIDUpcoming().add(comingSoonReceivedList.get(i).getId());
                    application.getMovieImagesUpcoming().add(comingSoonReceivedList.get(i).getPosterPath());

                }
            }
        };
        Runnable topRated = new Runnable() {
            public void run() {

                while(topRatedReceivedList == null ){

                }

                for(int i = 0; i < topRatedReceivedList.size(); i++) {
                   application.getMovieListTop().add(topRatedReceivedList.get(i).getTitle());
                   application.getMovieIDTop().add(topRatedReceivedList.get(i).getId());
                   application.getMovieImagesTop().add(topRatedReceivedList.get(i).getPosterPath());

                }

            }
        };
        Thread theaterThread = new Thread(theaters);
        Thread comingSoonThread = new Thread(comingSoon);
        Thread topRatedThread = new Thread(topRated);
        theaterThread.start();
        comingSoonThread.start();
        topRatedThread.start();
    }
    

    //getting information for a single movie class
    public void getMovieDetails(Integer id){
        Call movieInfoCall = movieService.getMovieDetails(id, API_KEY);
        movieInfoCall.enqueue(new Callback<MovieInfo>() {
            @Override
            public void onResponse(Response<MovieInfo> response) {
                MovieInfo model = response.body();
                if (model == null)
                    return;
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    //function that returns search results
    public void searchMovies(String movieName){
        try{
            Call moviesCall = movieService.searchMovies(API_KEY, movieName);
            //       enqueueMovieResults(moviesCall);
        }catch (Exception e){
            String exception = e.getMessage();
        }
    }

    //getting information for a list of movies
    public void getMovies(String requestType){
        try{
            //default set to movies now in theaters
            Call moviesCall = movieService.getNowPlayingMovies(API_KEY);


            //getting the appropriate results from the TMDB API
            if(requestType.equals("Upcoming")) {
                moviesCall = movieService.getUpcomingMovies(API_KEY);
            } else if(requestType.equals("TopRated")) {
                moviesCall = movieService.getTopRatedMovies(API_KEY);
            } else if(requestType.equals("NowPlaying")) {
                moviesCall = movieService.getNowPlayingMovies(API_KEY);
            }

            enqueueMovieResults(moviesCall, requestType);

        } catch (Exception e){

            String exception = e.getMessage();
        }
    }

    //helper function to populate movie results
    private void enqueueMovieResults(Call moviesCall,final String requestType ){
        Callback<MovieResults> callBack = new Callback<MovieResults>(){

            @Override
            public void onResponse(Response<MovieResults> response) {
                MovieResults mResults = response.body();
                if (mResults != null) {
                    //updating the movie results where it was originally called
                    if(requestType.equals("Upcoming")) {
                        comingSoonReceivedList = mResults.getMovies();

                    } else if(requestType.equals("TopRated")) {
                        topRatedReceivedList = mResults.getMovies();

                    } else if(requestType.equals("NowPlaying")) {
                        theatersReceivedList = mResults.getMovies();

                    }

                    //  moviesReceived = mResults.getMovies();

                }
            }

            @Override
            public void onFailure(Throwable thr) {
                Log.d(TAG, thr.getMessage());

            }
        };
        moviesCall.enqueue(callBack);

    }

    public void setService() {
        application.setMovieService(movieService);
    }

}