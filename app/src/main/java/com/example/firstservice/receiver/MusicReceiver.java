package com.example.firstservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.firstservice.listener.MusicListener;

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {//inten携带music对象
        String action = intent.getAction();
        switch (action){
            case "pause"://暂停
                //todo 调用监听器的播放方法

                break;
        }
    }
}
