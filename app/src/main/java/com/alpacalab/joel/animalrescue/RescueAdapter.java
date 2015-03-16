package com.alpacalab.joel.animalrescue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageView feedImageView = (ImageView) convertView.findViewById(R.id.feedImage);

        descriptionView.setText(rescue.getDescription());
        animalTypeView.setText(rescue.getAnimal());
        if(rescue.getRescueStatus()==false){
            statusView.setText("Awaiting Rescue");
        } else {
            statusView.setText("Rescued! :3");
        }
        feedImageView.setImageBitmap(rescue.getImage());


        return convertView;
    }
}
