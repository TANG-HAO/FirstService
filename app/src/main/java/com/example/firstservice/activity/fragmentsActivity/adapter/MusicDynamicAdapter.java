package com.example.firstservice.activity.fragmentsActivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;
import com.example.firstservice.bean.Music;
import com.example.firstservice.utils.MusicOperate;
import com.example.firstservice.widget.MusicControllerBar;

import java.util.List;

public class MusicDynamicAdapter extends RecyclerView.Adapter<MusicDynamicAdapter.ViewHolder> {
    private static final String TAG = "MusicDynamicAdapter";
    private List<Music> musicList;
    private Music music;
    private static Context mContext;
    private int position;
    // private ViewHolder holder;
    private static long music_id;
    private View view;

    public MusicDynamicAdapter(List<Music> musicList, Context context) {
        this.musicList = musicList;
        mContext = context;
    }

    /**
     * 获得点击的music,需要当前的播放的music
     *
     * @param music
     */
    public static void setMusicInfo(Music music) {
        Log.d(TAG, "setMusicInfo: 获得了music_id");
        music_id = music.id;

    }

    @NonNull
    @Override
    public MusicDynamicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mContext = parent.getContext();
        view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_music, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.music_delete:

                        int position = holder.getAdapterPosition();

                        Log.d(TAG, "onClick: 获取删除的音乐位置：" + position + "==" + holder.toString());
                        if (position < 0) {
                            position = 0;
                        }
                        if (music_id == musicList.get(position).id) {
                            Log.d(TAG, "onClick: 准备发送暂停广播");
                            Intent intent1 = new Intent(MusicOperate.STOP);
                            mContext.sendBroadcast(intent1);
                        }


                        musicList.remove(musicList.get(position));
                        notifyItemRemoved(position);
                        notifyItemRangeRemoved(position, musicList.size());//

                        Intent next_intent = new Intent(MusicOperate.PLAY);
                        next_intent.putExtra("music", musicList.get(position));
                        mContext.sendBroadcast(next_intent);


                        //通知music数目的变化
                        Intent intent = new Intent(MusicOperate.MUSIC_COUNT);
                        intent.putExtra(MusicOperate.MUSIC_COUNT, musicList.size());


                        //MusicControllerBar.setMusicList(musicList);//通知music数量改变了
                        if (musicList.size() == 0) {
                            Log.d(TAG, "onClick: musicList.size()=  " + musicList.size());
                            Log.d(TAG, "onClick: 准备发送底部控件消失广播");
                            intent.setAction(MusicOperate.DISSHOW_BOTTOM);
                            Log.d(TAG, "onClick: " + intent.getAction().toString());
                        }
                        mContext.sendBroadcast(intent);

                        break;
                    default:
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicDynamicAdapter.ViewHolder holder, int position) {
        //this.position=position;
        music = musicList.get(position);
        holder.titleText.setText(music.title);
        holder.artistText.setText(music.artist);


    }

    @Override
    public int getItemCount() {
        return musicList == null ? 0 : musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView artistText;
        private ImageView deleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.title_dynamic_text);
            artistText = (TextView) itemView.findViewById(R.id.artist_dynamic_text);
            deleteImage = (ImageView) itemView.findViewById(R.id.music_delete);
        }
    }


//    private class deleteOnClick implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.music_delete:
//                    //todo 存在holder position=-1的crash
//                    int position = holder.getAdapterPosition();
//
//                    Log.d(TAG, "onClick: 获取删除的音乐位置："+position+"=="+holder.toString());
//                    if (position < 0) {
//                        position = 0;
//                    }
//                    if (music_id == musicList.get(position).id){
//                        Log.d(TAG, "onClick: 准备发送暂停广播");
//                        Intent intent1 = new Intent(MusicOperate.STOP);
//                        mContext.sendBroadcast(intent1);
//                    }
//
//                    musicList.remove(musicList.get(position));
//                    notifyItemRemoved(position);
//
//
//
//
//                    //通知music数目的变化
//                    Intent intent = new Intent(MusicOperate.MUSIC_COUNT);
//                    intent.putExtra(MusicOperate.MUSIC_COUNT, musicList.size());
//                    mContext.sendBroadcast(intent);
//
//                    //MusicControllerBar.setMusicList(musicList);//通知music数量改变了
//
//
//                    break;
//                default:
//
//            }
//        }
//
//    }

    private void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }


}
