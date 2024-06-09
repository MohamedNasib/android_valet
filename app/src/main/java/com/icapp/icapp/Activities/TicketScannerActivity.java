package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.CAR_REPORT_KEY;
import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.AspectRatio;
import androidx.core.content.ContextCompat;

import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Helpers.Scanner.Scanner;
import com.icapp.icapp.Helpers.Scanner.ScannerListener;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicketScannerActivity extends ParentActivity {

    Scanner scanner;
    String pattern = ".*\\d{4,}.*";

    private final ActivityResultLauncher<String> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), o -> {
                if (o) {
                    // Initialize and bind the camera
                    initializeCamera();
                }
            });
    RelativeLayout viewCompleteScan;
    SurfaceView previewView;
    Boolean ticketScanned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_scanner);

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

//            ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);
//            try {
//                camera = listenableFuture.get().bindToLifecycle(this, new CameraSelector.Builder().build(), new ImageCapture.Builder().build());
//            } catch (ExecutionException | InterruptedException e) {
//                Log.d("TAG", Objects.requireNonNull(e.getMessage()));
//            }

            // Initialize and bind the camera
            initializeCamera();
        }

//        findViewById(R.id.btnFlash).setOnClickListener(view -> {
//
//            if (camera.getCameraInfo().hasFlashUnit()){
//                if (camera.getCameraInfo().getTorchState().getValue() == 0) {
//                    camera.getCameraControl().enableTorch(true);
//                    ((ImageButton) findViewById(R.id.btnFlash)).setImageResource(R.drawable.baseline_flash_on_24);
//                }else {
//                    camera.getCameraControl().enableTorch(false);
//                    ((ImageButton) findViewById(R.id.btnFlash)).setImageResource(R.drawable.group33986);
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticketScanned = false;
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    private void initializeCamera() {
        scanner = new Scanner(this, previewView, new ScannerListener() {
            @Override
            public void onDetected(String detections) {
                String numbers = extractNumbers(detections);
                if (validateText(numbers)) {
                    displayText(numbers);
                }
            }

            @Override
            public void onStateChanged(String state, int i) {
                Log.d("state", state);
            }
        });
    }

    private Boolean validateText(String scannedText) {

        // Create a matcher object
        Matcher matcher = Pattern.compile(pattern).matcher(scannedText);

        // Return true if the car plate matches the pattern
        return matcher.matches();
    }

    public void continuePressed(View view) {
        startActivity(new Intent(this, ArrivalActivity.class));
        finishAffinity();
    }

    public void replayPressed(View view) {
        recreate();
    }

    public void displayText(String stringText) {
        String text = extractNumbers(stringText);
        if (getIntent().getBooleanExtra("isDeparture", false)) {
            ArrayList<CarReport> lst = RealM.getInstance().searchPlateOrTicket(text);
            if (!lst.isEmpty() && !ticketScanned) {
                ticketScanned = true;
                startActivity(new Intent(this, DepartureCaseActivity.class).putExtra(CAR_REPORT_KEY, lst.get(lst.size() - 1)));
                finish();
            }
        } else {
            CarReport carReport = RealM.getInstance().checkTicketExist(text);
            if (carReport == null || carReport.getStatus() == ORDER_STATUS_2) {
                sharedPrefs.set(R.string.REPORT_TICKET_NUMBER_KEY, text);
                finish();
            } else {
                showToast(getString(R.string.ticket_already_exist));
            }
        }
    }

    private static String extractNumbers(String input) {
        // Define a regex pattern to match numbers
        Pattern pattern = Pattern.compile("\\d{4,}");
        Matcher matcher = pattern.matcher(input);

        // Use StringBuilder to construct the result string
        StringBuilder result = new StringBuilder();

        // Find and append all matched numbers
        while (matcher.find()) {
            result.append(matcher.group());
        }

        return result.toString();
    }
}