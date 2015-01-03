package com.alpacalab.joel.animalrescue;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by Joel on 1/3/15.
 */
public class App extends Application{
    @Override public void onCreate() {
        super.onCreate();

    // Parse intialisation
        if (!ParseCrashReporting.isCrashReportingEnabled()) {
            ParseCrashReporting.enable(this);
        }
        Parse.initialize(this, "H4ZKGFj7YJCYpy8UV1n0ZYGzEO4lMK5OMC5LXBIx", "tkrSkoYrZIYmVVzIuolxL8bV7N9iZDCFnkfCQqMm");    }
}
