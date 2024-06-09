package com.icapp.icapp.Activities;

import android.os.Bundle;
import android.os.Environment;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icapp.icapp.BroadcastReceiver.NotificationCenter;
import com.icapp.icapp.BroadcastReceiver.NotificationType;
import com.icapp.icapp.Helpers.PDFDownloaderTask;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.Models.CarUpdate;
import com.icapp.icapp.Models.Output.AddSignature.AddSignatureResponse;
import com.icapp.icapp.Models.Output.AddSignature.IssuesPhotosItem;
import com.icapp.icapp.Models.Output.Arrival.ArrivalResponse;
import com.icapp.icapp.Models.Output.Arrivals.AdditionalPhotosItem;
import com.icapp.icapp.Models.Output.Arrivals.ArrivalsResponse;
import com.icapp.icapp.Models.Output.Arrivals.DataItem;
import com.icapp.icapp.Models.Output.UpdateCase.UpdateCaseResponse;
import com.icapp.icapp.Models.Output.UpdateStatus.UpdateStatusResponse;
import com.icapp.icapp.R;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class SyncActivity extends ParentActivity implements PDFDownloaderTask.DownloadCallback {

    ArrayList<CarReport> reports = new ArrayList<>();
    ArrayList<CarReport> sign_reports = new ArrayList<>();
    ArrayList<CarReport> status_reports = new ArrayList<>();
    ArrayList<CarUpdate> update_reports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void syncInBackground() {
        if (isConnected()) {
            reports = RealM.getInstance().getArrivalList().stream().filter(report -> report.getRequire_full_sync() == 1).collect(Collectors.toCollection(ArrayList::new));

            if (!reports.isEmpty()) {
                addArrival(reports.get(0)); // send report
            } else {
                sign_reports = RealM.getInstance().getArrivalList().stream().filter(report -> report.getRequire_sign_sync() == 1).collect(Collectors.toCollection(ArrayList::new));

                if (!sign_reports.isEmpty()) {
                    sendSignature(sign_reports.get(0)); // send signature
                } else {
                    status_reports = RealM.getInstance().getArrivalList().stream().filter(report -> report.getRequire_status_sync() == 1).collect(Collectors.toCollection(ArrayList::new));

                    if (!status_reports.isEmpty()) {
                        sendStatus(status_reports.get(0));
                    } else {
                        update_reports = RealM.getInstance().getUpdateList();

                        if (!update_reports.isEmpty()) {
                            addUpdate(update_reports.get(0));
                        } else {
                            // load from server
                            retrofit.getAllArrivals(sharedPrefs.get(R.string.USER_ID_KEY, ""), Long.valueOf(sharedPrefs.get(R.string.LAST_SYNC_KEY, "0")), this);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSuccess(Response response, String msg) {
        if (msg.equals("duplicate") || response.body() instanceof ArrivalResponse) {

            if (!reports.isEmpty() && reports.get(0).getId().startsWith("_")) {
                RealM.getInstance().deleteArrival(reports.get(0).getId());
                //on response, when we want to trigger and send notification that our job is finished
                NotificationCenter.postNotification(this, NotificationType.UpdateSyncCount, new HashMap<>());
            }
            syncInBackground();

        } else if (response.body() instanceof AddSignatureResponse) {

            RealM.getInstance().beginTransaction();
            RealM.getInstance().findArrivalByID(sign_reports.get(0).getId()).setRequire_sign_sync(0);
            RealM.getInstance().commitTransaction();
            syncInBackground();

        } else if (response.body() instanceof UpdateStatusResponse) {

            RealM.getInstance().beginTransaction();
            RealM.getInstance().findArrivalByID(status_reports.get(0).getId()).setRequire_status_sync(0);
            RealM.getInstance().commitTransaction();
            syncInBackground();

        } else if (response.body() instanceof UpdateCaseResponse) {

            RealM.getInstance().deleteUpdate(update_reports.get(0).getId());
            syncInBackground();

        } else if (response.body() instanceof ArrivalsResponse) {

            for (DataItem arrival : ((ArrivalsResponse) response.body()).getData()) {

                RealmList<CarPart> damages = new RealmList<>();
                for (IssuesPhotosItem issue : arrival.getIssuesPhotos()) {
                    damages.add(new CarPart(issue.getName(), "", 0L));
                }

                RealmList<CarPart> additionals = new RealmList<>();
                for (AdditionalPhotosItem addition : arrival.getAdditionalPhotos()) {
                    additionals.add(new CarPart(addition.getName(), "", 0L));
                }

                CarReport carReport = new CarReport(String.valueOf(arrival.getId()),
                        arrival.getStatus(),
                        arrival.getCreatedAt(),
                        arrival.getUpdatedAt(),
                        arrival.getPlateNumber(),
                        arrival.getTicketNumber(),
                        arrival.getFrontImage(),
                        arrival.getFrontImageDate(),
                        arrival.getLeftImage(),
                        arrival.getLeftImageDate(),
                        arrival.getRightImage(),
                        arrival.getRightImageDate(),
                        arrival.getBackImage(),
                        arrival.getBackImageDate(),
                        arrival.getAnotherFrontImage(),
                        arrival.getAnotherFrontImageDate(),
                        arrival.getAnotherLeftImage(),
                        arrival.getAnotherLeftImageDate(),
                        arrival.getAnotherRightImage(),
                        arrival.getAnotherRightImageDate(),
                        arrival.getAnotherBackImage(),
                        arrival.getAnotherBackImageDate(),
                        new Gson().toJson(damages),
                        new Gson().toJson(additionals),
                        arrival.getSignatureImage(),
                        String.valueOf(arrival.getUserId()),
                        String.valueOf(arrival.getUsername()),
                        String.valueOf(arrival.getUserImage()),
                        Double.valueOf(arrival.getLatitude()),
                        Double.valueOf(arrival.getLongitude()),
                        arrival.getAddress(),
                        arrival.getSubmitLater(),
                        0, 0, 0, arrival.getPdf());

                new PDFDownloaderTask(this).execute(arrival.getPdf(), getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + arrival.getId() + ".pdf");

                if (RealM.getInstance().findArrivalByID(carReport.getId()) == null) {
                    RealM.getInstance().insertArrival(carReport);
                } else {
                    RealM.getInstance().updateArrival(carReport);
                }
            }
            sharedPrefs.set(R.string.LAST_SYNC_KEY, String.valueOf(System.currentTimeMillis()));
            RealM.getInstance().deleteFinalExits();
        }
    }

    MultipartBody.Part signaturePart;
    MultipartBody.Part anotherFrontPart;
    MultipartBody.Part anotherBackPart;
    MultipartBody.Part anotherRightPart;
    MultipartBody.Part anotherLeftPart;
    public void addArrival(CarReport carReport) {

        Map<String, RequestBody> fieldMap = new HashMap<>();
        fieldMap.put("ticket_number", RequestBody.create(MediaType.parse("text/plain"), carReport.getTicketNumber()));
        fieldMap.put("plate_number", RequestBody.create(MediaType.parse("text/plain"), carReport.getPlateNumber()));
        fieldMap.put("front_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getFront_date().toString()));
        fieldMap.put("left_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getLeft_date().toString()));
        fieldMap.put("back_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getBack_date().toString()));
        fieldMap.put("right_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getRight_date().toString()));
        fieldMap.put("user_id", RequestBody.create(MediaType.parse("text/plain"), carReport.getUser_id()));
        fieldMap.put("latitude", RequestBody.create(MediaType.parse("text/plain"), carReport.getLatitude().toString()));
        fieldMap.put("longitude", RequestBody.create(MediaType.parse("text/plain"), carReport.getLongitude().toString()));
        fieldMap.put("address", RequestBody.create(MediaType.parse("text/plain"), carReport.getAddress() + "."));
        fieldMap.put("submit_later", RequestBody.create(MediaType.parse("text/plain"), carReport.getSubmit_later().toString()));
        fieldMap.put("time", RequestBody.create(MediaType.parse("text/plain"), carReport.getCreatedAt().toString()));

        File frontFile = new File(Objects.requireNonNull(carReport.getFront_image()));
        RequestBody frontBody = RequestBody.create(MediaType.parse("image/jpeg"), frontFile);
        MultipartBody.Part frontPart = MultipartBody.Part.createFormData("front_image", frontFile.getName(), frontBody);

        File leftFile = new File(Objects.requireNonNull(carReport.getLeft_image()));
        RequestBody leftBody = RequestBody.create(MediaType.parse("image/jpeg"), leftFile);
        MultipartBody.Part leftPart = MultipartBody.Part.createFormData("left_image", leftFile.getName(), leftBody);

        File backFile = new File(Objects.requireNonNull(carReport.getBack_image()));
        RequestBody backBody = RequestBody.create(MediaType.parse("image/jpeg"), backFile);
        MultipartBody.Part backPart = MultipartBody.Part.createFormData("back_image", backFile.getName(), backBody);

        File rightFile = new File(Objects.requireNonNull(carReport.getRight_image()));
        RequestBody rightBody = RequestBody.create(MediaType.parse("image/jpeg"), rightFile);
        MultipartBody.Part rightPart = MultipartBody.Part.createFormData("right_image", rightFile.getName(), rightBody);

        Type type = new TypeToken<ArrayList<CarPart>>() {
        }.getType();
        ArrayList<CarPart> damagesList = new Gson().fromJson(carReport.getDamages(), type);
        ArrayList<CarPart> additionalList = new Gson().fromJson(carReport.getAdditional(), type);

        List<MultipartBody.Part> damages = new ArrayList<>();
        if (damagesList != null) {
            for (int i = 0; i < damagesList.size(); i++) {
                File file = new File(Objects.requireNonNull(Objects.requireNonNull(damagesList.get(i)).getPath()));
                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("issues_photos[" + i + "]", file.getName(), body);
                damages.add(part);
                fieldMap.put("issues_photos_part[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), file.getName()));
            }
        }

        List<MultipartBody.Part> additional = new ArrayList<>();
        if (additionalList != null) {
            for (int i = 0; i < additionalList.size(); i++) {
                File file = new File(Objects.requireNonNull(Objects.requireNonNull(additionalList.get(i)).getPath()));
                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("additional_photos[" + i + "]", file.getName(), body);
                additional.add(part);
            }
        }

        if (!Strings.isNullOrEmpty(carReport.getSignature())) {
            File signatureFile = new File(Objects.requireNonNull(carReport.getSignature()));
            RequestBody signatureBody = RequestBody.create(MediaType.parse("image/jpeg"), signatureFile);
            signaturePart = MultipartBody.Part.createFormData("signature_image", signatureFile.getName(), signatureBody);
        }

        if (!Strings.isNullOrEmpty(carReport.getAnother_front_image())) {
            fieldMap.put("another_front_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getAnother_front_date().toString()));
            File anotherFrontFile = new File(Objects.requireNonNull(carReport.getAnother_front_image()));
            RequestBody anotherFrontBody = RequestBody.create(MediaType.parse("image/jpeg"), anotherFrontFile);
            anotherFrontPart = MultipartBody.Part.createFormData("another_front_image", anotherFrontFile.getName(), anotherFrontBody);
        }

        if (!Strings.isNullOrEmpty(carReport.getAnother_left_image())) {
            fieldMap.put("another_left_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getAnother_left_date().toString()));
            File anotherLeftFile = new File(Objects.requireNonNull(carReport.getAnother_left_image()));
            RequestBody anotherLeftBody = RequestBody.create(MediaType.parse("image/jpeg"), anotherLeftFile);
            anotherLeftPart = MultipartBody.Part.createFormData("another_left_image", anotherLeftFile.getName(), anotherLeftBody);
        }

        if (!Strings.isNullOrEmpty(carReport.getAnother_back_image())) {
            fieldMap.put("another_back_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getAnother_back_date().toString()));
            File anotherBackFile = new File(Objects.requireNonNull(carReport.getAnother_back_image()));
            RequestBody anotherBackBody = RequestBody.create(MediaType.parse("image/jpeg"), anotherBackFile);
            anotherBackPart = MultipartBody.Part.createFormData("another_back_image", anotherBackFile.getName(), anotherBackBody);
        }

        if (!Strings.isNullOrEmpty(carReport.getAnother_right_image())) {
            fieldMap.put("another_right_image_date", RequestBody.create(MediaType.parse("text/plain"), carReport.getAnother_right_date().toString()));
            File anotherRightFile = new File(Objects.requireNonNull(carReport.getAnother_right_image()));
            RequestBody anotherRightBody = RequestBody.create(MediaType.parse("image/jpeg"), anotherRightFile);
            anotherRightPart = MultipartBody.Part.createFormData("another_right_image", anotherRightFile.getName(), anotherRightBody);
        }

        retrofit.arrival(
                fieldMap,
                frontPart,
                leftPart,
                backPart,
                rightPart,
                Strings.isNullOrEmpty(carReport.getAnother_front_image()) ? null : anotherFrontPart,
                Strings.isNullOrEmpty(carReport.getAnother_left_image()) ? null : anotherLeftPart,
                Strings.isNullOrEmpty(carReport.getAnother_back_image()) ? null : anotherBackPart,
                Strings.isNullOrEmpty(carReport.getAnother_right_image()) ? null : anotherRightPart,
                Strings.isNullOrEmpty(carReport.getSignature()) ? null : signaturePart,
                damages,
                additional,
                this);
    }

    public void sendSignature(CarReport carReport) {
        File file = new File(Objects.requireNonNull(carReport.getSignature()));
        RequestBody signatureBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part signaturePart = MultipartBody.Part.createFormData("signature_image", file.getName(), signatureBody);
        retrofit.addSignature(carReport.getId(), signaturePart,
                RequestBody.create(MediaType.parse("text/plain"), "PUT"),
                this);
    }

    public void sendStatus(CarReport carReport) {
        retrofit.updateStatus(carReport.getId(),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(carReport.getStatus())),
                RequestBody.create(MediaType.parse("text/plain"), "PUT"),
                this);
    }

    public void addUpdate(CarUpdate carUpdate) {
        Map<String, RequestBody> fieldMap = new HashMap<>();
//        fieldMap.put("ticket_number", RequestBody.create(MediaType.parse("text/plain"), carUpdate.getTicketNumber()));
        fieldMap.put("time", RequestBody.create(MediaType.parse("text/plain"), carUpdate.getCreatedAt().toString()));
        fieldMap.put("user_id", RequestBody.create(MediaType.parse("text/plain"), carUpdate.getUser_id()));
        fieldMap.put("ticket_id", RequestBody.create(MediaType.parse("text/plain"), carUpdate.getTicket_id()));

        Type type = new TypeToken<ArrayList<CarPart>>() {
        }.getType();
        ArrayList<CarPart> damagesList = new Gson().fromJson(carUpdate.getDamages(), type);

        List<MultipartBody.Part> damages = new ArrayList<>();
        if (damagesList != null) {
            for (int i = 0; i < damagesList.size(); i++) {
                File file = new File(Objects.requireNonNull(Objects.requireNonNull(damagesList.get(i)).getPath()));
                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("issues_photos[" + i + "]", file.getName(), body);
                damages.add(part);
                fieldMap.put("issues_photos_part[" + i + "]", RequestBody.create(MediaType.parse("text/plain"), file.getName()));
            }
        }
        retrofit.updateCase(fieldMap, damages, this);
    }

    @Override
    public void onProgressUpdate(int progress) {
        // Update progress UI
    }

    @Override
    public void onDownloadComplete() {
        // Handle download completion
    }

    @Override
    public void onDownloadError(String errorMessage) {
        // Handle download error
    }
}