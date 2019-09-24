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
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.firstservice.R;
import com.example.firstservice.activity.MainActivity;
import com.example.firstservice.asyncTask.DownloadTask;
import com.example.firstservice.listener.DownloadListener;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private String downloadUrl;
    private static final String TAG = "DownloadService";

    private DownloadListener downloadListener=new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success",-1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask=null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
           downloadTask=null;
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask=null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Cancled", Toast.LENGTH_SHORT).show();
        }
    };
    private DownloadBinder mbinder=new DownloadBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"使用Binder传递数据=======");
        return mbinder;
    }

    public class DownloadBinder extends Binder {
        public  void startDownload(String url){
            if(downloadTask==null){
                downloadUrl=url;
                downloadTask=new DownloadTask(downloadListener);
                downloadTask.execute(downloadUrl);//传入参数
                startForeground(1,getNotification("Download...",0));
                Toast.makeText(DownloadService.this, "Download...", Toast.LENGTH_SHORT).show();
            }
        }
        public void pauseDownload(){
            if(downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }
        public void cancelDownload(){
            if(downloadTask!=null){
                downloadTask.cancleDownload();
            }
            if(downloadUrl!=null){
                String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file=new File(directory+fileName);
                if(file.exists()){
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this, "Cancled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotification(String title, int progress){
        NotificationManager notificationManager=null;
        NotificationChannel channelbody=null;
        //创建notificationManager
        if(notificationManager==null){
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
//判断是否为android8以上 若是则创建notificationChannel 并指定其id,name,importance
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelbody = new NotificationChannel("Download","下载", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channelbody);
        }
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setChannelId("Download")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                .setContentIntent(pi)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_download_black_24dp));
        if(progress>=0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }

        return  builder.build();
    }
}
