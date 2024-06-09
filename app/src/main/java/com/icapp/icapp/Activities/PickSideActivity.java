package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.ADDITIONAL_PHOTO_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_BACK_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_FRONT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_LEFT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_RIGHT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.BACK_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.DAMAGE_KEY;
import static com.icapp.icapp.Helpers.Constants.DAMAGE_PHOTO_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.FRONT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.LEFT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.RIGHT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.SIDE_KEY;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class PickSideActivity extends ParentActivity {

    ImageButton btnCapture;
    TextView tvTitle;
    ImageView imgSideType;
    PreviewView previewView;
    RelativeLayout centerCropView;
    Camera camera;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    ActivityResultLauncher<String> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), o -> {
                if (o) {
                    startCamera(cameraFacing);
                }
            });
    int sideType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pick_side);

        previewView = findViewById(R.id.previewView);
        btnCapture = findViewById(R.id.btnCapture);

        tvTitle = findViewById(R.id.tvTitle);
        imgSideType = findViewById(R.id.imgSideType);
        centerCropView = findViewById(R.id.centerCropView);

        findViewById(R.id.btnFlash).setOnClickListener(view -> {
            if (camera.getCameraInfo().hasFlashUnit()){
                if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                    camera.getCameraControl().enableTorch(true);
                    ((ImageButton) findViewById(R.id.btnFlash)).setImageResource(R.drawable.baseline_flash_on_24);
                }else {
                    camera.getCameraControl().enableTorch(false);
                    ((ImageButton) findViewById(R.id.btnFlash)).setImageResource(R.drawable.group33986);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(PickSideActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }

        sideType = getIntent().getIntExtra(SIDE_KEY, 0);
        setupUI();
    }

    public void setupUI() {
        imgSideType.setVisibility(View.GONE);
        switch (sideType) {
            case FRONT_SIDE_REQUEST_CODE:
            case ANOTHER_FRONT_SIDE_REQUEST_CODE:
                tvTitle.setText(getString(R.string.scan_front_side));
                imgSideType.setImageResource(R.drawable.layer_front);
                centerCropView.setBackgroundResource(R.drawable.car_front_border);
                break;
            case LEFT_SIDE_REQUEST_CODE:
            case ANOTHER_LEFT_SIDE_REQUEST_CODE:
                tvTitle.setText(getString(R.string.scan_left_side));
                imgSideType.setImageResource(R.drawable.left_layer);
                centerCropView.setBackgroundResource(R.drawable.car_left_border);
                break;
            case BACK_SIDE_REQUEST_CODE:
            case ANOTHER_BACK_SIDE_REQUEST_CODE:
                tvTitle.setText(getString(R.string.scan_back_side));
                imgSideType.setImageResource(R.drawable.back_layer);
                centerCropView.setBackgroundResource(R.drawable.car_back_border);
                break;
            case RIGHT_SIDE_REQUEST_CODE:
            case ANOTHER_RIGHT_SIDE_REQUEST_CODE:
                tvTitle.setText(getString(R.string.scan_right_side));
                imgSideType.setImageResource(R.drawable.right_layer);
                centerCropView.setBackgroundResource(R.drawable.car_right_border);
                break;
            case ADDITIONAL_PHOTO_REQUEST_CODE:
            case DAMAGE_PHOTO_REQUEST_CODE:
                tvTitle.setText(R.string.scan_car_part);
                centerCropView.setBackgroundResource(R.drawable.car_part_border);
                break;
        }
    }

    public void startCamera(int cameraFacing) {
        int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);
        listenableFuture.addListener(() -> {

            try {
                ProcessCameraProvider cameraProvider = listenableFuture.get();
                Preview preview = new Preview.Builder()
                        .setTargetAspectRatio(aspectRatio).build();
                ImageCapture imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                        .build();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();
                cameraProvider.unbindAll();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                btnCapture.setOnClickListener(view -> {
//                    if (ContextCompat.checkSelfPermission(PickSideActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                    } else {
//                        takePicture(imageCapture);
//                    }
                    takePicture(imageCapture);
                });

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                Log.d("TAG", Objects.requireNonNull(e.getMessage()));
            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void takePicture(ImageCapture imageCapture) {
        Long time = System.currentTimeMillis();
        final File file = new File(getExternalFilesDir(null), time + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    switch (sideType) {
                        case FRONT_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.FRONT_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.FRONT_IMAGE_DATE_KEY, String.valueOf(time));
                            showDamageDialog();
                            break;
                        case LEFT_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.LEFT_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.LEFT_IMAGE_DATE_KEY, String.valueOf(time));
                            showDamageDialog();
                            break;
                        case BACK_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.BACK_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.BACK_IMAGE_DATE_KEY, String.valueOf(time));
                            showDamageDialog();
                            break;
                        case RIGHT_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.RIGHT_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.RIGHT_IMAGE_DATE_KEY, String.valueOf(time));
                            showDamageDialog();
                            break;
                        case ANOTHER_FRONT_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.ANOTHER_FRONT_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.ANOTHER_FRONT_IMAGE_DATE_KEY, String.valueOf(time));
                            finish();
                            break;
                        case ANOTHER_LEFT_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.ANOTHER_LEFT_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.ANOTHER_LEFT_IMAGE_DATE_KEY, String.valueOf(time));
                            finish();
                            break;
                        case ANOTHER_BACK_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.ANOTHER_BACK_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.ANOTHER_BACK_IMAGE_DATE_KEY, String.valueOf(time));
                            finish();
                            break;
                        case ANOTHER_RIGHT_SIDE_REQUEST_CODE:
                            sharedPrefs.set(R.string.ANOTHER_RIGHT_IMAGE_KEY, file.getAbsolutePath());
                            sharedPrefs.set(R.string.ANOTHER_RIGHT_IMAGE_DATE_KEY, String.valueOf(time));
                            finish();
                            break;
                        case ADDITIONAL_PHOTO_REQUEST_CODE:
                            ArrayList<CarPart> lst = sharedPrefs.get(R.string.ADDITIONAL_KEY);
                            lst.add(new CarPart(file.getAbsolutePath(), "", time));
                            sharedPrefs.set(R.string.ADDITIONAL_KEY, lst);
                            finish();
                            break;
                        case DAMAGE_PHOTO_REQUEST_CODE:
                            Intent backIntent = new Intent();
                            backIntent.putExtra(DAMAGE_KEY, new CarPart(file.getAbsolutePath(), "", time));
                            setResult(sideType, backIntent);
                            finish();
                            break;
                    }
                });
                startCamera(cameraFacing);
            }

            @Override
            public void onError(@NonNull ImageCaptureException e) {
                Log.d("TAG", Objects.requireNonNull(e.getMessage()));
                startCamera(cameraFacing);
            }
        });
    }

    private void showDamageDialog() {
        final Dialog dialog = new Dialog(PickSideActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.damage_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.findViewById(R.id.okBtn).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.findViewById(R.id.damageBtn).setOnClickListener(v -> {
            startActivity(new Intent(PickSideActivity.this, ReportDamageActivity.class)
                    .putExtra(SIDE_KEY, sideType));
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}