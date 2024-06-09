package com.icapp.icapp.Adapters;

import static com.icapp.icapp.Activities.ParentActivity.decodeSampledBitmapFromFile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icapp.icapp.Activities.ParentActivity;
import com.icapp.icapp.Helpers.TimeUtils;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class CarSidesAdapter extends RecyclerView.Adapter<CarPhotosViewHolder> {

    ArrayList<CarPart> arrayList;
    ParentActivity activity;

    public CarSidesAdapter(ParentActivity activity, ArrayList<CarPart> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CarPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_photo_layout , null , false);
        return new CarPhotosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarPhotosViewHolder holder, int position) {
        holder.onBind(activity, Objects.requireNonNull(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

class CarPhotosViewHolder extends RecyclerView.ViewHolder {
    ImageView carPhoto;
    TextView tvPartName, tvTime;
    ParentActivity activity;

    public CarPhotosViewHolder(@NonNull View itemView) {
        super(itemView);
        carPhoto = itemView.findViewById(R.id.carPhoto);
        tvPartName = itemView.findViewById(R.id.tvPartName);
        tvTime = itemView.findViewById(R.id.tvTime);
    }

    public void onBind(ParentActivity activity, CarPart carPart) {
        this.activity = activity;

        if (carPart.getPath() != null) {
            if (carPart.getPath().contains("https")) {
                Picasso.get().load(carPart.getPath()).resize(140, 95).into(carPhoto);
            } else {
                carPhoto.setImageBitmap(decodeSampledBitmapFromFile(carPart.getPath(), 140, 95));
            }
        }
        tvPartName.setText(carPart.getName());
        tvTime.setText(TimeUtils.convertTime(carPart.getDate()));
    }
}