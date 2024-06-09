package com.icapp.icapp.Helpers;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.icapp.icapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class PdfUtil {

    public static void createPdf(Context context , int pageWidth , int pageHeight) {
        // Create a PdfDocument
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight + 100, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.activity_new_case_report, null);
        content.measure(View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY));
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        // Draw the content to the PDF page
        content.draw(page.getCanvas());

        // Finish the page
        document.finishPage(page);

        // Save the PDF file
        savePdf(document);

        // Close the document
        document.close();
    }

    private static void savePdf(PdfDocument document) {
        // Get the directory for saving PDFs
        File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "IC App");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        // Create a unique file name
        String fileName = "Car Report"+System.currentTimeMillis() +".pdf";
        File pdfFile = new File(pdfDir, fileName);

        try {
            // Write the document content to the file
            document.writeTo(new FileOutputStream(pdfFile));
        } catch (IOException e) {
            Log.d("TAG", Objects.requireNonNull(e.getMessage()));
        }
    }
}

