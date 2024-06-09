package com.icapp.icapp.Helpers;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageUploadHelper {

    public static MultipartBody.Part bitmapToMultipartBodyPart(Bitmap bitmap) {
        // Convert Bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();

        // Create a RequestBody from the byte array
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), bitmapData);

        // Create a MultipartBody.Part from the RequestBody
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody);

        return imagePart;
    }
}