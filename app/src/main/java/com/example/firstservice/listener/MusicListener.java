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

}
