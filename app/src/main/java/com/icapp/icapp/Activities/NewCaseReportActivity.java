package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.CAR_REPORT_KEY;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.common.base.Strings;
import com.icapp.icapp.Adapters.CarDamageAdapter;
import com.icapp.icapp.Adapters.CarSidesAdapter;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Helpers.TimeUtils;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class NewCaseReportActivity extends ParentActivity {

    RecyclerView rvCarPhotos;
    RecyclerView rvCarIssuesPhotos;
    RecyclerView rvCarAdditionalPhotos;
    LinearLayout viewAdditional;
    CarSidesAdapter carSidesAdapter;
    CarDamageAdapter carDamageAdapter;
    AppCompatButton btnSubmitLaterReport, btnAddSignature, saveSignature;
    LinearLayout viewCarIssues;
    RelativeLayout containerView;
    TextView tvDate, tvLocation, tvCarPlateNumber, tvTicketNumber, tvValetName;
    CarReport carReport;
    SignaturePad signaturePad;
    Boolean isSigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_new_case_report);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        carReport = getIntent().getParcelableExtra(CAR_REPORT_KEY);

        containerView = findViewById(R.id.containerView);
        rvCarPhotos = findViewById(R.id.rvCarPhotos);
        rvCarIssuesPhotos = findViewById(R.id.rvCarIssuesPhotos);
        btnSubmitLaterReport = findViewById(R.id.btnSubmitLaterReport);
        viewCarIssues = findViewById(R.id.viewCarIssues);
        tvDate = findViewById(R.id.tvDate);
        tvLocation = findViewById(R.id.tvLocation);
        tvCarPlateNumber = findViewById(R.id.tvCarPlateNumber);
        tvTicketNumber = findViewById(R.id.tvTicketNumber);
        tvValetName = findViewById(R.id.tvValetName);
        rvCarAdditionalPhotos = findViewById(R.id.rvCarAdditionalPhotos);
        viewAdditional = findViewById(R.id.viewAdditional);
        btnAddSignature = findViewById(R.id.btnAddSignature);

        configure();

        btnAddSignature.setOnClickListener(view -> {
            showSignatureDialog();
        });

        btnSubmitLaterReport.setOnClickListener(view -> {
            if (isSigned) {
                carReport.setSubmit_later(1);
                saveReportAndBack();
            } else {
                showConfirmationDialog();
            }
        });
    }

    private void saveReportAndBack() {
        RealM.getInstance().insertArrival(carReport);
        removeArrival();
//        removeUser();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    public void addJpgSignatureToGallery(Bitmap signature) {
        try {
            Long time = System.currentTimeMillis();
            File photo = new File(getExternalFilesDir(null), time + ".jpg");
            carReport.setSignature(photo.getAbsolutePath());
            carReport.setSubmit_later(1);
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
        } catch (IOException e) {
            Log.d("TAG", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public void configure() {
        tvDate.setText(TimeUtils.convertDateTime(carReport.getCreatedAt()));
        tvLocation.setText(carReport.getAddress());
        tvCarPlateNumber.setText(carReport.getPlateNumber());
        tvTicketNumber.setText(carReport.getTicketNumber());
        tvValetName.setText(sharedPrefs.get(R.string.FIRST_NAME_KEY, ""));

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

        viewCarIssues.setVisibility(sharedPrefs.get(R.string.DAMAGES_KEY).isEmpty() ? View.GONE : View.VISIBLE);
        carDamageAdapter = new CarDamageAdapter(this, sharedPrefs.get(R.string.DAMAGES_KEY), false);
        rvCarIssuesPhotos.setLayoutManager(new GridLayoutManager(this , 2));
        rvCarIssuesPhotos.setAdapter(carDamageAdapter);

        viewAdditional.setVisibility(sharedPrefs.get(R.string.ADDITIONAL_KEY).isEmpty() ? View.GONE : View.VISIBLE);
        carSidesAdapter = new CarSidesAdapter(this, sharedPrefs.get(R.string.ADDITIONAL_KEY));
        rvCarAdditionalPhotos.setLayoutManager(new GridLayoutManager(this , 2));
        rvCarAdditionalPhotos.setAdapter(carSidesAdapter);
    }

    public void showConfirmationDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.submit_signature);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.findViewById(R.id.cancelBtn).setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.findViewById(R.id.submitLaterBtn).setOnClickListener(view -> {
            carReport.setSubmit_later(2);
            saveReportAndBack();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.submitNowBtn).setOnClickListener(view -> {
            carReport.setSubmit_later(3);
            saveReportAndBack();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showSignatureDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.signature_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        signaturePad = dialog.findViewById(R.id.signature_pad);
        saveSignature = dialog.findViewById(R.id.btnSaveSignature);
        Button btnSubmitWithoutSign = dialog.findViewById(R.id.btnSubmitWithoutSign);
        btnSubmitWithoutSign.setText(R.string.cancel_signature);
        saveSignature.setVisibility(View.VISIBLE);
        btnSubmitWithoutSign.setVisibility(View.VISIBLE);
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
                saveSignature.setVisibility(View.VISIBLE);
                isSigned = true;
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                saveSignature.setVisibility(View.GONE);
                isSigned = false;
            }
        });

        saveSignature.setOnClickListener(view -> {
            addJpgSignatureToGallery(signaturePad.getSignatureBitmap());
            btnAddSignature.setText(R.string.update_signature);
            dialog.dismiss();
        });

        btnSubmitWithoutSign.setOnClickListener(view -> {
            isSigned = false;
            dialog.dismiss();
        });

        dialog.show();
    }
}