package com.alpacalab.joel.animalrescue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

/**
 * Created by Joel on 1/20/15.
 */
@ParseClassName("Rescue")
public class Rescue extends ParseObject implements Parcelable {
    Bitmap bmp;

    public Rescue() {

    }

    public void setDescription(String desc) {
        put("description",desc);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setAnimal(String animal) {
        put("animal", animal);
    }

    public String getAnimal() {
        return getString("animal");
    }

    public void setImage(Uri uri, Bitmap bitmap) {
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        // Create the ParseFile
        String path = uri.getPath();
        String idStr = path.substring(path.lastIndexOf('/') + 1);
        ParseFile file = new ParseFile(idStr, image);
        Log.d("IMAGE URI",idStr);
        file.saveInBackground();
        put("image", file);
        Log.d("IMAGE","Image saved");
    }

    public ParseFile getImage(){
        ParseFile fileObject = (ParseFile) get("image");
        return fileObject;
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


    protected Rescue(Parcel in) {
        bmp = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bmp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Rescue> CREATOR = new Parcelable.Creator<Rescue>() {
        @Override
        public Rescue createFromParcel(Parcel in) {
            return new Rescue(in);
        }

        @Override
        public Rescue[] newArray(int size) {
            return new Rescue[size];
        }
    };
}