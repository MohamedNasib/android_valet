package com.icapp.icapp.Adapters;

import static com.icapp.icapp.Activities.ParentActivity.decodeSampledBitmapFromFile;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.icapp.icapp.Helpers.TimeUtils;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

    ArrayList<CarReport> carReports ;
    Context context;

    public LogAdapter(Context context, ArrayList<CarReport> carReports) {
        this.carReports = carReports;
        this.context = context;
    }

    public void setData(ArrayList<CarReport> carReportsFilter) {
        this.carReports = carReportsFilter;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_log_layout , null , false);
        return new LogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        holder.onBind(context, carReports.get(position));
    }

    @Override
    public int getItemCount() {
        return carReports.size();
    }
}
class LogViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout containerView;
    ImageView carImage, imgStatus;
    TextView tvID, tvTicketNumber, tvPlateNumber, tvDate, txtStatus;
    RelativeLayout report_status;

    public LogViewHolder(@NonNull View itemView) {
        super(itemView);
        containerView = itemView.findViewById(R.id.containerView);
        carImage = itemView.findViewById(R.id.carImage);
        tvID = itemView.findViewById(R.id.tvID);
        tvTicketNumber = itemView.findViewById(R.id.tvTicketNumber);
        tvPlateNumber = itemView.findViewById(R.id.tvPlateNumber);
        tvDate = itemView.findViewById(R.id.tvDate);
        report_status = itemView.findViewById(R.id.report_status);
        imgStatus = itemView.findViewById(R.id.imgStatus);
        txtStatus = itemView.findViewById(R.id.txtStatus);
    }

    public void onBind(Context context, CarReport carReport) {
        if (carReport.getFront_image() != null) {
            if (carReport.getFront_image().contains("https")) {
                Picasso.get().load(carReport.getFront_image()).resize(74, 50).into(carImage);
            } else {
                carImage.setImageBitmap(decodeSampledBitmapFromFile(carReport.getFront_image(), 74, 50));
            }
        }
        tvID.setText(carReport.getId());
        tvTicketNumber.setText(carReport.getTicketNumber());
        tvPlateNumber.setText(carReport.getPlateNumber());
        tvDate.setText(TimeUtils.convertDateTime(carReport.getCreatedAt()));
//        containerView.setOnClickListener(view -> {
//            context.startActivity(new Intent(context, DepartureCaseActivity.class).putExtra(CAR_REPORT_KEY, carReport));
//        });

        if (carReport.getDamages() == null || carReport.getDamages().isEmpty() || carReport.getDamages().equals("[]")) {
            report_status.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color._F2FDDF)));
            imgStatus.setImageResource(R.drawable.vector1);
            txtStatus.setTextColor(context.getColor(R.color._25741F));
            txtStatus.setText(R.string.ok);
        } else {
            report_status.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color._FFE9E8)));
            imgStatus.setImageResource(R.drawable.danger);
            txtStatus.setTextColor(context.getColor(R.color._E04F33));
            txtStatus.setText(R.string.damages);
        }
    }
}