package com.icapp.icapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.icapp.icapp.R;

public class FindVehicleActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_find_vehicle);
    }

    public void scanTicketNumberPressed(View view) {
        startActivity(new Intent(this, TicketScannerActivity.class).putExtra("isDeparture", true));
    }

    public void searchCarFleetPressed(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }
}