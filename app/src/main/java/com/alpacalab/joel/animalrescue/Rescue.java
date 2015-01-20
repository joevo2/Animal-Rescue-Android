package com.alpacalab.joel.animalrescue;

import com.parse.ParseClassName;
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

    public void setStatus(boolean status) {
        put("status",status);
    }

    public boolean getStatus() {
        return getBoolean("status");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }
}
