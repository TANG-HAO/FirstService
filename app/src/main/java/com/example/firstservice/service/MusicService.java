package com.example.firstservice.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;
import com.example.firstservice.bean.Music;
import com.example.firstservice.listener.MusicListener;

/**
 * 创建服务，实现前台服务
 */
public class MusicService extends Service {
    private MediaPlayer musicPlayer;//音乐播放器  只创建一个
    private NotificationManager notificationManager;//之创建一个管理器
    private NotificationChannel channelbody;//声明通知频道
    private RemoteViews remoteViews;//声明通知控件
    private Music music;//声明music对象  从intent中获取music对象
    private Context mContext= MyApplication.getContext();//创建上下文

    public MusicService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer=new MediaPlayer();//创建音乐播放器  只创建一次
        getNotificationManager();//获取管理器  只创建一次

    }

    /**
     * 获取管理器
     */
    private void getNotificationManager() {
        if(notificationManager==null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    /**
     * 创建ibinder
     */
    private MusicBinder musicBinder=new MusicBinder();

    private class MusicBinder {
        //todo 方法
    }




    //todo 创建getNotification

    private MusicListener musicListener = new MusicListener() {
        @Override
        public void Play(Intent intent) {

            Notification notification =getNotification();
            notificationManager.notify(1,notification);//播放音乐之前获取通知
            //startForeground();  //todo 需不需要？？


        }

        @Override
        public void pause() {

        }
    };

    /**
     * 播放音乐之前获取通知
     */
    private Notification getNotification() {
        //判断是否为android8以上 若是则创建notificationChannel 并指定其id,name,importance
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelbody = new NotificationChannel("播放音乐","播放音乐", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channelbody);
        }
        //创建通知布局
        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_music);
        remoteViews.setImageViewBitmap(R.id.notification_album_picture,music.albumPicture);
        remoteViews.setTextViewText(R.id.notification_album_title,music.title);
        remoteViews.setTextViewText(R.id.notification_album_artist,music.artist);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        Notification notification = builder
                //.setContentTitle(mlist.get(position).title)
//                        .setContentText(mlist.get(position).artist)
//                        .setLargeIcon(mlist.get(position).albumPicture)
                .setSmallIcon(R.drawable.music_albnum_picture)
                .setTicker("开始播放啦~~")
                .setOngoing(true)//设置常驻通知栏还是可右滑动取消
                .setChannelId("播放音乐")
                .setContent(remoteViews)
                .build();
        return notification;
    }
}
