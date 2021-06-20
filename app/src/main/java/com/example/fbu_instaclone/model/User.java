package com.example.fbu_instaclone.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

    public static final String KEY_PROFILE_PIC = "profilePic";

    public ParseFile getProfilePic(){
        return getParseFile(KEY_PROFILE_PIC);
    }

    public void setProfilePic(ParseFile image){
        put(KEY_PROFILE_PIC, image);
    }
}
