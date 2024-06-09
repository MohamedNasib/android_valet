package com.icapp.icapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.icapp.icapp.Helpers.SharedPrefs;
import com.icapp.icapp.Interfaces.ICallback;
import com.icapp.icapp.Models.Output.Login.UserData;
import com.icapp.icapp.R;
import com.icapp.icapp.Retrofit.Retrofit;
import com.rollbar.android.Rollbar;

import retrofit2.Response;

public class ParentActivity extends AppCompatActivity implements ICallback {
    SharedPrefs sharedPrefs;
    Retrofit retrofit;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = new SharedPrefs(this);
        retrofit = new Retrofit(this, true);
        Rollbar.init(this);
    }

    public static void setStatusBarGradiant(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getColor(android.R.color.transparent));
        window.setNavigationBarColor(activity.getColor(android.R.color.transparent));
        window.setBackgroundDrawableResource(R.drawable.gradient_view);
    }

    public void handleLoginResponse(UserData response) {
        sharedPrefs.set(R.string.USER_ID_KEY , String.valueOf(response.getData().getId()));
        sharedPrefs.set(R.string.FIRST_NAME_KEY, response.getData().getFirst_name());
        sharedPrefs.set(R.string.LAST_NAME_KEY, response.getData().getLast_name());
        sharedPrefs.set(R.string.USER_IMAGE_KEY, response.getData().getFace_image());
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }

    @Override
    public void onSuccess(Response response, String msg) {
        if (response.code() == 200) {
            if (!msg.isEmpty()) {
                showToast(msg);
            }
        }
    }

    @Override
    public void onSuccessFail(String msg) {
        if (!msg.isEmpty()) {
            showToast(msg);
        }
    }

    @Override
    public void onFail(String msg) {
        if (!msg.isEmpty()) {
            showToast(msg);
        }
    }

    public void backPressed(View view) {
        finish();
    }

    public View findViewWithTagRecursively(ViewGroup root, String tag) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);

            if (tag.equals(child.getTag())) {
                return child;
            }

            if (child instanceof ViewGroup) {
                View foundView = findViewWithTagRecursively((ViewGroup) child, tag);
                if (foundView != null) {
                    return foundView;
                }
            }
        }

        return null;
    }

    public Boolean isConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.isAvailable();
        } catch (Exception e) {
            return false;
        }
    }

    public void removeArrival() {
        sharedPrefs.remove(R.string.REPORT_CAR_PLATE_KEY);
        sharedPrefs.remove(R.string.REPORT_TICKET_NUMBER_KEY);

        sharedPrefs.remove(R.string.FRONT_IMAGE_KEY);
        sharedPrefs.remove(R.string.FRONT_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.FRONT_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.BACK_IMAGE_KEY);
        sharedPrefs.remove(R.string.BACK_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.BACK_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.RIGHT_IMAGE_KEY);
        sharedPrefs.remove(R.string.RIGHT_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.RIGHT_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.LEFT_IMAGE_KEY);
        sharedPrefs.remove(R.string.LEFT_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.LEFT_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.ANOTHER_FRONT_IMAGE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_FRONT_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_FRONT_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.ANOTHER_BACK_IMAGE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_BACK_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_BACK_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.ANOTHER_RIGHT_IMAGE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_RIGHT_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_RIGHT_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.ANOTHER_LEFT_IMAGE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_LEFT_IMAGE_DATE_KEY);
        sharedPrefs.remove(R.string.ANOTHER_LEFT_IMAGE_DAMAGES_KEY);

        sharedPrefs.remove(R.string.DAMAGES_KEY);
        sharedPrefs.remove(R.string.ADDITIONAL_KEY);
        sharedPrefs.remove(R.string.UPDATE_DAMAGES_KEY);
    }

    public void removeUser() {
        sharedPrefs.remove(R.string.USER_ID_KEY);
        sharedPrefs.remove(R.string.FIRST_NAME_KEY);
        sharedPrefs.remove(R.string.LAST_NAME_KEY);
        sharedPrefs.remove(R.string.USER_IMAGE_KEY);
    }

    public static Bitmap decodeSampledBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void showToast(String msg)
    {
        if(toast!=null) toast.cancel(); // Avoid null pointer exceptions!
        toast = Toast.makeText(this,msg,Toast.LENGTH_LONG);
        toast.show();
    }
}