package com.example.firstservice.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Music implements Serializable {
    public long id;
    public String title;//音乐名称
    public String data;//音乐文件路径
    public String album;//专辑
    public String artist;//艺人
    public int duration;//音乐时长
    public long size;//音乐文件大小
    public Bitmap albumPicture;//歌曲封面
}
