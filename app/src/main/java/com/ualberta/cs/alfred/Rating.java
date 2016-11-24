package com.ualberta.cs.alfred;

/**
 * This class contains and calculates ratings of drivers given by their riders
 *
 * Created by Avery on 11/18/2016.
 */
public class Rating {
    private double total;
    private int count;


    /**
     * constructor for creating a new Rating object
     *
     *
     */
    public Rating(){
        this.total = 0.0;
        this.count = 0;
    }


    /**
     * returns the current rating of a Driver
     *
     *@return returns rating of driver
     */
    public double getRating() {

        double rating;

        if (this.count == 0) {
            rating = 0.0;
        } else {
            rating = this.total / this.count;
        }

        return rating;
    }

    /**
     * Calculates the driver's new rating
     *
     * @param newRating the new rating of the driver
     */
    public void addRating(double newRating) {
        this.count = this.count + 1;
        this.total = this.total + newRating;
    }
}
