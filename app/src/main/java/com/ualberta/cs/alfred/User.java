package com.ualberta.cs.alfred;

import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * The user class holds all of the common data needed as both a rider and a driver.
 * All users will be contained on the Elastic Search server where they will pulled
 * when a user logs in to the local device.
 *
 * @author mmcote
 * @version 1.1
 *
 * @see RiderInfo
 * @see DriverInfo
 */
public class User {
    /** userID is the most consistent piece of data for a user profile. It will remain
     * the same for the lifetime of the user. Whereas all other attributes are visible
     * and able to be changed by the user.
     */
    @JestId
    private String userID;
    /**
     * userName is a shorthand named picked by the user, that will be used to access
     * their account.
     */
    private String userName;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String email;
    private Boolean isRider;
    private Boolean isDriver;
    private RiderInfo riderInfo;
    private DriverInfo driverInfo;

    /**
     * Instantiates a new User.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param userName    the user name
     * @param dateOfBirth the date of birth
     * @param phoneNumber the phone number
     * @param email       the email
     */
    public User(String firstName, String lastName, String userName, Date dateOfBirth,
                String phoneNumber, String email) {
        this.userID = null;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isRider = Boolean.FALSE;
        this.isDriver = Boolean.FALSE;
        this.save();
    }

    /**
     * Instantiates a new User.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param userName    the user name
     * @param dateOfBirth the date of birth
     * @param phoneNumber the phone number
     * @param email       the email
     * @param riderInfo   the explicit rider info
     */
    public User(String firstName, String lastName, String userName, Date dateOfBirth,
                String phoneNumber, String email, RiderInfo riderInfo) {
        this(firstName, lastName, userName, dateOfBirth, phoneNumber, email);
        this.riderInfo = riderInfo;
        this.isRider = Boolean.TRUE;
        this.isDriver = Boolean.FALSE;
    }

    /**
     * Instantiates a new User.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param userName    the user name
     * @param dateOfBirth the date of birth
     * @param phoneNumber the phone number
     * @param email       the email
     */
    public User(String firstName, String lastName, String userName, Date dateOfBirth,
                String phoneNumber, String email, DriverInfo driverInfo) {
        this(firstName, lastName, userName, dateOfBirth, phoneNumber, email);
        this.driverInfo = driverInfo;
        this.isDriver = Boolean.TRUE;
        this.isRider = Boolean.FALSE;
    }

    /**
     * Instantiates a new User that is both a rider and a driver.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param userName    the user name
     * @param dateOfBirth the date of birth
     * @param phoneNumber the phone number
     * @param email       the email
     */
    public User(String firstName, String lastName, String userName, Date dateOfBirth,
                String phoneNumber, String email, RiderInfo riderInfo, DriverInfo driverInfo) {
        this(firstName, lastName, userName, dateOfBirth, phoneNumber, email);
        this.driverInfo = driverInfo;
        this.riderInfo = riderInfo;
        this.isDriver = Boolean.TRUE;
        this.isRider = Boolean.TRUE;
    }


    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
        this.isDriver = Boolean.TRUE;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    public void setRiderInfo(RiderInfo riderInfo) {
        this.riderInfo = riderInfo;
        this.isRider = Boolean.TRUE;
    }

    public Boolean getIsRider() {
        return isRider;
    }

    public Boolean getIsDriver() {
        return isDriver;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets date of birth.
     *
     * @return the date of birth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets date of birth.
     *
     * @param dateOfBirth the date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets user id.
     *
     * @param userID the user id
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    private void save(){
        UserESAddController.AddUserTask<User> addUserTask = new UserESAddController.AddUserTask<User>();
        addUserTask.execute(this);
    }
}
