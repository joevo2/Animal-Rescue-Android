package com.alpacalab.joel.animalrescue;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Joel on 1/20/15.
 */
@ParseClassName("Rescue")
public class Rescue extends ParseObject{
    public Rescue() {

    }

    public void setDescription(String desc) {
        put("description",desc);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setImage() {

    }

    public void setLocation(double lad, double lon) {
        ParseGeoPoint point = new ParseGeoPoint(lad, lon);
        put("location", point);
    }

    public void setRescueStatus(boolean status) {
        put("status",status);
    }

    public boolean getRescueStatus() {
        return getBoolean("status");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }
}
