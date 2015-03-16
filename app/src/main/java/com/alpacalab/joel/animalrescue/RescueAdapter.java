package com.alpacalab.joel.animalrescue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

/**
 * Created by Joel on 1/21/15.
 */
public class RescueAdapter extends ArrayAdapter<Rescue>{
    private Context mContext;
    private List<Rescue> mRescue;

    public RescueAdapter(Context context, List<Rescue> objects) {
        super(context, R.layout.rescue_row_item, objects);
        this.mContext = context;
        this.mRescue = objects;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.rescue_row_item, null);
        }
        Rescue rescue = mRescue.get(position);

        TextView descriptionView = (TextView) convertView.findViewById(R.id.desc);
        TextView animalTypeView = (TextView) convertView.findViewById(R.id.animal_type);
        TextView statusView = (TextView) convertView.findViewById(R.id.status);
        final ImageView feedImageView = (ImageView) convertView.findViewById(R.id.feedImage);

        descriptionView.setText(rescue.getDescription());
        animalTypeView.setText(rescue.getAnimal());
        if(rescue.getRescueStatus()==false){
            statusView.setText("Awaiting Rescue");
        } else {
            statusView.setText("Rescued! :3");
        }
        ParseFile fileObject = rescue.getImage();
        if (fileObject !=  null) {
            fileObject.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Log.d("ImageFeed", "We've got data in data.");
                        // Decode the Byte[] into
                        // Bitmap
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        feedImageView.setImageBitmap(bmp);
                    } else {
                        Log.d("ImageFeed", "There was a problem downloading the data.");
                    }
                }

            });
        }




        return convertView;
    }
}
