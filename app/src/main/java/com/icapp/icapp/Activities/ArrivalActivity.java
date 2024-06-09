package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.ADDITIONAL_PHOTO_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_BACK_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_FRONT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_LEFT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.ANOTHER_RIGHT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.BACK_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.CAR_REPORT_KEY;
import static com.icapp.icapp.Helpers.Constants.FRONT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.LEFT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.RIGHT_SIDE_REQUEST_CODE;
import static com.icapp.icapp.Helpers.Constants.SIDE_KEY;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.common.base.Strings;
import com.icapp.icapp.Adapters.CarDamageAdapter;
import com.icapp.icapp.Adapters.CarPartAdapter;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ArrivalActivity extends ParentActivity {

    Button btnSubmit;
    LinearLayout viewFrontSide, viewLeftSide, viewBackSide, viewRightSide, viewtvTicketNumber;
    RelativeLayout viewAnotherFrontSide, viewAnotherLeftSide, viewAnotherBackSide, viewAnotherRightSide;
    LinearLayout viewtvRetakeFrontSide, viewtvRetakeLeftSide, viewtvRetakeBackSide, viewtvRetakeRightSide;
    ImageView imgFrontSide, imgLeftSide, imgBackSide, imgRightSide, imgSideSelect;
    ImageView imgAnotherFrontSide, imgAnotherLeftSide, imgAnotherBackSide, imgAnotherRightSide;
    TextView tvRetakeFrontSide, tvRetakeLeftSide, tvRetakeBackSide, tvRetakeRightSide, tvTicketNumber, tvTicketScanDescription;
    ImageButton ic_plus_front, ic_plus_left, ic_plus_back, ic_plus_right;
    ImageButton deleteFrontImage, deleteLeftImage, deleteBackImage, deleteRightImage;
    RelativeLayout btnScanTicket;
    RecyclerView rvCarIssuesPhotos, rvCarAdditionalPhotos;
    TextView tvAdditionalPhotoCount;
    RelativeLayout btnStartTakingPhoto, viewIssuesPhoto;
    LinearLayout starting_layout;
    CarDamageAdapter carDamageAdapter;
    CarPartAdapter carPartAdapter;
    private int selectedSide = FRONT_SIDE_REQUEST_CODE;
    CarReport carReport;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_arrival);

        carReport = new CarReport();

        initialize();
        implementActions();
        requestLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayPlateAndUser();
        displayTicketNumber();
        displayTakenImages();
        displayAdapters();
    }

    private void initialize() {
        btnSubmit = findViewById(R.id.btnSubmit);
        btnScanTicket = findViewById(R.id.btnScanTicket);

        rvCarIssuesPhotos = findViewById(R.id.rvCarIssuesPhotos);
        rvCarAdditionalPhotos = findViewById(R.id.rvCarAdditionalPhotos);
        tvAdditionalPhotoCount = findViewById(R.id.tvAdditionalPhotoCount);
        btnStartTakingPhoto = findViewById(R.id.btnStartTakingPhoto);
        viewIssuesPhoto = findViewById(R.id.viewIssuesPhoto);

        viewFrontSide = findViewById(R.id.viewFrontSide);
        viewLeftSide = findViewById(R.id.viewLeftSide);
        viewBackSide = findViewById(R.id.viewBackSide);
        viewRightSide = findViewById(R.id.viewRightSide);
        viewtvTicketNumber = findViewById(R.id.viewtvTicketNumber);
        viewAnotherFrontSide = findViewById(R.id.viewAnotherFrontSide);
        viewAnotherLeftSide = findViewById(R.id.viewAnotherLeftSide);
        viewAnotherBackSide = findViewById(R.id.viewAnotherBackSide);
        viewAnotherRightSide = findViewById(R.id.viewAnotherRightSide);

        imgFrontSide = findViewById(R.id.imgFrontSide);
        imgLeftSide = findViewById(R.id.imgLeftSide);
        imgBackSide = findViewById(R.id.imgBackSide);
        imgRightSide = findViewById(R.id.imgRightSide);
        imgSideSelect = findViewById(R.id.imgSideSelect);
        imgAnotherFrontSide = findViewById(R.id.imgAnotherFrontSide);
        imgAnotherLeftSide = findViewById(R.id.imgAnotherLeftSide);
        imgAnotherBackSide = findViewById(R.id.imgAnotherBackSide);
        imgAnotherRightSide = findViewById(R.id.imgAnotherRightSide);
        ic_plus_front = findViewById(R.id.ic_plus_front);
        ic_plus_left = findViewById(R.id.ic_plus_left);
        ic_plus_back = findViewById(R.id.ic_plus_back);
        ic_plus_right = findViewById(R.id.ic_plus_right);
        deleteFrontImage = findViewById(R.id.deleteFrontImage);
        deleteLeftImage = findViewById(R.id.deleteLeftImage);
        deleteBackImage = findViewById(R.id.deleteBackImage);
        deleteRightImage = findViewById(R.id.deleteRightImage);

        tvRetakeFrontSide = findViewById(R.id.tvRetakeFrontSide);
        tvRetakeLeftSide = findViewById(R.id.tvRetakeLeftSide);
        tvRetakeBackSide = findViewById(R.id.tvRetakeBackSide);
        tvRetakeRightSide = findViewById(R.id.tvRetakeRightSide);
        tvTicketNumber = findViewById(R.id.tvTicketNumber);
        tvTicketScanDescription = findViewById(R.id.tvTicketScanDescription);

        viewtvRetakeFrontSide = findViewById(R.id.viewtvRetakeFrontSide);
        viewtvRetakeLeftSide = findViewById(R.id.viewtvRetakeLeftSide);
        viewtvRetakeBackSide = findViewById(R.id.viewtvRetakeBackSide);
        viewtvRetakeRightSide = findViewById(R.id.viewtvRetakeRightSide);

        starting_layout = findViewById(R.id.starting_layout);
    }

    private void implementActions() {
        btnSubmit.setOnClickListener(view -> {

            carReport.setDamages(sharedPrefs.get(R.string.DAMAGES_KEY, null));
            carReport.setAdditional(sharedPrefs.get(R.string.ADDITIONAL_KEY, null));

            startActivity(new Intent(ArrivalActivity.this, NewCaseReportActivity.class)
                    .putExtra(CAR_REPORT_KEY, carReport));
        });

        btnScanTicket.setOnClickListener(view -> startActivity(new Intent(ArrivalActivity.this, TicketScannerActivity.class).putExtra("isDeparture", false)));
        viewtvTicketNumber.setOnClickListener(view -> startActivity(new Intent(ArrivalActivity.this, TicketScannerActivity.class).putExtra("isDeparture", false)));

        imgSideSelect.setOnClickListener(view -> {
            switch (selectedSide) {
                case LEFT_SIDE_REQUEST_CODE:
                    viewLeftSide.performClick();
                    break;
                case BACK_SIDE_REQUEST_CODE:
                    viewBackSide.performClick();
                    break;
                case RIGHT_SIDE_REQUEST_CODE:
                    viewRightSide.performClick();
                    break;
                case FRONT_SIDE_REQUEST_CODE:
                    viewFrontSide.performClick();
                    break;
                default:
                    break;
            }
        });

        viewFrontSide.setOnClickListener(view -> startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                .putExtra(SIDE_KEY, FRONT_SIDE_REQUEST_CODE)));
        viewLeftSide.setOnClickListener(view -> startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                .putExtra(SIDE_KEY, LEFT_SIDE_REQUEST_CODE)));
        viewBackSide.setOnClickListener(view -> startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                .putExtra(SIDE_KEY, BACK_SIDE_REQUEST_CODE)));
        viewRightSide.setOnClickListener(view -> startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                .putExtra(SIDE_KEY, RIGHT_SIDE_REQUEST_CODE)));

        ic_plus_front.setOnClickListener(view -> {
            if (sharedPrefs.get(R.string.FRONT_IMAGE_KEY, "").isEmpty()) {
                showToast(getString(R.string.please_select_front_image));
            } else {
                startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                        .putExtra(SIDE_KEY, ANOTHER_FRONT_SIDE_REQUEST_CODE));
            }
        });
        ic_plus_left.setOnClickListener(view -> {
            if (sharedPrefs.get(R.string.LEFT_IMAGE_KEY, "").isEmpty()) {
                showToast(getString(R.string.please_select_left_image));
            } else {
                startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                        .putExtra(SIDE_KEY, ANOTHER_LEFT_SIDE_REQUEST_CODE));
            }
        });
        ic_plus_back.setOnClickListener(view -> {
            if (sharedPrefs.get(R.string.BACK_IMAGE_KEY, "").isEmpty()) {
                showToast(getString(R.string.please_select_back_image));
            } else {
                startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                        .putExtra(SIDE_KEY, ANOTHER_BACK_SIDE_REQUEST_CODE));
            }
        });
        ic_plus_right.setOnClickListener(view -> {
            if (sharedPrefs.get(R.string.RIGHT_IMAGE_KEY, "").isEmpty()) {
                showToast(getString(R.string.please_select_right_image));
            } else {
                startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                        .putExtra(SIDE_KEY, ANOTHER_RIGHT_SIDE_REQUEST_CODE));
            }
        });

        btnStartTakingPhoto.setOnClickListener(view -> {
            if (sharedPrefs.get(R.string.ADDITIONAL_KEY).size() < 10) {
                startActivity(new Intent(ArrivalActivity.this, PickSideActivity.class)
                        .putExtra(SIDE_KEY, ADDITIONAL_PHOTO_REQUEST_CODE));
            }
        });

        imgAnotherFrontSide.setOnClickListener(view -> ic_plus_front.performClick());
        imgAnotherLeftSide.setOnClickListener(view -> ic_plus_left.performClick());
        imgAnotherBackSide.setOnClickListener(view -> ic_plus_back.performClick());
        imgAnotherRightSide.setOnClickListener(view -> ic_plus_right.performClick());

        deleteFrontImage.setOnClickListener(view -> {
            sharedPrefs.remove(R.string.ANOTHER_FRONT_IMAGE_KEY);
            carReport.setAnother_front_image("");
            carReport.setAnother_front_date(0L);
            ic_plus_front.setVisibility(View.VISIBLE);
            viewAnotherFrontSide.setVisibility(View.GONE);
        });
        deleteLeftImage.setOnClickListener(view -> {
            sharedPrefs.remove(R.string.ANOTHER_LEFT_IMAGE_KEY);
            carReport.setAnother_left_image("");
            carReport.setAnother_left_date(0L);
            ic_plus_left.setVisibility(View.VISIBLE);
            viewAnotherLeftSide.setVisibility(View.GONE);
        });
        deleteBackImage.setOnClickListener(view -> {
            sharedPrefs.remove(R.string.ANOTHER_BACK_IMAGE_KEY);
            carReport.setAnother_back_image("");
            carReport.setAnother_back_date(0L);
            ic_plus_back.setVisibility(View.VISIBLE);
            viewAnotherBackSide.setVisibility(View.GONE);
        });
        deleteRightImage.setOnClickListener(view -> {
            sharedPrefs.remove(R.string.ANOTHER_RIGHT_IMAGE_KEY);
            carReport.setAnother_right_image("");
            carReport.setAnother_right_date(0L);
            ic_plus_right.setVisibility(View.VISIBLE);
            viewAnotherRightSide.setVisibility(View.GONE);
        });
    }

    private void requestLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    getAddressFromLocation(latitude, longitude);
                }
            }
        };

        requestLocationUpdates();
    }

    private void displayPlateAndUser() {
        carReport.setUser_id(sharedPrefs.get(R.string.USER_ID_KEY, ""));
        carReport.setUser_name(sharedPrefs.get(R.string.FIRST_NAME_KEY, ""));
        carReport.setUser_image(sharedPrefs.get(R.string.USER_IMAGE_KEY, ""));
    }

    private void displayTicketNumber() {
        String plateNumber = sharedPrefs.get(R.string.REPORT_CAR_PLATE_KEY, "");
        carReport.setPlateNumber(plateNumber);

        String ticketNumber = sharedPrefs.get(R.string.REPORT_TICKET_NUMBER_KEY, "");
        tvTicketNumber.setText(ticketNumber);
        if (!Strings.isNullOrEmpty(ticketNumber)) {
            carReport.setTicketNumber(ticketNumber);
            btnScanTicket.setVisibility(View.GONE);
            viewtvTicketNumber.setVisibility(View.VISIBLE);
            tvTicketScanDescription.setVisibility(View.VISIBLE);
        }
    }

    private void displayTakenImages() {
        if (!sharedPrefs.get(R.string.FRONT_IMAGE_KEY, "").isEmpty()) {
            starting_layout.setVisibility(View.INVISIBLE); // for front only
            selectedSide = LEFT_SIDE_REQUEST_CODE;
            carReport.setFront_image(sharedPrefs.get(R.string.FRONT_IMAGE_KEY, ""));
            carReport.setFront_date(Long.valueOf(sharedPrefs.get(R.string.FRONT_IMAGE_DATE_KEY, "")));
            imgFrontSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getFront_image(), 74, 50));
            viewtvRetakeFrontSide.setVisibility(View.VISIBLE);
        }

        if (!sharedPrefs.get(R.string.LEFT_IMAGE_KEY, "").isEmpty()) {
            selectedSide = BACK_SIDE_REQUEST_CODE;
            carReport.setLeft_image(sharedPrefs.get(R.string.LEFT_IMAGE_KEY, ""));
            carReport.setLeft_date(Long.valueOf(sharedPrefs.get(R.string.LEFT_IMAGE_DATE_KEY, "")));
            imgLeftSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getLeft_image(), 74, 50));
            viewtvRetakeLeftSide.setVisibility(View.VISIBLE);
        }

        if (!sharedPrefs.get(R.string.BACK_IMAGE_KEY, "").isEmpty()) {
            selectedSide = RIGHT_SIDE_REQUEST_CODE;
            carReport.setBack_image(sharedPrefs.get(R.string.BACK_IMAGE_KEY, ""));
            carReport.setBack_date(Long.valueOf(sharedPrefs.get(R.string.BACK_IMAGE_DATE_KEY, "")));
            imgBackSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getBack_image(), 74, 50));
            viewtvRetakeBackSide.setVisibility(View.VISIBLE);
        }

        if (!sharedPrefs.get(R.string.RIGHT_IMAGE_KEY, "").isEmpty()) {
            carReport.setRight_image(sharedPrefs.get(R.string.RIGHT_IMAGE_KEY, ""));
            carReport.setRight_date(Long.valueOf(sharedPrefs.get(R.string.RIGHT_IMAGE_DATE_KEY, "")));
            imgRightSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getRight_image(), 74, 50));
            viewtvRetakeRightSide.setVisibility(View.VISIBLE);
        }

        if (!sharedPrefs.get(R.string.ANOTHER_FRONT_IMAGE_KEY, "").isEmpty()) {
            carReport.setAnother_front_image(sharedPrefs.get(R.string.ANOTHER_FRONT_IMAGE_KEY, ""));
            carReport.setAnother_front_date(Long.valueOf(sharedPrefs.get(R.string.ANOTHER_FRONT_IMAGE_DATE_KEY, "")));
            imgAnotherFrontSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getAnother_front_image(), 74, 50));
//            viewtvRetakeFrontSide.setVisibility(View.VISIBLE);
            ic_plus_front.setVisibility(View.GONE);
            viewAnotherFrontSide.setVisibility(View.VISIBLE);
        } else {
            ic_plus_front.setVisibility(View.VISIBLE);
            viewAnotherFrontSide.setVisibility(View.GONE);
        }

        if (!sharedPrefs.get(R.string.ANOTHER_LEFT_IMAGE_KEY, "").isEmpty()) {
            carReport.setAnother_left_image(sharedPrefs.get(R.string.ANOTHER_LEFT_IMAGE_KEY, ""));
            carReport.setAnother_left_date(Long.valueOf(sharedPrefs.get(R.string.ANOTHER_LEFT_IMAGE_DATE_KEY, "")));
            imgAnotherLeftSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getAnother_left_image(), 74, 50));
//            viewtvRetakeLeftSide.setVisibility(View.VISIBLE);
            ic_plus_left.setVisibility(View.GONE);
            viewAnotherLeftSide.setVisibility(View.VISIBLE);
        } else {
            ic_plus_left.setVisibility(View.VISIBLE);
            viewAnotherLeftSide.setVisibility(View.GONE);
        }

        if (!sharedPrefs.get(R.string.ANOTHER_BACK_IMAGE_KEY, "").isEmpty()) {
            carReport.setAnother_back_image(sharedPrefs.get(R.string.ANOTHER_BACK_IMAGE_KEY, ""));
            carReport.setAnother_back_date(Long.valueOf(sharedPrefs.get(R.string.ANOTHER_BACK_IMAGE_DATE_KEY, "")));
            imgAnotherBackSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getAnother_back_image(), 74, 50));
//            viewtvRetakeBackSide.setVisibility(View.VISIBLE);
            ic_plus_back.setVisibility(View.GONE);
            viewAnotherBackSide.setVisibility(View.VISIBLE);
        } else {
            ic_plus_back.setVisibility(View.VISIBLE);
            viewAnotherBackSide.setVisibility(View.GONE);
        }

        if (!sharedPrefs.get(R.string.ANOTHER_RIGHT_IMAGE_KEY, "").isEmpty()) {
            carReport.setAnother_right_image(sharedPrefs.get(R.string.ANOTHER_RIGHT_IMAGE_KEY, ""));
            carReport.setAnother_right_date(Long.valueOf(sharedPrefs.get(R.string.ANOTHER_RIGHT_IMAGE_DATE_KEY, "")));
            imgAnotherRightSide.setImageBitmap(decodeSampledBitmapFromFile(carReport.getAnother_right_image(), 74, 50));
//            viewtvRetakeRightSide.setVisibility(View.VISIBLE);
            ic_plus_right.setVisibility(View.GONE);
            viewAnotherRightSide.setVisibility(View.VISIBLE);
        } else {
            ic_plus_right.setVisibility(View.VISIBLE);
            viewAnotherRightSide.setVisibility(View.GONE);
        }

        changeSelectedSideImage();
        if (carReport.isValidReport()) {
            btnSubmit.setEnabled(true);
            btnSubmit.setAlpha(1.0F);
        }
    }

    private void displayAdapters() {
        refreshDamages();
        ArrayList<CarPart> damageList = sharedPrefs.get(R.string.DAMAGES_KEY);
        carDamageAdapter = new CarDamageAdapter(this, damageList, false);
        rvCarIssuesPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        rvCarIssuesPhotos.setAdapter(carDamageAdapter);
        viewIssuesPhoto.setVisibility(damageList.isEmpty() ? View.GONE : View.VISIBLE);

        ArrayList<CarPart> additionalList = sharedPrefs.get(R.string.ADDITIONAL_KEY);
        carPartAdapter = new CarPartAdapter(this, additionalList);
        rvCarAdditionalPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        rvCarAdditionalPhotos.setAdapter(carPartAdapter);
        rvCarAdditionalPhotos.setVisibility(additionalList.isEmpty() ? View.GONE : View.VISIBLE);
        tvAdditionalPhotoCount.setText(MessageFormat.format("{0}/10", additionalList.size()));
    }

    private void refreshDamages() {
        ArrayList<CarPart> front_lst = sharedPrefs.get(R.string.FRONT_IMAGE_DAMAGES_KEY);
        ArrayList<CarPart> left_lst = sharedPrefs.get(R.string.LEFT_IMAGE_DAMAGES_KEY);
        ArrayList<CarPart> back_lst = sharedPrefs.get(R.string.BACK_IMAGE_DAMAGES_KEY);
        ArrayList<CarPart> right_lst = sharedPrefs.get(R.string.RIGHT_IMAGE_DAMAGES_KEY);

        ArrayList<CarPart> lst = new ArrayList<>();
        lst.addAll(front_lst);
        lst.addAll(left_lst);
        lst.addAll(back_lst);
        lst.addAll(right_lst);

        sharedPrefs.set(R.string.DAMAGES_KEY, lst);
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                    createLocationRequest(),
                    locationCallback,
                    null /* Looper */);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        }
    }

    private LocationRequest createLocationRequest() {
        return new LocationRequest()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        carReport.setLatitude(latitude);
        carReport.setLongitude(longitude);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!Objects.requireNonNull(addresses).isEmpty()) {
                Address address = addresses.get(0);
                String fullAddress = address.getAddressLine(0);
                carReport.setAddress(fullAddress);
            }
        } catch (IOException e) {
            Log.d("TAG", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                showToast(getString(R.string.location_permission_denied_unable_to_get_current_location));
            }
        }
    }

    private void changeSelectedSideImage() {
        switch (selectedSide) {
            case FRONT_SIDE_REQUEST_CODE:
                imgSideSelect.setImageResource(R.drawable.side1);
                break;
            case LEFT_SIDE_REQUEST_CODE:
                imgSideSelect.setImageResource(R.drawable.side2);
                break;
            case BACK_SIDE_REQUEST_CODE:
                imgSideSelect.setImageResource(R.drawable.side3);
                break;
            case RIGHT_SIDE_REQUEST_CODE:
                imgSideSelect.setImageResource(R.drawable.side4);
                break;
            default:
                break;
        }
    }
}