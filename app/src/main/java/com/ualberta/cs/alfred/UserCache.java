package com.ualberta.cs.alfred;

/**
 * cache used to store user data while offline
 */

public class UserCache {
    private User user;
    private RequestList requests;

    /**
     * constructor to create new usercache
     *
     *
     */
    public UserCache() {
        this.user = null;
        this.requests = null;
    }
    /**
     * constructor to create new usercache
     *
     *@param retrieved retrieved usercache
     */
    public UserCache(UserCache retrieved) {
        this.user = retrieved.user;
        this.requests = retrieved.requests;
    }

    /**
     * Gets user
     *
     * @return user
     *
     *
     */
    public User getUser() {
        return user;
    }


    /**
     * Gets request
     *
     *@return request
     */
    public RequestList getRequests() {
        return requests;
    }


    /**
     * sets user
     *
     *@param user user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * sets request
     *
     *
     *@param requests request
     *
     */
    public void setRequests(RequestList requests) {
        this.requests = requests;
    }
}
