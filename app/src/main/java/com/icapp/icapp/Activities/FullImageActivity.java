package com.icapp.icapp.Activities;

import static com.icapp.icapp.Activities.ParentActivity.setStatusBarGradiant;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.icapp.icapp.Models.CarPart;
import com.icapp.icapp.R;
import com.squareup.picasso.Picasso;

public class FullImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_full_image);
        TextView pageTitle = findViewById(R.id.lblPageTitle);
        PhotoView photoView = findViewById(R.id.photo_view);

        CarPart carPart = getIntent().getParcelableExtra("Damage");
        if (carPart != null) {
            pageTitle.setText(carPart.getName());
            if (carPart.getPath() != null) {
                if (carPart.getPath().contains("https")) {
                    Picasso.get().load(carPart.getPath()).into(photoView);
                } else {
                    photoView.setImageBitmap(BitmapFactory.decodeFile(carPart.getPath()));
                }
            }
        }
    }
}