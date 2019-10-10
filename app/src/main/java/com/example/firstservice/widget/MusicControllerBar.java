package com.example.firstservice.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.firstservice.R;
import com.example.firstservice.bean.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicControllerBar extends RelativeLayout implements View.OnClickListener {
    private static  Context mContext;
    private static ImageButton stopButton;
    private View view;
    private ContentResolver resolver;
    private List<Music> musics;
    private static TextView song_title;//歌曲名称


    private static boolean isPlay;
    private static Music music_reciver;
    private static  MediaPlayer mediaPlayer_reciver;

    private int currentPosition;//暂停时目前的位置

    private static SimpleRoundProgress progressBar;
    private static Handler handler;


    public MusicControllerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        view = LayoutInflater.from(context).inflate(R.layout.music_layout, this);
        //实例化控件
        initView();
        //添加监听事件
        addListener();
        //主线程中更新ui
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==0x11){
                    progressBar.setProgress(mediaPlayer_reciver.getCurrentPosition());
                }
            }
        };

    }

    /**
     * 设置媒体播放器
     * 并更新ui界面
     * @param mediaPlayer
     * @param music
     */
    public static void setMusicInfo(final MediaPlayer mediaPlayer, Music music) {
        mediaPlayer_reciver=mediaPlayer;
        music_reciver=music;
        Log.d("hsdjkas",106031+"");
        System.out.println(mediaPlayer.getDuration());
        progressBar.setMax(mediaPlayer.getDuration());
        setMusicControllerstate();//设置初始状态
        //创建子线程
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x11);
            }
        },0,1000);

    }

    /**
     * 设置初始状态
     */
    public static  void setMusicControllerstate(){
            stopButton.setBackgroundResource(R.drawable.ic_stop_black_24dp);
            progressBar.setBackgroundResource(R.drawable.ic_stop_black_24dp);
            song_title.setText(music_reciver.title);
            isPlay=false;


    }

    /**
     * 监听器的添加
     */
    private void addListener() {
        stopButton.setOnClickListener(this);
        progressBar.setOnClickListener(this);
    }

    /**
     * 控件的实例化
     */
    private void initView() {
        stopButton=(ImageButton)findViewById(R.id.stop_button);
        song_title=(TextView)findViewById(R.id.song_title);
        progressBar=(SimpleRoundProgress)findViewById(R.id.music_progressbar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.stop_button:
                if(isPlay){//播放
                    stopButton.setBackgroundResource(R.drawable.ic_stop_black_24dp);
                    if(mediaPlayer_reciver!=null){
                        mediaPlayer_reciver.start();
                    }
                    isPlay=false;

                }else{//暂停
                    stopButton.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);//点击暂停切换成开始图片
                    if(mediaPlayer_reciver!=null){
                        mediaPlayer_reciver.pause();//音乐暂停
                        currentPosition=mediaPlayer_reciver.getCurrentPosition();
                    }
                    isPlay=true;
                }
                break;
            case R.id.music_progressbar:
                if(isPlay){//播放
                    progressBar.setBackgroundResource(R.drawable.ic_stop_black_24dp);
                    if(mediaPlayer_reciver!=null){
                        mediaPlayer_reciver.start();
                    }
                    isPlay=false;

                }else{//暂停
                    progressBar.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    if(mediaPlayer_reciver!=null){
                        mediaPlayer_reciver.pause();//音乐暂停
                        currentPosition=mediaPlayer_reciver.getCurrentPosition();
                    }
                    isPlay=true;
                }
                break;
        }
    }

    /**
     * 设置progressBar
     */
    public void setProgressBar(){
        progressBar.getProgress();
    }

}
