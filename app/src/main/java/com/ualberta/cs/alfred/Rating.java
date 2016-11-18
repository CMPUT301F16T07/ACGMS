package com.ualberta.cs.alfred;

/**
 * Created by Avery on 11/18/2016.
 */

public class Rating {
    private float rating_total;
    private float rating;
    private int rating_count;

    public Rating(){
        this.rating_total = 0;
        this.rating_count = 0;
        this.rating = 0;
    }


    public float getRating() {
        return rating;
    }

    public void updateRating(float new_rating) {
        this.rating_count = this.rating_count+1;
        this.rating_total = this.rating_total + new_rating;
        this.rating = this.rating_total/this.rating_count;
    }
}
