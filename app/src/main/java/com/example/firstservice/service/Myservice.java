package com.example.firstservice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.firstservice.R;
import com.example.firstservice.activity.MainActivity;


public class Myservice extends Service {

    public static final String TAG=Myservice.class.getName();
    private DownloadBinder mbinder=new DownloadBinder();
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        Log.d(TAG,"MySerVice====Create");
        Intent intent = new Intent(this, MainActivity.class);

        NotificationChannel channelbody = null;
        if (notificationManager == null) {
            notificationManager =  (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelbody = new NotificationChannel("weather_channel","消息推送", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channelbody);
        }
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);//创建PendingIntent对象
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("天气")
                .setContentText("晴")

                 .setSmallIcon(R.mipmap.weather)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .setChannelId("weather_channel")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.weather))
                .build();
        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"MySerVice====start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"MySerVice====Destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    public class DownloadBinder extends Binder {
        public void startDownload(){
            Log.d(TAG,"startDownload excuted");
        }
        public int getProgress(){
            Log.d(TAG,"getProgress excuted");
            return 0;
        }
    }
}
