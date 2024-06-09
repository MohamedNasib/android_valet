package com.icapp.icapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.icapp.icapp.Helpers.FaceComparator;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Interfaces.ICallback;
import com.icapp.icapp.Models.Output.CheckCompany.CheckCompanyResponse;
import com.icapp.icapp.Models.Output.Settings.SettingsResponse;
import com.icapp.icapp.Models.Output.Settings.UsersItem;
import com.icapp.icapp.Models.User;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import retrofit2.Response;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends ParentActivity implements ICallback {
    FaceDetector detector;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    CameraSelector cameraSelector;
    boolean start = true, flipX = false;
    int cam_face = CameraSelector.LENS_FACING_FRONT; //Default Back Camera
    ProcessCameraProvider cameraProvider;
    Integer failedAttemptsCount = 0;
    Integer maxFailedAttemptsCount = 10;
    ImageView face_id_img;
    LinearLayout logo_section, face_id_view, normal_login;
    Dialog companyDialog;
    HashMap<User, Bitmap> registered = new HashMap<>();
    FaceComparator faceComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        face_id_img = findViewById(R.id.face_id_img);
        logo_section = findViewById(R.id.logo_section);
        face_id_view = findViewById(R.id.face_id_view);
        normal_login = findViewById(R.id.normal_login);
        previewView = findViewById(R.id.previewView);

        faceComparator = new FaceComparator(this);

        if (!sharedPrefs.get(R.string.USER_ID_KEY, "").isEmpty()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (sharedPrefs.get(R.string.COMPANY_CODE_KEY, "").isEmpty()) {
            showEmptyShardDialog();
        } else {
            syncFaces(); // load faces from web
            displayPageContent();
        }
    }

    private void syncFaces() {
        if (isConnected()) {
            retrofit.getSettings(sharedPrefs.get(R.string.COMPANY_CODE_KEY, ""), this);
        } else {
            loadFaces(); // from local DB
        }
    }

    private void loadFaces() {
        ArrayList<User> lst = RealM.getInstance().getUserList().stream().filter(user -> user.getFaceImage() != null).collect(Collectors.toCollection(ArrayList::new));
        for (User user : lst) {
            loadImageBitmap(user);
        }
    }

    private void displayPageContent() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            logo_section.setVisibility(View.VISIBLE);
            face_id_view.setVisibility(View.VISIBLE);
        }, 1000);

        //Camera Permission
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }

        //Initialize Face Detector
        FaceDetectorOptions highAccuracyOpts = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build();

        detector = FaceDetection.getClient(highAccuracyOpts);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
                Log.d("TAG", Objects.requireNonNull(e.getMessage()));
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    public void onSuccess(Response response, String msg) {
        if (response.body() instanceof SettingsResponse) {
            SettingsResponse settingsResponse = (SettingsResponse) response.body();

            // To clear DB First
            RealM.getInstance().dropUser();

            for (UsersItem usersItem : settingsResponse.getData().getUsers()) {
                User user = new User(String.valueOf(usersItem.getId()),
                        usersItem.getFirstName(),
                        usersItem.getLastName(),
                        usersItem.getCompanyId(),
                        usersItem.getAreaId(),
                        usersItem.getPassCode(),
                        usersItem.getFaceImage());

                RealM.getInstance().insertUser(user);
            }
            loadFaces(); // from local DB

        } else if (response.body() instanceof CheckCompanyResponse) {
            CheckCompanyResponse checkCompanyResponse = (CheckCompanyResponse) response.body();
            sharedPrefs.set(R.string.COMPANY_CODE_KEY, checkCompanyResponse.getData().getCode());
            companyDialog.dismiss();
            displayPageContent();
            syncFaces();
        }
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        cameraSelector = new CameraSelector.Builder().requireLensFacing(cam_face).build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) //Latest frame is shown
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        imageAnalysis.setAnalyzer(executor, imageProxy -> {

            Image mediaImage = imageProxy.getImage();

            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                //Process acquired image to detect faces
                detector.process(Objects.requireNonNull(image)).addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        Face face = faces.get(0);

                        //mediaImage to Bitmap
                        Bitmap frame_bmp = toBitmap(mediaImage);
                        int rot = imageProxy.getImageInfo().getRotationDegrees();

                        //Adjust orientation of Face
                        Bitmap frame_bmp1 = rotateBitmap(frame_bmp, rot, false, false);

                        //Get bounding box of face
//                        RectF boundingBox = new RectF(face.getBoundingBox());

                        //Crop out bounding box from whole Bitmap(image)
//                        Bitmap cropped_face = getCropBitmapByCPU(frame_bmp1, boundingBox);

//                        if (flipX)
//                            cropped_face = rotateBitmap(cropped_face, 0, flipX, false);

                        //Scale the acquired Face to 112*112 which is required input for model
//                        Bitmap scaled = getResizedBitmap(frame_bmp1, 112, 112);

                        if (start)
                            recognizeImage(frame_bmp1); //Send scaled bitmap to create face embeddings.
                    }
                }).addOnFailureListener(e -> {
                }).addOnCompleteListener(task -> {
                    imageProxy.close(); //v.important to acquire next frame for analysis
                });
            }
        });

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    public void recognizeImage(final Bitmap bitmap) {
        Double max = 0.0;
        User selectedUser = null;
        for (User user : registered.keySet()) {
            Double current = faceComparator.compareFaces(this, bitmap, Objects.requireNonNull(registered.get(user)));
            if (current > max && current > 0.5) {
                max = current;
                selectedUser = user;
            }
        }
        if (selectedUser != null)  {
            sharedPrefs.set(R.string.USER_ID_KEY, selectedUser.getId());
            sharedPrefs.set(R.string.FIRST_NAME_KEY, selectedUser.getFirstName());
            sharedPrefs.set(R.string.LAST_NAME_KEY, selectedUser.getLastName());
            sharedPrefs.set(R.string.USER_IMAGE_KEY, selectedUser.getFaceImage());

            face_id_img.setImageResource(R.drawable.touch_id_symbol);
            start = false;
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        unknownFace(); // in case of registered is empty or not found identical
    }

    private void unknownFace() {
        failedAttemptsCount += 1;
        if (failedAttemptsCount == maxFailedAttemptsCount) {
            start = false;
            showWrongFaceDialog();
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private static Bitmap getCropBitmapByCPU(Bitmap source, RectF cropRectF) {
        Bitmap resultBitmap = Bitmap.createBitmap((int) cropRectF.width(), (int) cropRectF.height(), Bitmap.Config.ARGB_8888);
        Canvas cavas = new Canvas(resultBitmap);

        // draw background
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setColor(Color.WHITE);
        cavas.drawRect(new RectF(0, 0, cropRectF.width(), cropRectF.height()), paint);

        Matrix matrix = new Matrix();
        matrix.postTranslate(-cropRectF.left, -cropRectF.top);

        cavas.drawBitmap(source, matrix, paint);

        if (!source.isRecycled()) {
            source.recycle();
        }

        return resultBitmap;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotationDegrees, boolean flipX, boolean flipY) {
        Matrix matrix = new Matrix();

        // Rotate the image back to straight.
        matrix.postRotate(rotationDegrees);

        // Mirror the image along the X or Y axis.
        matrix.postScale(flipX ? -1.0f : 1.0f, flipY ? -1.0f : 1.0f);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // Recycle the old bitmap if it has changed.
        if (rotatedBitmap != bitmap) {
            bitmap.recycle();
        }
        return rotatedBitmap;
    }

    //IMPORTANT. If conversion not done ,the toBitmap conversion does not work on some devices.
    private static byte[] YUV_420_888toNV21(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int ySize = width * height;
        int uvSize = width * height / 4;

        byte[] nv21 = new byte[ySize + uvSize * 2];

        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer(); // Y
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer(); // U
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer(); // V

        int rowStride = image.getPlanes()[0].getRowStride();
        assert (image.getPlanes()[0].getPixelStride() == 1);

        int pos = 0;

        if (rowStride == width) { // likely
            yBuffer.get(nv21, 0, ySize);
            pos += ySize;
        } else {
            long yBufferPos = -rowStride; // not an actual position
            for (; pos < ySize; pos += width) {
                yBufferPos += rowStride;
                yBuffer.position((int) yBufferPos);
                yBuffer.get(nv21, pos, width);
            }
        }

        rowStride = image.getPlanes()[2].getRowStride();
        int pixelStride = image.getPlanes()[2].getPixelStride();

        assert (rowStride == image.getPlanes()[1].getRowStride());
        assert (pixelStride == image.getPlanes()[1].getPixelStride());

        if (pixelStride == 2 && rowStride == width && uBuffer.get(0) == vBuffer.get(1)) {
            // maybe V an U planes overlap as per NV21, which means vBuffer[1] is alias of uBuffer[0]
            byte savePixel = vBuffer.get(1);
            try {
                vBuffer.put(1, (byte) ~savePixel);
                if (uBuffer.get(0) == (byte) ~savePixel) {
                    vBuffer.put(1, savePixel);
                    vBuffer.position(0);
                    uBuffer.position(0);
                    vBuffer.get(nv21, ySize, 1);
                    uBuffer.get(nv21, ySize + 1, uBuffer.remaining());

                    return nv21; // shortcut
                }
            } catch (Exception e) {
                Log.d("TAG", Objects.requireNonNull(e.getMessage()));
            }

            // unfortunately, the check failed. We must save U and V pixel by pixel
            vBuffer.put(1, savePixel);
        }

        // other optimizations could check if (pixelStride == 1) or (pixelStride == 2),
        // but performance gain would be less significant

        for (int row = 0; row < height / 2; row++) {
            for (int col = 0; col < width / 2; col++) {
                int vuPos = col * pixelStride + row * rowStride;
                nv21[pos++] = vBuffer.get(vuPos);
                nv21[pos++] = uBuffer.get(vuPos);
            }
        }

        return nv21;
    }

    private Bitmap toBitmap(Image image) {
        byte[] nv21 = YUV_420_888toNV21(image);
        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public void loginPressed(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void newUserPressed(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void showWrongFaceDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.failed_faceid_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.findViewById(R.id.enterPasswordBtn).setOnClickListener(v -> {
            face_id_view.setVisibility(View.GONE);
            normal_login.setVisibility(View.VISIBLE);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.cancelBtn).setOnClickListener(v -> {
            failedAttemptsCount = 0;
            start = true;
            dialog.cancel();
        });

        dialog.show();
    }

    public void showEmptyShardDialog() {
        companyDialog = new Dialog(this);
        companyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        companyDialog.setCancelable(false);
        companyDialog.setContentView(R.layout.empty_sherd_dialog);
        Objects.requireNonNull(companyDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        EditText et_enterCode = companyDialog.findViewById(R.id.et_enterCode);
        companyDialog.findViewById(R.id.okBtn).setOnClickListener(v -> {
            if (isConnected()) {
                retrofit.checkCompany(et_enterCode.getText().toString(), this);
            } else {
                showToast(getString(R.string.check_your_connectivity));
            }
        });

        companyDialog.show();
    }

    public void loadImageBitmap(User user) {
        Picasso.get().load(user.getFaceImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                registered.put(user, bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }
}