package com.icapp.icapp.Adapters;

import static com.icapp.icapp.Activities.ParentActivity.decodeSampledBitmapFromFile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icapp.icapp.Activities.FullImageActivity;
import com.icapp.icapp.Activities.ParentActivity;
import com.icapp.icapp.Helpers.TimeUtils;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class CarDamageAdapter extends RecyclerView.Adapter<CarDamageViewHolder> {
    List<CarPart> damages;
    ParentActivity activity;
    Boolean allowZoom;

    public CarDamageAdapter(ParentActivity activity, List<CarPart> damages, Boolean allowZoom) {
        this.damages = damages;
        this.activity = activity;
        this.allowZoom = allowZoom;
    }

    @NonNull
    @Override
    public CarDamageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_issues_photo_layout , null , false);
        return new CarDamageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarDamageViewHolder holder, int position) {
        holder.onBind(activity, Objects.requireNonNull(damages.get(position)), allowZoom);
    }

    @Override
    public int getItemCount() {
        return damages.size();
    }
}

class CarDamageViewHolder extends RecyclerView.ViewHolder {

    ImageView issuePhoto;
    TextView tvPartName;
    TextView tvTime;
    ParentActivity activity;

    public CarDamageViewHolder(@NonNull View itemView) {
        super(itemView);
        issuePhoto = itemView.findViewById(R.id.issuePhoto);
        tvPartName = itemView.findViewById(R.id.tvPartName);
        tvTime = itemView.findViewById(R.id.tvTime);
    }

    public void onBind(ParentActivity activity, CarPart carPart, Boolean allowZoom) {
        this.activity = activity;

        if (carPart.getPath() != null) {
            if (carPart.getPath().contains("https")) {
                Picasso.get().load(carPart.getPath()).resize(74, 50).into(issuePhoto);
            } else {
                issuePhoto.setImageBitmap(decodeSampledBitmapFromFile(carPart.getPath(), 74, 50));
            }
        }
        tvPartName.setText(carPart.getName());
        tvTime.setText(TimeUtils.convertTime(carPart.getDate()));

        if (allowZoom) {
            issuePhoto.setOnClickListener(view -> {
                activity.startActivity(new Intent(activity, FullImageActivity.class).putExtra("Damage", carPart));
            });
        }
    }
}