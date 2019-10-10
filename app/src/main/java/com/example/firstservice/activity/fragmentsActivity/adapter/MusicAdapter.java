package com.example.firstservice.activity.fragmentsActivity.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.bean.Music;
import com.example.firstservice.widget.MusicControllerBar;

import java.io.IOException;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<Music> mlist;
    private Context mContext;
    private MediaPlayer mediaPlayer=new MediaPlayer();
    private Music music;

   // private int position;
    public MusicAdapter(List<Music> mlist) {
        this.mlist=mlist;
    }

    /**
     * 创建viewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.items_music,parent,false);
        final ViewHolder holder= new ViewHolder(view);
        holder.titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(mlist.get(position).data);
                    mediaPlayer.prepare();
                    //mediaPlayer.start();
//                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MusicControllerBar.setMusicInfo(mediaPlayer,mlist.get(position));
            }
        });
        return holder;
    }

    /**
     * 媒体播放器的初始化
     */
    private void initMediaPlayer() {



    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        music = mlist.get(position);
        holder.titleText.setText(music.title);//音乐名称
        holder.nameText.setText(music.artist);//演唱者
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }



    /**
     * 子列表项外部控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView titleText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText=(TextView)itemView.findViewById(R.id.title_Text);
            titleText=(TextView)itemView.findViewById(R.id.name_Text);
        }
    }
}
