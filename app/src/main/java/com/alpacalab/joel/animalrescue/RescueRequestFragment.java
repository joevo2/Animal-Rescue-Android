package com.alpacalab.joel.animalrescue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Joel on 1/3/15.
 */

public class RescueRequestFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //Camera2 API stuff
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int RESULT_GALLERY = 0;
    private boolean gallery = false;
    private Uri fileUri;
    private Bitmap bitmap;
    //Google Play Service
    private GoogleApiClient mGoogleApiClient;
    //Location
    private Location mLastLocation;

    //Widget
    private EditText mDesc;
    private ImageView mImage;
    private TextView mLocation;
    private RadioGroup mAnimal;
    private String animal;


    //Debug
    protected static final String TAG = "RescueRequest";

    public RescueRequestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rescue_request, container, false);

        //Register ParseObject subclass
        ParseObject.registerSubclass(Rescue.class);

        //Widget
        mImage = (ImageView) rootView.findViewById(R.id.picture_preview);
        mLocation = (TextView) rootView.findViewById(R.id.location);
        mDesc = (EditText) rootView.findViewById(R.id.rescue_desc_input);
        mAnimal = (RadioGroup) rootView.findViewById(R.id.animal_type);

        //Button
        Button mCamera = (Button) rootView.findViewById(R.id.camera);
        Button mGetLocation = (Button) rootView.findViewById(R.id.get_location);
        Button mRescueRequest = (Button) rootView.findViewById(R.id.rescue_submit_button);

        //Google Play Service
        buildGoogleApiClient();

        //Camera Button
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        mAnimal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cat:
                        animal = "cat";
                        break;
                    case R.id.dog:
                        animal = "dog";
                        break;
                    case R.id.others:
                        animal = "others";
                        break;
                }
            }
        });

        //Location Button
        mGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        //Rescue Request Button
        mRescueRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRescue();
            }
        });

        return rootView;
    }

    public void createRescue() {
        if (mDesc.getText().length() > 0 && mLastLocation != null && animal != null) {
            Rescue r = new Rescue();
            r.setDescription(mDesc.getText().toString());
            r.setAnimal(animal);
            //Image is not working yet
            //r.setImage(fileUri, bitmap);
            r.setLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            r.setRescueStatus(false);
            r.setACL(new ParseACL(ParseUser.getCurrentUser()));
            r.setUser(ParseUser.getCurrentUser());
            r.saveInBackground();
            mDesc.setText("");

            Toast.makeText(getActivity(),"Rescue Request Submited",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(),"Rescue Request Incomplete",Toast.LENGTH_SHORT).show();
        }


    }

    public void getLocation() {
        //Get location
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Toast.makeText(getActivity(),"Got Location",Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Location is available");
            mLocation.setText(String.valueOf(mLastLocation.getLatitude()+", "+mLastLocation.getLongitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        } else {
            Toast.makeText(getActivity(),"No Location is detected", Toast.LENGTH_LONG).show();
            Log.d(TAG,"Location is null");
        }
    }

    public void getImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.image_option_description)
                .setItems(R.array.image_option, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int list) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        //First option "Camera"
                        if (list == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

                            // start the image capture Intent
                            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                        //Second option "Gallery"
                        if (list == 1) {
                            Intent galleryIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent , RESULT_GALLERY );
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void previewCapturedImage() {
        try {
            // bitmap factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 5;

            if (gallery == true) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    mImage.setImageBitmap(bitmap);
                    gallery = false;
                } catch (Exception e ){
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
                mImage.setImageBitmap(bitmap);
            }



        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //Create a file Uri for saving an image or video
    public static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // Create a File for saving an image or video
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    //Receive camera result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                previewCapturedImage();

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }

        if (requestCode == RESULT_GALLERY )
        if (data != null) {
            fileUri = data.getData();
            gallery = true;
            previewCapturedImage();
        }
    }

    //Google play service
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            Toast.makeText(getActivity(),"Got Location",Toast.LENGTH_SHORT).show();
//            Log.d(TAG,"Location is available");
//            //mLocation.setText(String.valueOf(mLastLocation.getLatitude()));
//            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        } else {
//            Toast.makeText(getActivity(),"No Location is detected", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}

