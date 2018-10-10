package com.example.nathan.movieknight.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chaitanyap7 on 4/16/16.
 */
public class MovieBox {

    private String IMAGE_PATH = "http://image.tmdb.org/t/p/w342";

    @SerializedName("title")
    private String movieTitle;

    @SerializedName("poster_path")
    private String moviePosterPath;

    @SerializedName("release_date")
    private String movieReleaseDate;

    @SerializedName("vote_average")
    private String movieVoteAverage;

    @SerializedName("vote_count")
    private String movieVoteCount;

    @SerializedName("id")
    private Integer movieId;

    @SerializedName("overview")
    private String movieOverview;

    public String getOverview() {
        return movieOverview;
    }

    public String getTitle() {
        return movieTitle;
    }

    public String getPosterPath(){
        return IMAGE_PATH+moviePosterPath;
    }

    public String getReleaseDate() {
        return movieReleaseDate;
    }

    public String getVoteAverage() {
        return movieVoteAverage;
    }

    public String getVoteCount() {
        return movieVoteCount;
    }

    public Integer getId() {
        return movieId;
    }
}