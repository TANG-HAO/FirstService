package com.example.firstservice.widget;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.activity.fragmentsActivity.adapter.MusicDynamicAdapter;
import com.example.firstservice.bean.Music;
import com.example.firstservice.utils.GetBitMapById;
import com.example.firstservice.utils.MusicOperate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.baidu.location.e.a.p;

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

    private static CircleImageView circleImageView;
    private NotificationMusicReceiver musicReceiver;

    private List<Music> musicList=new ArrayList<Music>();//音乐列表

    private MusicDynamicAdapter adapter;
    private ImageButton menu_Button;




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
        //开启底部控件广播
        musicReceiver=new NotificationMusicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicOperate.NOTIFICATION_PAUSE);
        intentFilter.addAction(MusicOperate.NOTIFICATION_PLAY);
        mContext.registerReceiver(musicReceiver,intentFilter);

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
            progressBar.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            song_title.setText(music_reciver.title);
            circleImageView.setImageBitmap(GetBitMapById.getBitMap(music_reciver.albumPictureid));
            isPlay=false;


    }

    public static void setMusicInfo(MediaPlayer musicPlayer) {

    }

    /**
     * 监听器的添加
     */
    private void addListener() {
        //stopButton.setOnClickListener(this);
        progressBar.setOnClickListener(this);
        menu_Button.setOnClickListener(this);
    }

    /**
     * 控件的实例化
     */
    private void initView() {
       // stopButton=(ImageButton)findViewById(R.id.stop_button);

        song_title=(TextView)findViewById(R.id.song_title);
        circleImageView=(CircleImageView)findViewById(R.id.album_picture_controller);
        progressBar=(SimpleRoundProgress)findViewById(R.id.music_progressbar);
        menu_Button=(ImageButton)findViewById(R.id.menu_button);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.music_progressbar:
                if(isPlay){//播放
                    progressBarState(R.drawable.ic_pause_black_24dp,MusicOperate.CONTINUE,false);

                }else{//暂停
                    progressBarState(R.drawable.ic_play_arrow_black_24dp, MusicOperate.PAUSE, true);
                }
                break;
            case R.id.menu_button:
                //展示dailog
                Log.d("dialog","点击出现dialog");
                showDynamicList(mContext,R.layout.dynamic_dialog);
                break;
        }
    }

    /**
     * 点击出现Dialog
     * @param mContext
     * @param layout_id
     */
    private void showDynamicList(Context mContext,int layout_id) {
        Log.d("dialog","点击出现dialog");
        //动态加载视图，从视图中实例化recycleView,将点击的music反转塞入，将视图set进创建的dialog
        View view = View.inflate(mContext, layout_id, null);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.musci_dynamic_recycle_view);
//        recyclerView.setLayoutManager(linearLayoutManager);

//        musicList.add(music_reciver);
//        Collections.reverse(musicList);
//        List<Music> reverseList=musicList;
//        Collections.reverse(musicList);

//        adapter = new MusicDynamicAdapter(musicList);
//        recyclerView.setAdapter(adapter);

        Dialog dialog = new Dialog(mContext, R.style.NormalDialogStyle);//设置样式
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT,ScreenUtils.getScreenHeight(mContext)/12*7);
        window.setGravity(Gravity.BOTTOM);
        //设置动画
        window.setWindowAnimations(R.style.normalDialogAnim);

        dialog.show();


    }

    private void progressBarState(int picture_id, String pause, boolean b) {
        progressBar.setBackgroundResource(picture_id);
        if (mediaPlayer_reciver != null) {
            Intent intent = new Intent(pause);
            mContext.sendBroadcast(intent);
        }
        isPlay = b;
    }

    /**
     * 设置progressBar
     */
    public void setProgressBar(){
        progressBar.getProgress();
    }

    public class NotificationMusicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case MusicOperate.NOTIFICATION_PAUSE:
                    pause();
                    break;
                case MusicOperate.NOTIFICATION_PLAY:
                    play();
                    break;
                    default :
            }
        }
    }
    /**
     * 接受通知栏发送过来的消息，播放音乐
     */
    private void play() {
        mediaPlayer_reciver.start();
        progressBar.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        isPlay=false;
    }

    /**
     * 接受通知栏发送过来的消息，暂停音乐
     */
    private void pause() {
        //音乐停止，改变ui,设置isPlay为true
       mediaPlayer_reciver.pause();
       progressBar.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
       isPlay=true;
    }


}
