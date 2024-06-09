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
import com.icapp.icapp.Models.CarUpdate;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UpdateLogAdapter extends RecyclerView.Adapter<UpdateLogViewHolder> {

    ArrayList<CarUpdate> carUpdates ;
    Context context;

    public UpdateLogAdapter(Context context, ArrayList<CarUpdate> carUpdates) {
        this.carUpdates = carUpdates;
        this.context = context;
    }

    public void setData(ArrayList<CarUpdate> carReportsFilter) {
        this.carUpdates = carReportsFilter;
    }

    @NonNull
    @Override
    public UpdateLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_log_layout , null , false);
        return new UpdateLogViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateLogViewHolder holder, int position) {
        holder.onBind(context, carUpdates.get(position));
    }

    @Override
    public int getItemCount() {
        return carUpdates.size();
    }
}
class UpdateLogViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout containerView;
    ImageView carImage, imgStatus;
    TextView tvID, tvTicketNumber, tvPlateNumber, tvDate, txtStatus;
    RelativeLayout report_status;

    public UpdateLogViewHolder(@NonNull View itemView) {
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

    public void onBind(Context context, CarUpdate carUpdate) {
//        if (carUpdate.getFront_image() != null) {
//            if (carReport.getFront_image().contains("https")) {
//                Picasso.get().load(carReport.getFront_image()).into(carImage);
//            } else {
//                carImage.setImageBitmap(decodeSampledBitmapFromFile(carReport.getFront_image(), 74, 50));
//            }
//        }
        tvID.setText(carUpdate.getId());
        tvTicketNumber.setText(carUpdate.getTicketNumber());
        tvPlateNumber.setText(carUpdate.getTicket_id());
        tvDate.setText(TimeUtils.convertDateTime(carUpdate.getCreatedAt()));
//        containerView.setOnClickListener(view -> {
//            context.startActivity(new Intent(context, DepartureCaseActivity.class).putExtra(CAR_REPORT_KEY, carReport));
//        });

        if (carUpdate.getDamages() == null || carUpdate.getDamages().isEmpty() || carUpdate.getDamages().equals("[]")) {
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