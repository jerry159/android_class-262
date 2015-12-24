package com.example.user.simeplui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jerry on 2015/12/17.
 */
public class SimpleUIApplication extends Application{

   @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
    }
}
