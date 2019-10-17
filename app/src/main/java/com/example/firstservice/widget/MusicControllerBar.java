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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.activity.fragmentsActivity.adapter.MusicDynamicAdapter;
import com.example.firstservice.bean.Music;
import com.example.firstservice.utils.GetBitMapById;
import com.example.firstservice.utils.MusicOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.baidu.location.e.a.p;

public class MusicControllerBar extends RelativeLayout implements View.OnClickListener {

    private static Context mContext;
    private static ImageButton stopButton;
    private View view;
    private ContentResolver resolver;
    private List<Music> musics;
    private static TextView song_title;//歌曲名称


    private static boolean isPlay;
    private static Music music_reciver;
    private static MediaPlayer mediaPlayer_reciver;

    private int currentPosition;//暂停时目前的位置

    private static SimpleRoundProgress progressBar;
    private static Handler handler;

    private static CircleImageView circleImageView;
    private NotificationMusicReceiver musicReceiver;

    private static List<Music> musicList = new ArrayList<Music>();//音乐列表

    private MusicDynamicAdapter adapter;
    private ImageButton menu_Button;

    private static List<Music> reverseList;
    private ImageView deleteAllImage;//清空
    private static TextView musicCount;
    private ImageView deleteOne;
    private static final String TAG = "MusicControllerBar";
    private Dialog dialog;


    public MusicControllerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.music_layout, this);
        //实例化控件
        initView();
        //添加监听事件
        addListener();
        //主线程中更新ui
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x11) {
                    progressBar.setProgress(mediaPlayer_reciver.getCurrentPosition());
                }
            }
        };
        //开启底部控件广播
        musicReceiver = new NotificationMusicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicOperate.NOTIFICATION_PAUSE);
        intentFilter.addAction(MusicOperate.NOTIFICATION_PLAY);
        intentFilter.addAction(MusicOperate.MUSIC_COUNT);
        intentFilter.addAction(MusicOperate.NEXT_MUSIC);
        intentFilter.addAction(MusicOperate.PREVIOUS_MUSIC);
        intentFilter.addAction(MusicOperate.SHOW_BOTTOM);
        intentFilter.addAction(MusicOperate.PLAY);
        intentFilter.addAction(MusicOperate.DISSHOW_BOTTOM);
        mContext.registerReceiver(musicReceiver, intentFilter);

    }

    /**
     * 设置媒体播放器
     * 并更新ui界面
     *
     * @param mediaPlayer
     * @param music
     */
    public static void setMusicInfo(final MediaPlayer mediaPlayer, Music music) {
        Log.d(TAG,"调用静态犯法传入媒体播放器和music对象");
        mediaPlayer_reciver = mediaPlayer;
        music_reciver = music;

        progressBar.setMax(mediaPlayer.getDuration());
        setMusicControllerstate();//设置初始状态

        //添加点击的音乐到list
        if (music_reciver != null && isContains(music)) {
            musicList.add(music);
            reverseList = musicList;
            Collections.reverse(reverseList);
        }



        //创建子线程
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x11);
            }
        }, 0, 1000);

    }

    /**
     * 判断是否存在已有的music
     *
     * @param music
     * @return
     */
    private static boolean isContains(Music music) {
        boolean flag = true;
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).id == music.id) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 设置初始状态
     */
    public static void setMusicControllerstate() {
        progressBar.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        song_title.setText(music_reciver.title);
        circleImageView.setImageBitmap(GetBitMapById.getBitMap(music_reciver.albumPictureid));
        isPlay = false;
    }

    public static void setMusicList(List<Music> musicList1) {
        musicCount.setText(musicList1.size() + "");
        musicList = musicList1;

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
        song_title = (TextView) findViewById(R.id.song_title);
        circleImageView = (CircleImageView) findViewById(R.id.album_picture_controller);
        progressBar = (SimpleRoundProgress) findViewById(R.id.music_progressbar);
        menu_Button = (ImageButton) findViewById(R.id.menu_button);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.music_progressbar:

                if (isPlay) {//播放
                    progressBarState(R.drawable.ic_pause_black_24dp, MusicOperate.CONTINUE, false);

                } else {//暂停
                    progressBarState(R.drawable.ic_play_arrow_black_24dp, MusicOperate.PAUSE, true);
                }
                break;
            case R.id.menu_button:
                //展示dailog
                Log.d(TAG, "onClick: 点击出现dialog");
                showDynamicList(mContext, R.layout.dynamic_dialog);
                break;
        }
    }

    /**
     * 点击出现Dialog
     *
     * @param mContext
     * @param layout_id
     */
    private void showDynamicList(Context mContext, int layout_id) {
        Log.d(TAG, "showDynamicList: 显示音乐列表");
        //动态加载视图，从视图中实例化recycleView,将点击的music反转塞入，将视图set进创建的dialog
        View view = View.inflate(mContext, layout_id, null);



        //reverseList = musicList;
        //Collections.reverse(reverseList);
        //实例化dialog控件
        initDynamicView(view);
        deleteAllImage.setOnClickListener(new onClick());




        musicCount.setText((reverseList == null ? 0 : reverseList.size())+"");//显示musicList的数据


        //设置样式 在styles中添加style
        dialog = new Dialog(mContext, R.style.NormalDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, ScreenUtils.getScreenHeight(mContext) / 12 * 7);//设置弹出框高宽
        window.setGravity(Gravity.BOTTOM);
        //设置动画
        window.setWindowAnimations(R.style.normalDialogAnim);
        //设置触摸外边界消失
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.musci_dynamic_recycle_view);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new MusicDynamicAdapter(reverseList,mContext);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();




    }

    /**
     * diaolog布局响应
     */
    private class onClick implements OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.music_delete_all:
                    Log.d(TAG, "onClick: 点击清空列表");
                    reverseList.clear();
                    musicCount.setText(reverseList.size() + "");
                    adapter.notifyDataSetChanged();
                    if(mediaPlayer_reciver!=null){
                        //mediaPlayer_reciver.stop();//清除当前的音乐
                        Intent intent = new Intent(MusicOperate.STOP);
                        mContext.sendBroadcast(intent);
                    }
                    dialog.dismiss();//dialog消失
                    setVisibility(GONE);
                    break;
                default:
            }
        }
    }

    /**
     * 实例化控件dialog
     */
    private void initDynamicView(View view) {

        deleteAllImage = (ImageView) view.findViewById(R.id.music_delete_all);

        musicCount = (TextView) view.findViewById(R.id.music_list_size);

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
    public void setProgressBar() {
        progressBar.getProgress();
    }

    public class NotificationMusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case MusicOperate.NOTIFICATION_PAUSE:
                    pause();
                    break;
                case MusicOperate.NOTIFICATION_PLAY:
                    play();
                    break;
                case MusicOperate.MUSIC_COUNT:
                    setMusicCount(intent);
                    break;
                case MusicOperate.NEXT_MUSIC:
                    Log.d(TAG,"接收到播放下一首的广播");
                    playNextMusic(intent);
                    break;
                case MusicOperate.PREVIOUS_MUSIC:
                    Log.d(TAG,"接收到播放上一首的广播");
                    playPreviousMusic(intent);
                    break;
                case MusicOperate.SHOW_BOTTOM:
                    Log.d(TAG,"接收到显示底部控件广播");
                    setVisibility(VISIBLE);
                    break;
                case MusicOperate.PLAY:
                    Log.d(TAG,"接收到播放音乐的广播，为获得当前播放歌曲的id");
                    setMusicInfoToAdapter(intent);
                    break;
                case MusicOperate.DISSHOW_BOTTOM:
                    Log.d(TAG, "onReceive: MusicOperate.DISSHOW_BOTTOM"+"没有歌曲底部控件消失");
                    dialog.dismiss();
                    setVisibility(GONE);
                    break;
                default:
            }
        }
    }

    /**
     * 获取穿给adapter的music
     * @param intent
     */
    private void setMusicInfoToAdapter(Intent intent) {
        Log.d(TAG,"通过静态方法设置音乐信息给adapter");
        Music music = (Music) intent.getParcelableExtra(MusicOperate.MUSIC);
        MusicDynamicAdapter.setMusicInfo(music);

    }

    /**
     *播放上一首歌
     */
    private void playPreviousMusic(Intent intent) {
        int position = getMusicPosition(intent,MusicOperate.PREVIOUS_MUSIC);
        Log.d(TAG,"上一首歌的position为 "+(position-1));
        Music pre_music = null;
        if(position > 0){
            pre_music = reverseList.get( position-1);
        }
        if(pre_music != null){
            sendPreOrNextBroadCast(pre_music);
        }else{
            Toast.makeText(mContext, "已经是第一首歌了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放下一首歌
     * @param intent
     */
    private void playNextMusic(Intent intent) {
        int position=getMusicPosition(intent,MusicOperate.NEXT_MUSIC);
        Log.d(TAG,"下一首歌的position为 "+(position+1));
        Music next_music=null;
        if(position < (reverseList.size()-1)){
            next_music = reverseList.get( position +1);
        }

        if(next_music != null){
            sendPreOrNextBroadCast(next_music);
        }else {
            Toast.makeText(mContext, "最后一首歌了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将得到的上一首或者下一首歌发送给Service
     * @param music
     */
    private void sendPreOrNextBroadCast(Music music) {
        progressBar.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        song_title.setText(music.title);
        circleImageView.setImageBitmap(GetBitMapById.getBitMap(music.albumPictureid));
        isPlay = false;

        Intent intent = new Intent(MusicOperate.PLAY);
        intent.putExtra("music", music);
        mContext.sendBroadcast(intent);
    }

    /**
     * 获取广播传过来的music所处的position
     * @param intent
     * @return
     */
    private int getMusicPosition(Intent intent,String musicString) {
        Music music = (Music) intent.getParcelableExtra(musicString);
        Log.d(TAG, "getMusicPosition: 准备获取下一首歌的id"+music.toString());
        int position = 0;
        for (int i = 0; i < reverseList.size(); i++) {
            if (reverseList.get(i).id==music.id){
                position=i;
            }
        }
        return position;
    }

    /**
     * 通知改变music的数量
     */
    private void setMusicCount(Intent intent) {
        int music_count = intent.getIntExtra(MusicOperate.MUSIC_COUNT, 0);
        Log.d(TAG, "onReceive: 音乐列表的数目为："+music_count);
        musicCount.setText(music_count+"");
    }

    /**
     * 接受通知栏发送过来的消息，播放音乐
     */
    private void play() {
        mediaPlayer_reciver.start();
        progressBar.setBackgroundResource(R.drawable.ic_pause_black_24dp);
        isPlay = false;
    }

    /**
     * 接受通知栏发送过来的消息，暂停音乐
     */
    private void pause() {
        //音乐停止，改变ui,设置isPlay为true
        mediaPlayer_reciver.pause();
        progressBar.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
        isPlay = true;
    }


}
