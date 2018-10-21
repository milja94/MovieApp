package com.example.milja.movieapp.io.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MovieCredits implements Serializable {
    @SerializedName("crew")
    private List<Crew> crew;

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }
}
