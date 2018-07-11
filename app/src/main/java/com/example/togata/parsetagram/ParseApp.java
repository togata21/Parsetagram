package com.example.togata.parsetagram;

import android.app.Application;

import com.example.togata.parsetagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by togata on 7/9/18.
 */

public class ParseApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId("hashtagParsing")
                .clientKey("rickAndMorty")
                .server("http://parsetagram12.herokuapp.com/parse")
                .build();
        Parse.initialize(config);
        //Parse.enableLocalDatastore(this);
    }
}
