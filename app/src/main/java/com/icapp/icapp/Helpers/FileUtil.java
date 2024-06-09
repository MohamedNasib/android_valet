package com.icapp.icapp.Helpers;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileUtil {

    public static File convertBitmapToFile(Context context, Bitmap bitmap) {
        // Create a file in the cache directory (you can choose another directory based on your needs)
        File file = new File(context.getCacheDir(), "temp_image.jpg");

        try {
            // Compress the bitmap to a file using JPEG format
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return file;
        } catch (IOException e) {
            Log.d("TAG", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}