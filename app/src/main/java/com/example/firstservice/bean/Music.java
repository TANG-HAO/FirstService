package com.example.firstservice.bean;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public  class Music implements Parcelable {
    public long id;
    public String title;//音乐名称
    public String data;//音乐文件路径
    public String album;//专辑
    public String artist;//艺人
    public int duration;//音乐时长
    public long size;//音乐文件大小
    //public Bitmap albumPicture;//歌曲封面
    public int albumPictureid;//歌曲封面id

    public Music() {
    }


    protected Music(Parcel in) {
        id = in.readLong();
        title = in.readString();
        data = in.readString();
        album = in.readString();
        artist = in.readString();
        duration = in.readInt();
        size = in.readLong();
        albumPictureid = in.readInt();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(data);
        parcel.writeString(album);
        parcel.writeString(artist);
        parcel.writeInt(duration);
        parcel.writeLong(size);
        parcel.writeInt(albumPictureid);
    }
}
