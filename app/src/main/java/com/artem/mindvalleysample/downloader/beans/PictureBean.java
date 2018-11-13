package com.artem.mindvalleysample.downloader.beans;

public class PictureBean {
    String id; // short version of JSON
    String full;

    public PictureBean(String id, String full) {
        this.id = id;
        this.full = full;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getFullPicture() {
        return full;
    }
}
