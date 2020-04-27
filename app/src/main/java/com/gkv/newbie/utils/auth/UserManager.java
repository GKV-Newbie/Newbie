package com.gkv.newbie.utils.auth;

import com.gkv.newbie.model.User;

public class UserManager {

    private static UserManager instance;

    private String AuthToken;

    private User user;

    private UserManager(){

    }

    public static UserManager getInstance() {
        if(instance==null)
            instance = new UserManager();
        return instance;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
