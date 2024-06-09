package com.icapp.icapp.Helpers.Scanner;

public interface ScannerListener {
    void onDetected(String detections);
    void onStateChanged(String state, int i);
}
