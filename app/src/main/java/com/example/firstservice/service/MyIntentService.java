package com.example.firstservice.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    public MyIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

                Log.d(TAG,"Thread id is"+Thread.currentThread().getId());

    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"IntentService======destory");
    }
}
