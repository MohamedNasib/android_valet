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

import java.util.List;
import java.util.Objects;

public class CarPartAdapter extends RecyclerView.Adapter<CarPartViewHolder> {
    List<CarPart> parts;
    ParentActivity activity;

    public CarPartAdapter(ParentActivity activity, List<CarPart> parts) {
        this.parts = parts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CarPartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_issues_photo_layout , null , false);
        return new CarPartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarPartViewHolder holder, int position) {
        holder.onBind(activity, Objects.requireNonNull(parts.get(position)));
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }
}

class CarPartViewHolder extends RecyclerView.ViewHolder {

    ImageView issuePhoto;
    TextView tvPartName, tvTime;
    ParentActivity activity;

    public CarPartViewHolder(@NonNull View itemView) {
        super(itemView);
        issuePhoto = itemView.findViewById(R.id.issuePhoto);
        tvPartName = itemView.findViewById(R.id.tvPartName);
        tvTime = itemView.findViewById(R.id.tvTime);
    }

    public void onBind(ParentActivity activity, CarPart carPart) {
        this.activity = activity;

        if (carPart.getPath() != null) {
            if (carPart.getPath().contains("https")) {
                Picasso.get().load(carPart.getPath()).resize(74, 50).into(issuePhoto);
            } else {
                issuePhoto.setImageBitmap(decodeSampledBitmapFromFile(carPart.getPath(), 74, 50));
            }
        }
        tvPartName.setText(carPart.getName());
        tvPartName.setVisibility((carPart.getName() == null || Objects.equals(carPart.getName(), "")) ? View.GONE : View.VISIBLE);
        tvTime.setText(TimeUtils.convertTime(carPart.getDate()));
    }
}