package com.example.togata.parsetagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by togata on 7/9/18.
 */

@ParseClassName("Post")
public class Post extends ParseObject{

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_USER = "user";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_CREATED_AT = "createdAt";

    public Post(){}

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String update){
        put(KEY_DESCRIPTION, update);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile update){
        put(KEY_IMAGE, update);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseObject update){
        put(KEY_USER, update);
    }

    public Date getDate(){
        return getCreatedAt();
    }
    public void setDate(Date update){
        put(KEY_CREATED_AT, update);
    }


    public static class Query extends ParseQuery<Post>{
        public Query(){
            super(Post.class);
        }

        public Query getTop(){
            setLimit(20);
            return this;
        }

        public Query withUser(){
            include("user");
            return this;
        }
    }
}
