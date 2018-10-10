package com.example.nathan.movieknight.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chaitanyap7 on 4/15/16.
 */
public class MovieInfo {

    private String IMAGE_PATH = "http://image.tmdb.org/t/p/w342";

    @SerializedName("original_title")
    private String movieTitle;

    @SerializedName("overview")
    private String movieOverview;

    @SerializedName("tagline")
    private String movieTagline;

    @SerializedName("poster_path")
    private String moviePosterPath;

    @SerializedName("runtime")
    private String movieRuntime;

    @SerializedName("vote_average")
    private String movieVoteAverage;

    @SerializedName("vote_count")
    private String movieVoteCount;

    @SerializedName("release_date")
    private String movieReleaseDate;

    @SerializedName("genres")
    private List<Genre> movieGenres;


    public String getTitle() {
        return movieTitle;
    }

    public String getOverview() {
        return movieOverview;
    }

    public String getTagLine() {
        return movieTagline;
    }

    public String getPosterPath() {
        return IMAGE_PATH + moviePosterPath;
    }

    public String getRuntime() {
        return movieRuntime;
    }

    public String getVoteAverage() {
        return movieVoteAverage;
    }

    public String getVoteCount() {
        return movieVoteCount;
    }

    public List<Genre> getGenres() {
        return movieGenres;
    }

    public String getReleaseDate() {
        return movieReleaseDate;
    }

    public static class Genre {
        @SerializedName("name")
        private String movieName;

        public String getName() {
            return movieName;
        }
    }
}