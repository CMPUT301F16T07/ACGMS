package com.ualberta.cs.alfred;

/**
 * Created by mmcote on 2016-11-08.
 */

public class UserCache {
    private User user;
    private RequestList requests;

    public UserCache() {
        this.user = null;
        this.requests = null;
    }

    public UserCache(UserCache retrieved) {
        this.user = retrieved.user;
        this.requests = retrieved.requests;
    }

    public User getUser() {
        return user;
    }

    public RequestList getRequests() {
        return requests;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRequests(RequestList requests) {
        this.requests = requests;
    }
}
