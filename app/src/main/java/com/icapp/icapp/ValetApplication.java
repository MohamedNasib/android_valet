package com.icapp.icapp;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ValetApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Realm.init(this);
        RealmConfiguration config =
                new RealmConfiguration.Builder()
                        // below line is to allow write
                        // data to database on ui thread.
                        .allowWritesOnUiThread(true)
                        // below line is to delete realm
                        // if migration is needed.
                        .deleteRealmIfMigrationNeeded()
                        // at last we are calling a method to build.
                        .build();
        // on below line we are setting
        // configuration to our realm database.
        Realm.setDefaultConfiguration(config);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle bundle) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }
}