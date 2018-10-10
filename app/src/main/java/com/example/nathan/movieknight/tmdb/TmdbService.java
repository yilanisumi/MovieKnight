package com.example.nathan.movieknight.tmdb;

import com.example.nathan.movieknight.models.MovieInfo;
import com.example.nathan.movieknight.models.MovieResults;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by chaitanyap7 on 4/13/16.
 */
public interface TmdbService {

    //to get movies now playing in theaters
    @GET("/3/movie/now_playing")
    Call<MovieResults> getNowPlayingMovies(@Query("api_key") String apiKey);

    //to get movies coming soon to theaters
    @GET("/3/movie/upcoming")
    Call<MovieResults> getUpcomingMovies(@Query("api_key") String apiKey);

    //to get movies that are rated high by tmdb users
    @GET("/3/movie/top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String apiKey);

    //to get a list of movies that match search string
    @GET("/3/search/movie")
    Call<MovieResults> searchMovies(@Query("api_key") String apiKey, @Query("query") String searchQuery);

    //to get details about a particular movie
    @GET("/3/movie/{id}")
    Call<MovieInfo> getMovieDetails(@Path("id") Integer movieId, @Query("api_key") String apiKey);
}