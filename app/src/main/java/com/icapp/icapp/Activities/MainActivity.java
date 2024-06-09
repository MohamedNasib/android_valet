package com.icapp.icapp.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Strings;
import com.icapp.icapp.Adapters.CarFleetAdapter;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends SyncActivity {

    RecyclerView rvCarFleet;
    CarFleetAdapter carFleetAdapter;
    LinearLayout greatJobView;
    TextView valet_name;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_main);
        rvCarFleet = findViewById(R.id.rvCarFleet);
        greatJobView = findViewById(R.id.greatJobView);
        valet_name = findViewById(R.id.valet_name);
        profileImage = findViewById(R.id.profileImage);
        valet_name.setText(sharedPrefs.get(R.string.FIRST_NAME_KEY, ""));

        String userImage = sharedPrefs.get(R.string.USER_IMAGE_KEY, "");
        if (!Strings.isNullOrEmpty(userImage)) {
            Picasso.get().load(userImage).into(profileImage);
        }
        syncInBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshCarFleet();
    }

    public void refreshCarFleet() {
        ArrayList<CarReport> lst = RealM.getInstance().getArrivalList().stream().filter(report -> report.getSubmit_later() == 2).collect(Collectors.toCollection(ArrayList::new));
        carFleetAdapter = new CarFleetAdapter(this, lst);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rvCarFleet.setLayoutManager(lm);
        rvCarFleet.setAdapter(carFleetAdapter);
        rvCarFleet.setVisibility(lst.isEmpty() ? View.GONE : View.VISIBLE);
        greatJobView.setVisibility(!lst.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void settingsPressed(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void arrivalPressed(View view) {
        if (!sharedPrefs.get(R.string.REPORT_CAR_PLATE_KEY, "").isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.previous_car_report);
            alert.setMessage(R.string.do_you_need);
            alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, ArrivalActivity.class));
            });

            alert.setNegativeButton(R.string.no, (dialog, which) -> {
                dialog.dismiss();
                removeArrival();
                startActivity(new Intent(MainActivity.this, CarPlateScannerActivity.class));
            });

            alert.show();
        } else {
            startActivity(new Intent(this, CarPlateScannerActivity.class));
        }
    }

    public void departurePressed(View view) {
        startActivity(new Intent(this, FindVehicleActivity.class));
    }
}