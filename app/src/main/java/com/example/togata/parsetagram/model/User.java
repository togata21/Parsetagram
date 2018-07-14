package com.example.togata.parsetagram.model;

import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by togata on 7/13/18.
 */

@ParseClassName("User")
public class User extends ParseUser{
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_HANDLE = "password";


    public User(){
    }

    public String getUsername(){
        return getString(KEY_USERNAME);
    }
    public void setUsername(String update){
        put(KEY_USERNAME, update);
    }

    public String getPassword(){
        return getString(KEY_PASSWORD);
    }
    public void setPassword(String update){
        put(KEY_PASSWORD, update);
    }

    public String getHandle(){
        return getString(KEY_HANDLE);
    }
    public void setHandle(String update){
        put(KEY_HANDLE, update);
    }

    public static class Query extends ParseQuery<User> {
        public Query(){
            super(User.class);
        }

        public User.Query getTop(){
            setLimit(20);
            return this;
        }

        public User.Query withUser(){
            include("user");
            return this;
        }
    }
}
