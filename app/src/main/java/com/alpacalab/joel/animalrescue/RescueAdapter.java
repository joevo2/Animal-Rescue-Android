package com.alpacalab.joel.animalrescue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
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
public class RescueAdapter extends RecyclerView.Adapter<RescueAdapter.ViewHolder>{
    private Context mContext;
    private List<Rescue> mRescue;

    public RescueAdapter(Context context, List<Rescue> objects) {
        this.mContext = context;
        this.mRescue = objects;
    }

    @Override
    public RescueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rescue_row_item, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(final RescueAdapter.ViewHolder holder, int position) {
        Rescue rescue = mRescue.get(position);
        holder.descriptionView.setText(rescue.getDescription());
        holder.animalTypeView.setText(rescue.getAnimal());
        if(rescue.getRescueStatus()==false){
            holder.statusView.setText("Awaiting Rescue");
        } else {
            holder.statusView.setText("Rescued! :3");
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
                        holder.feedImageView.setImageBitmap(bmp);
                    } else {
                        Log.d("ImageFeed", "There was a problem downloading the data.");
                    }
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return mRescue == null ? 0 : mRescue.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionView;
        public TextView animalTypeView;
        public TextView statusView;
        public ImageView feedImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            descriptionView = (TextView) itemView.findViewById(R.id.desc);
            animalTypeView = (TextView) itemView.findViewById(R.id.animal_type);
            statusView = (TextView) itemView.findViewById(R.id.status);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage);
        }
    }
}
