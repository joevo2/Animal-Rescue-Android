package com.alpacalab.joel.animalrescue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
public class Rescue extends ParseObject{
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
//        // Locate the class table named "ImageUpload" in Parse.com
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Rescue");
//        // Locate the objectId from the class
//        query.getInBackground("", new GetCallback<ParseObject>() {
//            public void done(ParseObject object, ParseException e) {
//                // Locate the column named "ImageName" and set
//                // the string
//                ParseFile fileObject = (ParseFile) get("image");
//                fileObject.getDataInBackground(new GetDataCallback() {
//                    public void done(byte[] data, ParseException e) {
//                        if (e == null) {
//                            Log.d("ImageFeed", "We've got data in data.");
//                            // Decode the Byte[] into
//                            // Bitmap
//                            bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                            // Get the ImageView from
//                            // main.xml
//
//                            //ImageView image = (ImageView) findViewById(R.id.image);
//
//                            // Set the Bitmap into the
//                            // ImageView
//
//                            //image.setImageBitmap(bmp);
//                        } else {
//                            Log.d("ImageFeed", "There was a problem downloading the data.");
//                        }
//                    }
//
//                });
//            }
//        });
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
}
