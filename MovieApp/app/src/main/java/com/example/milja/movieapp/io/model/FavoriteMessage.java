package com.example.milja.movieapp.io.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavoriteMessage implements Serializable {
   @SerializedName("status_code")
    private int code;
    @SerializedName("status_message")
        private String statusMessage;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
