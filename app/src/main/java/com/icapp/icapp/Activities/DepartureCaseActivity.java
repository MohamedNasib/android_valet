package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.CAR_REPORT_KEY;
import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_1;
import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_2;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Helpers.TimeUtils;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DepartureCaseActivity extends ParentActivity {

    CarReport carReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_departure_case);

        ImageView imgReport = findViewById(R.id.imgReport);
        RelativeLayout report_status = findViewById(R.id.report_status);
        ImageView imgStatus = findViewById(R.id.imgStatus);
        TextView txtStatus = findViewById(R.id.txtStatus);
        TextView txt_plate_number = findViewById(R.id.txt_plate_number);
        TextView txt_address = findViewById(R.id.txt_address);
        TextView txt_date = findViewById(R.id.txt_date);
        TextView txt_user = findViewById(R.id.txt_user);
        ImageView profileImage = findViewById(R.id.profileImage);

        carReport = getIntent().getParcelableExtra(CAR_REPORT_KEY);
        if (carReport != null) {
            Picasso.get().load(carReport.getFront_image()).into(imgReport);
            txt_plate_number.setText(carReport.getPlateNumber());
            txt_address.setText(carReport.getAddress());
            txt_date.setText(TimeUtils.convertDateTime(carReport.getCreatedAt()));
            txt_user.setText(carReport.getUser_name());
            if (!Strings.isNullOrEmpty(carReport.getUser_image())) {
                Picasso.get().load(carReport.getUser_image()).into(profileImage);
            }

            if (carReport.getDamages() == null || carReport.getDamages().isEmpty() || carReport.getDamages().equals("[]")) {
                report_status.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color._F2FDDF)));
                imgStatus.setImageResource(R.drawable.vector1);
                txtStatus.setTextColor(getColor(R.color._25741F));
                txtStatus.setText(R.string.ok);
            } else {
                report_status.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color._FFE9E8)));
                imgStatus.setImageResource(R.drawable.danger);
                txtStatus.setTextColor(getColor(R.color._E04F33));
                txtStatus.setText(R.string.damages);
            }
        }
    }

    public void departurePressed(View view) {
        updateStatus(carReport.getId(), ORDER_STATUS_2, R.string.removed);
    }

    public void temporaryLeavePressed(View view) {
        updateStatus(carReport.getId(), ORDER_STATUS_1, R.string.temporary);
    }

    public void showReport(View view) {
        if (!Strings.isNullOrEmpty(carReport.getPdf())) {
            if (carReport.getDamages() == null || carReport.getDamages().isEmpty() || carReport.getDamages().equals("[]")) {
                // no damage
            } else {
                startActivity(new Intent(this, PDFViewerActivity.class)
                        .putExtra(CAR_REPORT_KEY, carReport));
            }
        }
    }

    public void updateStatus(String id, int status, int msg_id) {

        RealM.getInstance().beginTransaction();
        CarReport carReport = RealM.getInstance().findArrivalByID(id);
        carReport.setStatus(status);
        if (!carReport.getId().startsWith("_")) {
            carReport.setRequire_status_sync(1);
        }
        RealM.getInstance().commitTransaction();

        // show dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.done_removed_temporary);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((TextView) dialog.findViewById(R.id.tv_message)).setText(msg_id);
        dialog.findViewById(R.id.close).setOnClickListener(view1 -> {
            dialog.dismiss();
            removeArrival();
//            removeUser();
            startActivity(new Intent(DepartureCaseActivity.this, MainActivity.class));
            finishAffinity();
        });

        dialog.show();
    }
}