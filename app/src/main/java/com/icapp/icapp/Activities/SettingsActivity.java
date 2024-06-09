package com.icapp.icapp.Activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icapp.icapp.BroadcastReceiver.NotificationCenter;
import com.icapp.icapp.BroadcastReceiver.NotificationType;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Interfaces.ICallback;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SettingsActivity extends SyncActivity implements ICallback {

    ImageView fully_synced;
    LinearLayout synced_counter;
    TextView txt_numError;
    ArrayList<CarReport> reports = new ArrayList<>();

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_empty);
        fully_synced = findViewById(R.id.fully_synced);
        synced_counter = findViewById(R.id.synced_counter);
        txt_numError = findViewById(R.id.txt_numError);
        updateCounter();
        //subscribe to notifications listener in onCreate of activity
        NotificationCenter.addObserver(this, NotificationType.UpdateSyncCount, updateSyncCountReceiver);
    }

    @Override
    protected void onDestroy() {
        // Don't forget to unsubscribe from notifications listener
        NotificationCenter.removeObserver(this, updateSyncCountReceiver);
        super.onDestroy();
    }

    @SuppressLint("DefaultLocale")
    public void updateCounter() {
        reports = RealM.getInstance().getArrivalList().stream().filter(report -> report.getRequire_full_sync() == 1).collect(Collectors.toCollection(ArrayList::new));
        txt_numError.setText(String.format("%d", reports.size()));
        fully_synced.setVisibility(reports.isEmpty() ? View.VISIBLE : View.GONE);
        synced_counter.setVisibility(reports.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void syncPressed(View view) {
        showToast(getString(R.string.sync_process_started));
        syncInBackground();
    }

    public void logoutPressed(View view) {
        // TODO: To check it with mena
//        removeArrival();
        removeUser();
        startActivity(new Intent(this, SplashActivity.class));
        finishAffinity();
    }

    public void logPressed(View view) {
        startActivity(new Intent(this, LogActivity.class));
    }

    public void UpdateLogPressed(View view) {
        startActivity(new Intent(this, UpdateLogActivity.class));
    }

    private final BroadcastReceiver updateSyncCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // do what you need to do with parameters that you sent with notification
            updateCounter();
        }
    };
}