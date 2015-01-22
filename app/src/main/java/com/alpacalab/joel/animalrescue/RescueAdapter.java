package com.alpacalab.joel.animalrescue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        TextView descriptionView = (TextView) convertView.findViewById(R.id.rescue_desc);
        descriptionView.setText(rescue.getDescription());

        return convertView;
    }
}
