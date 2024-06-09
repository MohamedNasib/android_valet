package com.icapp.icapp.Adapters;


import static com.icapp.icapp.Activities.ParentActivity.decodeSampledBitmapFromFile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.icapp.icapp.Activities.MainActivity;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Helpers.TimeUtils;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class CarFleetAdapter extends RecyclerView.Adapter<CarFleetViewHolder> {
    ArrayList<CarReport> reports;
    MainActivity activity;

    public CarFleetAdapter(MainActivity activity, ArrayList<CarReport> reports) {
        this.reports = reports;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CarFleetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_fleet_layout, null, false);
        return new CarFleetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarFleetViewHolder holder, int position) {
        holder.onBind(activity, reports.get(position));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }
}
class CarFleetViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout containerView;
    ImageView carImage;
    TextView carNumber, carDate;
    Boolean isSigned = false;
    MainActivity activity;

    public CarFleetViewHolder(@NonNull View itemView) {
        super(itemView);
        containerView = itemView.findViewById(R.id.containerView);
        carImage = itemView.findViewById(R.id.carImage);
        carNumber = itemView.findViewById(R.id.carNumber);
        carDate = itemView.findViewById(R.id.carDate);
    }

    public void onBind(MainActivity activity, CarReport carReport) {
        this.activity = activity;

        if (carReport.getFront_image() != null) {
            if (carReport.getFront_image().contains("https")) {
                Picasso.get().load(carReport.getFront_image()).resize(74, 50).into(carImage);
            } else {
                carImage.setImageBitmap(decodeSampledBitmapFromFile(carReport.getFront_image(), 74, 50));
            }
        }
        carNumber.setText(carReport.getPlateNumber());
        carDate.setText(TimeUtils.convertDateTime(carReport.getCreatedAt()));
        containerView.setTag(carReport.getId());
        containerView.setOnClickListener(view -> {
            isSigned = false;

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.signature_dialog);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            SignaturePad signaturePad = dialog.findViewById(R.id.signature_pad);
            Button saveSignature = dialog.findViewById(R.id.btnSaveSignature);
            Button btnSubmitWithoutSign = dialog.findViewById(R.id.btnSubmitWithoutSign);
            saveSignature.setText(R.string.cancel_signature);
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
//                    saveSignature.setVisibility(View.VISIBLE);
                    isSigned = true;
                    saveSignature.setText(R.string.save_signature);
                }

                @Override
                public void onClear() {
                    //Event triggered when the pad is cleared
//                    saveSignature.setVisibility(View.GONE);
                    isSigned = false;
                    saveSignature.setText(R.string.cancel_signature);
                }
            });

            saveSignature.setOnClickListener(v -> {
                if (isSigned) {
                    addJpgSignatureToGallery(signaturePad.getSignatureBitmap());
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            });

            btnSubmitWithoutSign.setOnClickListener(v -> {

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle(R.string.submit_with_signature);
                alert.setMessage(R.string.are_you_sure);
                alert.setPositiveButton(R.string.yes, (dialog1, which) -> {

                    RealM.getInstance().beginTransaction();
                    CarReport report = RealM.getInstance().findArrivalByID((String) containerView.getTag());
                    report.setSubmit_later(3);
                    report.setRequire_sign_sync(1);
                    RealM.getInstance().commitTransaction();
                    activity.refreshCarFleet();

                    dialog1.dismiss();
                    dialog.dismiss();
                });

                alert.setNegativeButton(R.string.no, (dialog2, which) -> dialog2.dismiss());

                alert.show();
            });

            dialog.show();

        });
    }

    public void addJpgSignatureToGallery(Bitmap signature) {
        try {
            Long time = System.currentTimeMillis();
            File photo = new File(activity.getExternalFilesDir(null), time + ".jpg");
            saveBitmapToJPG(signature, photo);
            scanMediaFile(activity, photo);

            RealM.getInstance().beginTransaction();
            CarReport carReport = RealM.getInstance().findArrivalByID((String) containerView.getTag());
            carReport.setSignature(photo.getAbsolutePath());
            carReport.setSubmit_later(1);
            carReport.setRequire_sign_sync(1);
            RealM.getInstance().commitTransaction();
            activity.refreshCarFleet();;

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

    private void scanMediaFile(Context context, File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }
}
