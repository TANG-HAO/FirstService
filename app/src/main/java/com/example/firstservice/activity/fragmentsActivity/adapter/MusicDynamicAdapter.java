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
    private List<Music> musicList;
    private Music music;
    private Context mContext;
    //private int position;
    private ViewHolder holder;

    public MusicDynamicAdapter(List<Music> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public MusicDynamicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_music, parent, false);
        holder = new ViewHolder(view);
        holder.deleteImage.setOnClickListener(new deleteOnClick());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicDynamicAdapter.ViewHolder holder, int position) {

        music = musicList.get(position);
        holder.titleText.setText(music.title);
        holder.artistText.setText(music.artist);


    }

    @Override
    public int getItemCount() {
        return musicList.size();
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


    private class deleteOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.music_delete:
                    int position = holder.getAdapterPosition();

                    Log.d("position", "delete" + position);
                    if (position < 0) {
                        position = 0;
                    }

                    musicList.remove(musicList.get(position));
                    notifyItemRemoved(position);

                    //通知music数目的变化
                    Intent intent = new Intent(MusicOperate.MUSIC_COUNT);
                    intent.putExtra(MusicOperate.MUSIC_COUNT, musicList.size());
                    intent.getAction();
                    intent.getAction();
                    intent.getAction();

                    mContext.sendBroadcast(intent);

                    //MusicControllerBar.setMusicList(musicList);//通知music数量改变了

                    break;
                default:
            }
        }

    }





}
