package com.example.fbu_instaclone.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment(){}

    public static final String KEY_BODY = "body";
    public static final String KEY_USER = "createdBy";
    public static final String KEY_POST = "relatingPost";

    public String getBody(){ return getString(KEY_BODY); }

    public void setBody(String body) { put(KEY_BODY, body); }

    public ParseUser getUser(){ return getParseUser(KEY_USER); }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public void setPost(Post post) { put(KEY_POST, post); }

    public Post getPost() { return (Post) getParseObject(KEY_POST); }
}
