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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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
    private ImageView mImage;
    //Google Play Service
    private GoogleApiClient mGoogleApiClient;
    //Location
    private Location mLastLocation;
    private TextView mLocation;
    //Debug
    protected static final String TAG = "RescueRequest";

    public RescueRequestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rescue_request, container, false);

        //View
        mImage = (ImageView) rootView.findViewById(R.id.picture_preview);
        mLocation = (TextView) rootView.findViewById(R.id.location);
        //Button
        Button mCamera = (Button) rootView.findViewById(R.id.camera);
        Button mGetLocation = (Button) rootView.findViewById(R.id.get_location);

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromGallery();
            }
        });

        buildGoogleApiClient();

        mGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        return rootView;
    }

    public void getFromGallery() {
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
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);
                    mImage.setImageBitmap(bitmap);
                    gallery = false;
                } catch (Exception e ){
                    e.printStackTrace();
                }
            } else {
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
                mImage.setImageBitmap(bitmap);
            }



        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    //Camera stuff
    // Create a file Uri for saving an image or video
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
        if (null != data) {
            fileUri = data.getData();
            gallery = true;
            previewCapturedImage();
            //Do whatever that you desire here. or leave this blank

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

