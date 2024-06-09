package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.CAR_REPORT_KEY;
import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_0;
import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_1;
import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_2;
import static com.icapp.icapp.Helpers.EuropeanCarPlateValidator.isValidEuropeanCarPlate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Helpers.Scanner.Scanner;
import com.icapp.icapp.Helpers.Scanner.ScannerListener;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;

public class CarPlateScannerActivity extends ParentActivity {
    EditText tvCarPlate;
    RelativeLayout viewCompleteScan;
    SurfaceView previewView;
    Scanner scanner;

    private boolean haveValidPlate = false;
    private final ActivityResultLauncher<String> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), o -> {
                if (o) {
                    // Initialize and bind the camera
                    initializeCamera();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_plate_scanner);

        tvCarPlate = findViewById(R.id.tvCarPlate);
        viewCompleteScan = findViewById(R.id.viewCompleteScan);
        previewView = findViewById(R.id.previewView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            // Initialize and bind the camera
            initializeCamera();
        }

//        findViewById(R.id.btnFlash).setOnClickListener(view -> {
//            scanner.toggleFlashLight();
////            ((ImageButton) findViewById(R.id.btnFlash)).setImageResource(scanner.isFlashOn ? R.drawable.baseline_flash_on_24 : R.drawable.group33986);
//        });
    }

    private void initializeCamera() {
        scanner = new Scanner(this, previewView, new ScannerListener() {
            @Override
            public void onDetected(String detections) {
                if (!haveValidPlate) {
                    StringBuilder result = new StringBuilder();
                    for (String row : detections.split("\n")) {
                        if (isValidEuropeanCarPlate(row.replaceAll("\\s", ""))) {
                            result.append(row);
                        }
                    }
                    if (isValidEuropeanCarPlate(String.valueOf(result))) {
                        haveValidPlate = true;
                        displayText(detections);
                    }
                }
            }

            @Override
            public void onStateChanged(String state, int i) {
                Log.d("state", state);
            }
        });
    }

    public void continuePressed(View view) {
        String plate = tvCarPlate.getText().toString();
        CarReport carReport = RealM.getInstance().checkCarExist(plate);
        if (carReport == null || carReport.getStatus() == ORDER_STATUS_2) { // not exist of final exist
            sharedPrefs.set(R.string.REPORT_CAR_PLATE_KEY, plate);
            startActivity(new Intent(this, ArrivalActivity.class));
        } else if (carReport.getStatus() == ORDER_STATUS_0 || carReport.getStatus() == ORDER_STATUS_1) { // found or temp exit
            startActivity(new Intent(this, ArrivalCaseActivity.class).putExtra(CAR_REPORT_KEY, carReport));
        }
    }

    public void replayPressed(View view) {
        tvCarPlate.setText("");
        haveValidPlate = false;
        viewCompleteScan.setVisibility(View.GONE);
        tvCarPlate.setFocusable(false);
    }

    public void displayText(String text) {
        tvCarPlate.setText(text.replaceAll(" ", "").replaceAll("-", "").replaceAll("\n", ""));
        viewCompleteScan.setVisibility(View.VISIBLE);
        tvCarPlate.setFocusable(true);
    }
}