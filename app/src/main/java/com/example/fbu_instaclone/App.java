package com.example.fbu_instaclone;

import android.app.Application;
import android.util.Log;

import com.example.fbu_instaclone.model.Comment;
import com.example.fbu_instaclone.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class App extends Application {

    public static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }

    public static void changeLikeStatus(Post post, boolean status){
        ParseQuery<Post> query = ParseQuery.getQuery("Post");

        // Retrieve the object by id
        query.getInBackground(post.getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                object.put(Post.KEY_LIKES, post.getLikes());
                object.put(Post.KEY_LIKE_STATUS, status);
                //All other fields will remain the same
                object.saveInBackground();
            } else {
                // something went wrong
                Log.e(TAG, "Error: " + e.getMessage());
            }
        });
    }
}
