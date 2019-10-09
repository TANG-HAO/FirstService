package com.example.firstservice.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.firstservice.R;
import com.example.firstservice.bean.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicRelativeLayout extends RelativeLayout implements View.OnClickListener {
    private ImageButton stopButton;
    private View view;
    private ContentResolver resolver;
    private List<Music> musics;

    public MusicRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.music_layout, this);
        //requestPermission();

        //实例化控件
        initView();
        //初始化媒体播放器
        initMediaPlayer();
        //添加监听事件
        addListener();
    }

    /**
     * 初始化媒体播放器
     */

    private void initMediaPlayer() {
        //getMusicInfo();//获取音乐信息

    }




    /**
     * 监听器的添加
     */
    private void addListener() {
        stopButton.setOnClickListener(this);
    }

    /**
     * 控件的实例化
     */
    private void initView() {
        stopButton=(ImageButton)findViewById(R.id.stop_button);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stop_button:
                Toast.makeText(getContext(), "asdjsadhjs", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
