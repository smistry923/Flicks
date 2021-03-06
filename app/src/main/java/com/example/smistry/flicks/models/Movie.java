package com.example.smistry.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

public @Parcel class Movie  {
    // values from API
    public String title;
    public String overview;
    public String posterPath; // only the path
    public String backdropPath;
    public Double voteAverage;
    public Integer id;


    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Double getVoteAverage() {
        return voteAverage;
    }

    //initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id"); //TODO

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Integer getId() {
        return id;
    }
}
