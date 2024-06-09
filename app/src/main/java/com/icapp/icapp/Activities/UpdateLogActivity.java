package com.icapp.icapp.Activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.icapp.icapp.Adapters.LogAdapter;
import com.icapp.icapp.Adapters.UpdateLogAdapter;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.Models.CarUpdate;
import com.icapp.icapp.R;

import java.util.ArrayList;

public class UpdateLogActivity extends ParentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_update_log);
        RecyclerView rvCars = findViewById(R.id.rvCars);

        ArrayList<CarUpdate> carUpdates = RealM.getInstance().getUpdateList();

        UpdateLogAdapter adapter = new UpdateLogAdapter(this, carUpdates);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rvCars.setLayoutManager(lm);
        rvCars.setAdapter(adapter);
    }
}