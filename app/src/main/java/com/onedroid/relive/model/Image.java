package com.onedroid.relive.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Data model for Event.
 */
public class Image {

    private  int resourceId;
    private  int likes;
    private  HashMap<String,Boolean> liked;

    public Image(int resourceId, int likes, HashMap<String, Boolean> liked) {
        this.resourceId = resourceId;
        this.likes = likes;
        this.liked = liked;
    }

    public int getResouceId() {
        return resourceId;
    }

    public int getLikes() {
        return likes;
    }

    public HashMap<String, Boolean> getLiked() {
        return liked;
    }

    public void setResourceId(int resouceId) {
        this.resourceId = resouceId;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setLiked(HashMap<String, Boolean> liked) {
        this.liked = liked;
    }
}
