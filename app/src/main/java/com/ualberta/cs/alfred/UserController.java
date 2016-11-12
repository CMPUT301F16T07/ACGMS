package com.ualberta.cs.alfred;

/**
 * This is the user controller class.
 * This will be the class the will be used by the user view to access the user model
 *
 * @author ccastell
 * @version 1.0
 */

public class UserController {
    /**
     * This will be the instance of the user and it'll be store in this
     * class so that the view can access it's information
     */
    User user;

    /**
     * Initiates user controller
     *  @param user an instance of a user.
     */
    public UserController(User user) {
        this.user = user;
    }


    /**
     * Get the full name of the user
     *
     * @return fullname
     */
    public String getFullName() {
        String firstname = user.getFirstName();
        String lastname = user.getLastName();
        String fullname = firstname + lastname;
        return fullname;
    }

    /**
     * Get the username of the user
     *
     * @return user ID
     */
    public String getUserName() {
        return this.user.getUserID();
    }

    /**
     * Gets the email address of the user
     *
     * @return useremail
     */
    public String getEmailAddress() {
        return this.user.getEmail();
    }
}
