package com.alpacalab.joel.animalrescue;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

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

    public void setImage(Uri uri, Bitmap bitmap) {
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        // Create the ParseFile
        ParseFile file = new ParseFile(uri.toString(), image);
        try
        {
            file.save();
            put("Image", file);
            Log.d("IMAGE","Image saved");
        }
        catch (ParseException e)
        {
            Log.d("IMAGE","Image not saved");
            e.printStackTrace();
        }

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
