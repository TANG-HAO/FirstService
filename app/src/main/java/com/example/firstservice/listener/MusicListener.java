package com.example.firstservice.listener;

import android.content.Intent;

public interface MusicListener {
    /**
     * 音乐播放
     */
    void Play(Intent intent);
    /**
     * 音乐暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void continue1();

    /**
     * 判断翻转
     */
    void convert();
}
