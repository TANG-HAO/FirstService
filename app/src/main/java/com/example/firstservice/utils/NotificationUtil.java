package com.example.firstservice.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.firstservice.R;

//import static com.baidu.location.e.k.T;

public class NotificationUtil {

    private  String CALENDAR_ID ;
    private  boolean isSetPendingIntent=false;
    private  Class c;


    /**
     * 设置频道id
     * @param calendarId
     */
    public  void setCalendarId(String calendarId) {
        CALENDAR_ID = calendarId;
    }

    /**
     * 设置是否开启通知意图
     * @param setPendingIntent
     */
    public  void setSetPendingIntent(boolean setPendingIntent) {
        isSetPendingIntent = setPendingIntent;
    }

    /**
     * 获取通知管理器
     * @param context
     * @return
     */
    public  NotificationManager getNotificationManager(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);//设置通知的重要级别默认
        return  notificationManager;
    }

    /**
     * 设置通知
     * @param context
     * @param notificationManager
     * @param contentTitle
     * @param contentText
     * @param notificationPicture
     * @param smallIcon
     * @return
     */
    public  Notification createNotification(Context context, NotificationManager notificationManager, String contentTitle, String contentText, Bitmap notificationPicture, int smallIcon) {
        Notification notification;
        NotificationCompat.Builder builder;
        //android8以上通知频道设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CALENDAR_ID, "123",
                    NotificationManager.IMPORTANCE_DEFAULT);
            // 设置渠道描述
            notificationChannel.setDescription("测试通知组");
            // 是否绕过请勿打扰模式
            notificationChannel.canBypassDnd();
            // 设置绕过请勿打扰模式
            notificationChannel.setBypassDnd(true);
            // 桌面Launcher的消息角标
            notificationChannel.canShowBadge();
            // 设置显示桌面Launcher的消息角标
            notificationChannel.setShowBadge(true);
            // 设置通知出现时声音，默认通知是有声音的
            notificationChannel.setSound(null, null);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400,
                    300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
        builder = new NotificationCompat.Builder(context, CALENDAR_ID);
        if(isSetPendingIntent){
            PendingIntent p = PendingIntent.getActivity(context, 0, new Intent(context, c), 0);
            builder.setContentIntent(p);
        }

        notification = builder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setLargeIcon(notificationPicture)
                .setSmallIcon(smallIcon)
                .setChannelId(CALENDAR_ID)
                .build();
        return notification;
    }



}
