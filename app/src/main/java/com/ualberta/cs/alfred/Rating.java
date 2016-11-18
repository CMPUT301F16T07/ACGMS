package com.ualberta.cs.alfred;

/**
 * This class contains and calculates ratings of drivers given by their riders
 *
 * Created by Avery on 11/18/2016.
 */
public class Rating {
    private float rating_total;
    private float rating;
    private int rating_count;


    /**
     * constructor for creating a new Rating object
     *
     *
     */
    public Rating(){
        this.rating_total = 0;
        this.rating_count = 0;
        this.rating = 0;
    }


    /**
     * returns the current rating of a Driver
     *
     *@return returns rating of driver
     */
    public float getRating() {
        return rating;
    }

    /**
     * Calculates the driver's new rating
     *
     * @param new_rating the new rating of the driver
     */
    public void updateRating(float new_rating) {
        this.rating_count = this.rating_count+1;
        this.rating_total = this.rating_total + new_rating;
        this.rating = this.rating_total/this.rating_count;
    }
}
