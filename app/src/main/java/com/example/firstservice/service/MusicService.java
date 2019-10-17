package com.example.firstservice.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;
import com.example.firstservice.bean.Music;
import com.example.firstservice.listener.MusicListener;
import com.example.firstservice.utils.GetBitMapById;
import com.example.firstservice.utils.MusicOperate;
import com.example.firstservice.widget.MusicControllerBar;
import com.example.firstservice.widget.RollingTextView;

import java.io.IOException;

/**
 * 创建服务，实现前台服务
 */
public class MusicService extends Service {
    private final String MUSIC = "music";
    private final String PLAY = "play";
    private final String PAUSE = "pause";
    private MediaPlayer musicPlayer;//音乐播放器  只创建一个
    private NotificationManager notificationManager;//之创建一个管理器
    private NotificationChannel channelbody;//声明通知频道
    private RemoteViews remoteViews;//声明通知控件
    private Music music;//声明music对象  从intent中获取music对象
    private Context mContext = MyApplication.getContext();//创建上下文
    private MusicReceiver mr;
    private NotificationCompat.Builder builder;
    private boolean isPlay = true;
    private static final String TAG = "MusicService";


    /**
     * 获取管理器
     */
    private void getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
        musicPlayer = new MediaPlayer();//创建音乐播放器  只创建一次
        getNotificationManager();//获取管理器  只创建一次

        // 动态注册receiver
        mr = new MusicReceiver();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(PLAY);
        intentfilter.addAction(MusicOperate.PAUSE);
        intentfilter.addAction(MusicOperate.CONTINUE);
        intentfilter.addAction(MusicOperate.isPlay);
        intentfilter.addAction(MusicOperate.STOP);
        registerReceiver(mr, intentfilter);

    }


    //todo 创建getNotification

    private MusicListener musicListener = new MusicListener() {
        /**
         * 监听器监听音乐开始
         * @param intent
         */
        @Override
        public void Play(Intent intent) {
            music = (Music) intent.getParcelableExtra(MUSIC);

            Intent show_intent = new Intent(MusicOperate.SHOW_BOTTOM);
            mContext.sendBroadcast(show_intent);


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
            builder = getNotificationBuilder();
            if (remoteViews != null) {
                remoteViews.setImageViewResource(R.id.notification_play_pause_album, R.drawable.ic_pause_black_24dp);
            }
            Notification notification = builder.setContent(remoteViews).build();
            notificationManager.notify(1, notification);//播放音乐之前获取通知


        }

        /**
         * 监听器监听音乐暂停
         */
        @Override
        public void pause() {
            if (musicPlayer != null) {
                musicPlayer.pause();
            }
            setNotification(R.drawable.ic_play_arrow_black_24dp);
            isPlay = false;
        }

        /**
         * 监听器监听音乐继续
         */
        @Override
        public void continue1() {
            if (musicPlayer != null) {
                musicPlayer.start();
            }
            setNotification(R.drawable.ic_pause_black_24dp);
            isPlay = true;
        }

        /**
         * 判断翻转
         */
        @Override
        public void convert() {
            //设置ui,改变isPaly,发送广播
            if (isPlay) {//点击暂停音乐
                Log.d("isPlay", "isPlay=true,暂停");
                remoteViews.setImageViewResource(R.id.notification_play_pause_album, R.drawable.ic_play_arrow_black_24dp);
                Intent intent = new Intent(MusicOperate.NOTIFICATION_PAUSE);
                mContext.sendBroadcast(intent);
            } else {//点击播放音乐
                Log.d("isPlay", "isPlay=false，播放");
                remoteViews.setImageViewResource(R.id.notification_play_pause_album, R.drawable.ic_pause_black_24dp);
                Intent intent = new Intent(MusicOperate.NOTIFICATION_PLAY);
                mContext.sendBroadcast(intent);
            }
            isPlay = !isPlay;
            Log.d("isPlay", isPlay + "==");
            Notification notification = builder.setContent(remoteViews).build();
            notificationManager.notify(1, notification);//播放音乐之前获取通知

        }

        /**
         * 歌曲清除,通知消失
         */
        @Override
        public void stop() {
            musicPlayer.stop();
            notificationManager.cancel(1);
        }
    };

    /**
     * 设置通知栏播放暂停图片切换
     *
     * @param picture_id
     */
    private void setNotification(int picture_id) {
        if (builder == null) {
            builder = getNotificationBuilder();
        }
        if (remoteViews != null) {
            remoteViews.setImageViewResource(R.id.notification_play_pause_album, picture_id);
        }
        Notification notification = builder.setContent(remoteViews).build();
        notificationManager.notify(1, notification);//播放音乐之前获取通知
    }

    /**
     * 播放音乐之前获取通知Builder
     */
    private NotificationCompat.Builder getNotificationBuilder() {
        //判断是否为android8以上 若是则创建notificationChannel 并指定其id,name,importance
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelbody = new NotificationChannel("播放音乐", "播放音乐", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channelbody);
        }
        //创建通知布局
        setRemoteViews();
        //设置点击发送广播事件
        setOnClickBroadCast();

        builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.drawable.music_albnum_picture)
                .setTicker("开始播放啦~~")
                .setOngoing(true)//设置常驻通知栏还是可右滑动取消
                .setChannelId("播放音乐")
                .setContent(remoteViews);
        return builder;
    }

    /**
     * 设置通知点击发送广播
     */
    private void setOnClickBroadCast() {
        //暂停或者继续
        Intent intent = new Intent(MusicOperate.isPlay);
        intent.putExtra("isPlay", isPlay);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notification_play_pause_album, pendingIntent);
        //点击下一首歌
        Intent next_intent = new Intent(MusicOperate.NEXT_MUSIC);
        next_intent.putExtra(MusicOperate.NEXT_MUSIC , music);
        PendingIntent next_pendingIntent = PendingIntent.getBroadcast(mContext, 0, next_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.next_music, next_pendingIntent);

        //点击上一首歌
        Intent previous_intent = new Intent(MusicOperate.PREVIOUS_MUSIC);
        previous_intent.putExtra(MusicOperate.PREVIOUS_MUSIC , music);
        PendingIntent pre_pendingIntent = PendingIntent.getBroadcast(mContext, 0, previous_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.previous_music,pre_pendingIntent);


    }

    /**
     * 通知布局的初始化
     * @return
     */
    private void  setRemoteViews() {
        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_music);
        remoteViews.setImageViewBitmap(R.id.notification_album_picture, GetBitMapById.getBitMap(music.albumPictureid));
        remoteViews.setTextViewText(R.id.notification_album_title, music.title);
        remoteViews.setTextViewText(R.id.notification_album_artist, music.artist);
        remoteViews.setImageViewResource(R.id.notification_play_pause_album, R.drawable.ic_pause_black_24dp);

    }

    /**
     * 广播接受者
     */
    public class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {//inten携带music对象
            String action = intent.getAction();

            switch (action) {
                case PLAY://播放
                    //todo 调用监听器的播放方法
                    musicListener.Play(intent);
                    break;
                case PAUSE://暂停
                    musicListener.pause();
                    break;
                case MusicOperate.CONTINUE:
                    musicListener.continue1();
                    break;
                case MusicOperate.isPlay:
                    Log.d(TAG, "onReceive: 进入convert()");
                    musicListener.convert();
                    break;
                case MusicOperate.STOP:
                    Log.d(TAG,"接收到清除广播");
                    musicListener.stop();
                    break;
                default:
            }

        }
    }

    @Override
    public void onDestroy() {
        if(musicPlayer.isPlaying()){
            musicPlayer.stop();
        }
        musicPlayer.release();
        super.onDestroy();
    }
}
