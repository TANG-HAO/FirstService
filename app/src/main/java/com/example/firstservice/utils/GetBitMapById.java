package com.example.firstservice.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;

public class GetBitMapById {
    /**
     * 根据id获取封面
     *
     * @param albumId
     * @return
     */
    public static Bitmap getBitMap(int albumId) {
        Context mContext=MyApplication.getContext();

        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = mContext.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(albumId)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        Bitmap bm = null;
        if (album_art != null) {
            bm = BitmapFactory.decodeFile(album_art);
        } else {//默认封面
            bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.music_albnum_picture);
        }
        return bm;

    }
}
