package com.example.myapplication;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.BatchActivityLifecycleHelper;
import com.batch.android.Config;

import io.branch.referral.Branch;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Branch logging for debugging
        Branch.enableLogging();
        // Branch object initialization
        Branch.getAutoInstance(this);

        Batch.setConfig(new Config("<your apikey")); // live
        // [... misc batch config here ...]
        registerActivityLifecycleCallbacks(new BatchActivityLifecycleHelper());

        Batch.Push.setNotificationInterceptor(new BranchNotificationInterceptor());
    }
}
