package com.icapp.icapp.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.icapp.icapp.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FaceComparator {
    private CascadeClassifier cascadeClassifier;

    public FaceComparator(Context context) {
        // Initialize the face detection classifier
        // Load the classifier file
        BaseLoaderCallback baseCallBack = new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                if (status == SUCCESS) {

                    // Initialize the face detection classifier
                    cascadeClassifier = new CascadeClassifier();
                    // Load the classifier file
                    InputStream modelInputStream = context.getResources().openRawResource(R.raw.haarcascade_frontalface_default);

                    File faceDir = context.getDir("faceDir", Context.MODE_PRIVATE); // FACE_DIR should be a string constant

                    File faceModel = new File(faceDir, "faceModel"); // FACE_MODEL should be a string constant

                    try (FileOutputStream modelOutputStream = new FileOutputStream(faceModel)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead = modelInputStream.read(buffer);
                        while (bytesRead != -1) {
                            modelOutputStream.write(buffer, 0, bytesRead);
                            bytesRead = modelInputStream.read(buffer);
                        }
                    } catch (IOException e) {
                        // Handle exceptions here
                    } finally {
                        try {
                            modelInputStream.close();
                        } catch (IOException e) {
                            // Handle exceptions here
                        }
                    }
                    cascadeClassifier.load(faceModel.getAbsolutePath());

                    if (cascadeClassifier.empty()) {
                        cascadeClassifier = null;
                    } else {
                        faceDir.delete();
                    }

                    Log.d("OpenCVLoad", "OpenCV Loaded");
                } else {
                    super.onManagerConnected(status);
                }
            }
        };
        if (!OpenCVLoader.initDebug())
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, context, baseCallBack);
        else
            baseCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    public Double compareFaces(Context context, Bitmap bitmap1, Bitmap bitmap2) {
        // Convert Bitmaps to OpenCV Mat objects
        Mat mat1 = new Mat(bitmap1.getWidth(), bitmap1.getHeight(), CvType.CV_8UC1);
        Mat mat2 = new Mat(bitmap2.getWidth(), bitmap2.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(bitmap1, mat1);
        Utils.bitmapToMat(bitmap2, mat2);

        // Detect faces in both bitmaps
        MatOfRect faces1 = detectFaces(mat1);
        MatOfRect faces2 = detectFaces(mat2);

        // If no faces detected in either bitmap, return false
        if (faces1.empty() || faces2.empty()) {
            return 0.0;
        }

        // Extract features from the first detected face
        Mat face1 = extractFace(mat1, faces1.toArray()[0]);
        // Extract features from the second detected face
        Mat face2 = extractFace(mat2, faces2.toArray()[0]);

        // Compare the extracted features and determine similarity
        double similarityScore = compareFeatures(face1, face2) * 10000000;

        Log.d("simulation", String.valueOf(similarityScore));

        // You need to define your threshold based on your requirements
        double threshold = 0.5; // Example threshold value

        // If the similarity score exceeds the threshold, consider the faces as the same person
        return similarityScore; // > threshold;
    }

    private MatOfRect detectFaces(Mat image) {
        MatOfRect faces = new MatOfRect();
        cascadeClassifier.detectMultiScale(image, faces);
        return faces;
    }

    private Mat extractFace(Mat image, Rect faceRect) {
        return new Mat(image, faceRect);
    }

    private double compareFeatures(Mat face1, Mat face2) {
//        // Ensure that both feature vectors are of the same size and type
//        if (face1.rows() != face2.rows() || face1.cols() != face2.cols() || face1.type() != face2.type()) {
//            return 0.0;
//        }

        // Calculate the Euclidean distance between the two feature vectors
        double distance = 0.0;

        int minRows = Math.min(face1.rows(), face2.rows());
        int minCols = Math.min(face1.cols(), face2.cols());

        for (int i = 0; i < minRows; i++) {
            for (int j = 0; j < minCols; j++) {
                double diff = face1.get(i, j)[0] - face2.get(i, j)[0]; // Assuming single channel (grayscale) for simplicity
                distance += diff * diff;
            }
        }

        // Convert distance to a similarity score if needed
        // For now, just return the inverse of distance as a simplistic similarity measure
        // Note: You might want to normalize this value or convert it into a more interpretable score
        return 1.0 / (1.0 + distance);
    }
}