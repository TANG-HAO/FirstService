package com.example.firstservice.activity.fragmentsActivity.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.activity.fragmentsActivity.adapter.MusicAdapter;
import com.example.firstservice.bean.Music;
import com.example.firstservice.service.MusicService;
import com.example.firstservice.utils.GetBitMapById;
import com.example.firstservice.widget.MusicControllerBar;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MusicFragment";
    private MusicControllerBar musicRelativeLayout;
    private Context mContext;
    private Activity mActivity;
    private View view;
    private ContentResolver resolver;
    private List<Music> musics;
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music_fragment, container, false);
        Log.d(TAG, "进入音乐播放器");
        initView();//控件实例化
        requestPermission();
        getMusicInfo();

        //直接启动musicService
        Intent music_intent = new Intent(mContext, MusicService.class);
        mContext.startService(music_intent);

        //初始化数据  结合适配器
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.HORIZONTAL));
        musicAdapter = new MusicAdapter(musics);
        recyclerView.setAdapter(musicAdapter);
        return view;
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        mContext = getContext();
        mActivity = getActivity();
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "获取音乐", Toast.LENGTH_SHORT).show();
                    //getMusicInfo();//获取音乐信息
                } else {
                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void initView() {
        musicRelativeLayout = (MusicControllerBar) view.findViewById(R.id.musicrelativelayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.music_recycle_view);
    }

    /**
     * 获取音乐信息
     */
    private void getMusicInfo() {
        Log.d("获取", "获取信息");
        resolver = getContext().getContentResolver();
        musics = new ArrayList<>();


        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            Music music = new Music();
            music.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            music.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            music.data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            music.album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            music.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            music.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            music.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
           // music.albumPicture = GetBitMapById.getBitMap(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            music.albumPictureid = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            musics.add(music);
        }
        cursor.close();

    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(mContext, i, Toast.LENGTH_SHORT).show();
    }
}
