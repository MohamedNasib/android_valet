package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.CAR_REPORT_KEY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icapp.icapp.Adapters.CarDamageAdapter;
import com.icapp.icapp.Adapters.CarSidesAdapter;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Helpers.TimeUtils;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.Models.CarUpdate;
import com.icapp.icapp.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UpdateCaseReportActivity extends ParentActivity {

    RecyclerView rvCarPhotos;
    RecyclerView rvCarIssuesPhotos, rvCarIssuesPhotos_update;
    RecyclerView rvCarAdditionalPhotos;
    LinearLayout viewAdditional;
    CarSidesAdapter carSidesAdapter;
    CarDamageAdapter carDamageAdapter, carDamageAdapter_update;
    LinearLayout viewCarIssues, viewCarIssues_update;
    RelativeLayout containerView;
    TextView tvDate, tvLocation, tvCarPlateNumber, tvTicketNumber, tvValetName;
    CarReport carReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_update_case_report);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        carReport = getIntent().getParcelableExtra(CAR_REPORT_KEY);

        containerView = findViewById(R.id.containerView);
        rvCarPhotos = findViewById(R.id.rvCarPhotos);
        rvCarIssuesPhotos = findViewById(R.id.rvCarIssuesPhotos);
        rvCarIssuesPhotos_update = findViewById(R.id.rvCarIssuesPhotos_update);
        viewCarIssues = findViewById(R.id.viewCarIssues);
        viewCarIssues_update = findViewById(R.id.viewCarIssues_update);
        tvDate = findViewById(R.id.tvDate);
        tvLocation = findViewById(R.id.tvLocation);
        tvCarPlateNumber = findViewById(R.id.tvCarPlateNumber);
        tvTicketNumber = findViewById(R.id.tvTicketNumber);
        tvValetName = findViewById(R.id.tvValetName);
        rvCarAdditionalPhotos = findViewById(R.id.rvCarAdditionalPhotos);
        viewAdditional = findViewById(R.id.viewAdditional);

        configure();

        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            saveReportAndBack();
        });

        findViewById(R.id.btnReportDamages).setOnClickListener(view -> {
            startActivity(new Intent(this, UpdateDamageActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<CarPart> damagesList_update = sharedPrefs.get(R.string.UPDATE_DAMAGES_KEY);

        viewCarIssues_update.setVisibility(damagesList_update.isEmpty() ? View.GONE : View.VISIBLE);
        carDamageAdapter_update = new CarDamageAdapter(this, damagesList_update, true);
        rvCarIssuesPhotos_update.setLayoutManager(new GridLayoutManager(this , 2));
        rvCarIssuesPhotos_update.setAdapter(carDamageAdapter_update);
    }

    private void saveReportAndBack() {
        CarUpdate carUpdate = new CarUpdate(carReport.getTicketNumber(),
                new Gson().toJson(sharedPrefs.get(R.string.UPDATE_DAMAGES_KEY)),
                carReport.getUser_id(), carReport.getId());
        RealM.getInstance().insertUpdate(carUpdate);
        removeArrival();
//        removeUser();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    public void configure() {
        tvDate.setText(TimeUtils.convertDateTime(carReport.getCreatedAt()));
        tvLocation.setText(carReport.getAddress());
        tvCarPlateNumber.setText(carReport.getPlateNumber());
        tvTicketNumber.setText(carReport.getTicketNumber());
        tvValetName.setText(carReport.getUser_name());

        ArrayList<CarPart> carSides = new ArrayList<>();
        carSides.add(new CarPart(carReport.getFront_image(), "front", carReport.getFront_date()));
        carSides.add(new CarPart(carReport.getLeft_image(), "left", carReport.getLeft_date()));
        carSides.add(new CarPart(carReport.getBack_image(), "back", carReport.getBack_date()));
        carSides.add(new CarPart(carReport.getRight_image(), "right", carReport.getRight_date()));
        if (!Strings.isNullOrEmpty(carReport.getAnother_front_image())) {
            carSides.add(new CarPart(carReport.getAnother_front_image(), "front +", carReport.getAnother_front_date()));
        }
        if (!Strings.isNullOrEmpty(carReport.getAnother_left_image())) {
            carSides.add(new CarPart(carReport.getAnother_left_image(), "left +", carReport.getAnother_left_date()));
        }
        if (!Strings.isNullOrEmpty(carReport.getAnother_back_image())) {
            carSides.add(new CarPart(carReport.getAnother_back_image(), "back +", carReport.getAnother_back_date()));
        }
        if (!Strings.isNullOrEmpty(carReport.getAnother_right_image())) {
            carSides.add(new CarPart(carReport.getAnother_right_image(), "right +", carReport.getAnother_right_date()));
        }

        carSidesAdapter = new CarSidesAdapter(this, carSides);
        rvCarPhotos.setLayoutManager(new GridLayoutManager(this , 2));
        rvCarPhotos.setAdapter(carSidesAdapter);

        Type type = new TypeToken<ArrayList<CarPart>>() {
        }.getType();
        ArrayList<CarPart> damagesList = new Gson().fromJson(carReport.getDamages(), type);
        ArrayList<CarPart> additionalList = new Gson().fromJson(carReport.getAdditional(), type);

        viewCarIssues.setVisibility(damagesList.isEmpty() ? View.GONE : View.VISIBLE);
        carDamageAdapter = new CarDamageAdapter(this, damagesList, true);
        rvCarIssuesPhotos.setLayoutManager(new GridLayoutManager(this , 2));
        rvCarIssuesPhotos.setAdapter(carDamageAdapter);

        viewAdditional.setVisibility(additionalList.isEmpty() ? View.GONE : View.VISIBLE);
        carSidesAdapter = new CarSidesAdapter(this, additionalList);
        rvCarAdditionalPhotos.setLayoutManager(new GridLayoutManager(this , 2));
        rvCarAdditionalPhotos.setAdapter(carSidesAdapter);
    }
}