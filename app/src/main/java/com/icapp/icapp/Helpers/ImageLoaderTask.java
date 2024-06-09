package com.icapp.icapp.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

    private OnImageLoadListener listener;

    public ImageLoaderTask(OnImageLoadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imageUrl = params[0];
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            Log.d("TAG", Objects.requireNonNull(e.getMessage()));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            listener.onImageLoaded(result);
        }
    }

    public interface OnImageLoadListener {
        void onImageLoaded(Bitmap bitmap);
    }
}