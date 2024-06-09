package com.icapp.icapp.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDFDownloaderTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = "PDFDownloaderTask";

    private DownloadCallback callback;

    public PDFDownloaderTask(DownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String pdfUrl = params[0];
        String destinationPath = params[1];

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(pdfUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // Make sure the server response code is in the 200 range
            if (connection.getResponseCode() / 100 != 2) {
                return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
            }

            int fileLength = connection.getContentLength();
            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(destinationPath);

            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                if (fileLength > 0) {
                    int progress = (int) (totalBytesRead * 100 / fileLength);
                    publishProgress(progress);
                }
            }

            return null; // No errors, download successful
        } catch (IOException e) {
            Log.e(TAG, "Error downloading PDF", e);
            return e.getMessage();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error closing resources", e);
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        callback.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) {
            callback.onDownloadComplete();
        } else {
            callback.onDownloadError(result);
        }
    }

    public interface DownloadCallback {
        void onProgressUpdate(int progress);

        void onDownloadComplete();

        void onDownloadError(String errorMessage);
    }
}