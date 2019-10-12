package com.example.firstservice.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;
import com.example.firstservice.bean.Music;
import com.example.firstservice.listener.MusicListener;
import com.example.firstservice.utils.GetBitMapById;
import com.example.firstservice.widget.MusicControllerBar;

import java.io.IOException;

/**
 * 创建服务，实现前台服务
 */
public class MusicService extends Service {
    private final  String MUSIC="music";
    private final String PLAY ="play";
    private final String PAUSE="pause";
    private MediaPlayer musicPlayer;//音乐播放器  只创建一个
    private NotificationManager notificationManager;//之创建一个管理器
    private NotificationChannel channelbody;//声明通知频道
    private RemoteViews remoteViews;//声明通知控件
    private Music music;//声明music对象  从intent中获取music对象
    private Context mContext= MyApplication.getContext();//创建上下文
    private MusicReceiver mr;
    private NotificationCompat.Builder builder;







    /**
     * 获取管理器
     */
    private void getNotificationManager() {
        if(notificationManager==null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer=new MediaPlayer();//创建音乐播放器  只创建一次
        getNotificationManager();//获取管理器  只创建一次

        // 动态注册receiver
        mr = new MusicReceiver();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(PLAY);
        registerReceiver(mr, intentfilter);

    }




    //todo 创建getNotification

    private MusicListener musicListener = new MusicListener() {
        @Override
        public void Play(Intent intent) {
            music= (Music)intent.getParcelableExtra(MUSIC);


            try {
                musicPlayer.reset();
                musicPlayer.setDataSource(music.data);
                musicPlayer.prepare();
                musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            MusicControllerBar.setMusicInfo(musicPlayer, music);//传入自定义控件所需的参数
            builder=getNotificationBuilder();
            if(remoteViews!=null){
                remoteViews.setImageViewResource(R.id.notification_play_pause_album,R.drawable.ic_stop_black_24dp);
            }
            Notification notification = builder.setContent(remoteViews).build();
            notificationManager.notify(1,notification);//播放音乐之前获取通知
            //startForeground();  //todo 需不需要？？

        }

        @Override
        public void pause() {
            if(musicPlayer!=null){
                musicPlayer.pause();
            }
            if(remoteViews!=null){
                remoteViews.setImageViewResource(R.id.notification_play_pause_album,R.drawable.ic_play_arrow_black_24dp);
            }
            Notification notification = builder.setContent(remoteViews).build();
            notificationManager.notify(1,notification);//播放音乐之前获取通知

        }
    };

    /**
     * 播放音乐之前获取通知Builder
     */
    private NotificationCompat.Builder getNotificationBuilder() {
        //判断是否为android8以上 若是则创建notificationChannel 并指定其id,name,importance
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelbody = new NotificationChannel("播放音乐","播放音乐", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channelbody);
        }
        //创建通知布局
        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_music);
        remoteViews.setImageViewBitmap(R.id.notification_album_picture, GetBitMapById.getBitMap(music.albumPictureid));
        remoteViews.setTextViewText(R.id.notification_album_title,music.title);
        remoteViews.setTextViewText(R.id.notification_album_artist,music.artist);


        remoteViews.setImageViewResource(R.id.notification_play_pause_album,R.drawable.ic_stop_black_24dp);


        builder = new NotificationCompat.Builder(mContext);
        builder
                //.setContentTitle(mlist.get(position).title)
//                        .setContentText(mlist.get(position).artist)
//                        .setLargeIcon(mlist.get(position).albumPicture)
                .setSmallIcon(R.drawable.music_albnum_picture)
                .setTicker("开始播放啦~~")
                .setOngoing(true)//设置常驻通知栏还是可右滑动取消
                .setChannelId("播放音乐")
                .setContent(remoteViews);

        return builder;
    }

    /**
     * 广播接受者
     */
    public class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {//inten携带music对象
            String action = intent.getAction();

            switch (action){
                case PLAY://播放
                    //todo 调用监听器的播放方法
                    musicListener.Play(intent);
                    break;
                case PAUSE://暂停
                    musicListener.pause();
                    break;
            }
        }
    }



}
