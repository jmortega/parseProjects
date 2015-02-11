package com.androidbegin.parselogintutorial;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import android.app.Application;
 
public class ParseApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
        Parse.initialize(this, "rz05IhFzx1dFvQs8Y8RW8BDdrSARJ9V2I5Gu91JK", "iNBQitoUMUBBdTaklwaDRTewgdTufvXfZqKCEuyZ");
        
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}