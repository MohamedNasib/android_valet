package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.CAR_REPORT_KEY;

import android.os.Bundle;
import android.os.Environment;

import com.icapp.icapp.Helpers.PDFDownloaderTask;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;
import com.pdfview.PDFView;

import java.io.File;

public class PDFViewerActivity extends ParentActivity implements PDFDownloaderTask.DownloadCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_pdfviewer);
        displayPDF();
    }

    private void displayPDF() {
        PDFView pdfView = findViewById(R.id.pdfView);
        CarReport carReport = getIntent().getParcelableExtra(CAR_REPORT_KEY);
        if (carReport != null) {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + carReport.getId() + ".pdf");
            if (file.exists()) {
                pdfView.fromFile(file).show();
            } else {
                new PDFDownloaderTask(this).execute(carReport.getPdf(), getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + carReport.getId() + ".pdf");
            }
        }
    }

    @Override
    public void onProgressUpdate(int progress) {

    }

    @Override
    public void onDownloadComplete() {
        displayPDF();
    }

    @Override
    public void onDownloadError(String errorMessage) {

    }
}