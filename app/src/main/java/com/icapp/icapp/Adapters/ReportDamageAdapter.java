package com.icapp.icapp.Adapters;

import static com.icapp.icapp.Activities.ParentActivity.decodeSampledBitmapFromFile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icapp.icapp.Activities.ParentActivity;
import com.icapp.icapp.Interfaces.IRecyclerInterface;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReportDamageAdapter extends RecyclerView.Adapter<ReportDamageViewHolder> {
    ArrayList<CarPart> carDamages;
    IRecyclerInterface iRecyclerInterface;
    ParentActivity activity;

    public ReportDamageAdapter(ParentActivity activity, ArrayList<CarPart> carDamages, IRecyclerInterface iRecyclerInterface) {
        this.carDamages = carDamages;
        this.iRecyclerInterface = iRecyclerInterface;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ReportDamageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_damage_layout ,null , false);
        return new ReportDamageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportDamageViewHolder holder, int position) {
        holder.onBind(activity, position, carDamages.get(position), iRecyclerInterface);
    }

    @Override
    public int getItemCount() {
        return carDamages.size();
    }
}

class ReportDamageViewHolder extends RecyclerView.ViewHolder {
    ImageView imgCar;
    TextView dateCar;
    ImageButton btn_trash;
    ParentActivity activity;
    public ReportDamageViewHolder(@NonNull View itemView) {
        super(itemView);
        imgCar = itemView.findViewById(R.id.carImage);
        dateCar = itemView.findViewById(R.id.txt_dateCar);
        btn_trash = itemView.findViewById(R.id.btn_trash);
    }

    public void onBind(ParentActivity activity, Integer position, CarPart carPart, IRecyclerInterface iRecyclerInterface) {
        this.activity = activity;

        if (carPart.getPath() != null) {
            if (carPart.getPath().contains("https")) {
                Picasso.get().load(carPart.getPath()).resize(74, 50).into(imgCar);
            } else {
                imgCar.setImageBitmap(decodeSampledBitmapFromFile(carPart.getPath(), 74, 50));
            }
        }
        dateCar.setText(carPart.getName());
        btn_trash.setOnClickListener(view -> {
            iRecyclerInterface.onDeletePressed(position);
        });
    }
}