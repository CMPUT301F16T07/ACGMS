package com.ualberta.cs.alfred;

/**
 * The type Vehicle.
 *
 * @author ookmm
 * @version 1.0
 */
public class Vehicle {

    private String serialNumber;
    private String plateNumber;
    private String type;
    private String make;
    private String model;
    private int year;
    private String color;

    /**
     * Instantiates a new Vehicle.
     *
     * @param serialNumber the serial number
     * @param plateNumber  the plate number
     * @param type         the type
     * @param make         the make
     * @param model        the model
     * @param year         the year
     * @param color        the color
     */
    public Vehicle(String serialNumber, String plateNumber, String type, String make,
                   String model, int year, String color) {
        this.serialNumber = serialNumber;
        this.plateNumber = plateNumber;
        this.type = type;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    /**
     * Gets serial number.
     *
     * @return the serial number
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets serial number.
     *
     * @param serialNumber the serial number
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Gets plate number.
     *
     * @return the plate number
     */
    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * Sets plate number.
     *
     * @param plateNumber the plate number
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets make.
     *
     * @return the make
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets make.
     *
     * @param make the make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets year.
     *
     * @param year the year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(String color) {
        this.color = color;
    }
}
