package com.example.smistry.parsetagram;

import android.app.Application;

import com.example.smistry.parsetagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("smistry-parsing")
                .clientKey("tigersAreAdorable1115")
                .server("http://smistry923-parsetagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
