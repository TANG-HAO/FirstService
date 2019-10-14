package com.example.firstservice.activity.fragmentsActivity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;
import com.example.firstservice.bean.Music;

import java.util.List;

public class MusicDynamicAdapter extends RecyclerView.Adapter<MusicDynamicAdapter.ViewHolder> {
    private List<Music> musicList;
    private Music music;
    private Context mContext= MyApplication.getContext();
    public MusicDynamicAdapter(List<Music> musicList) {
        this.musicList= musicList;
    }

    @NonNull
    @Override
    public MusicDynamicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_music, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicDynamicAdapter.ViewHolder holder, int position) {
        if(getItemCount()>0){
            music = musicList.get(position);
            holder.titleText.setText(music.title);
            holder.artistText.setText(music.artist);
        }

    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleText;
        private TextView artistText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText=(TextView)itemView.findViewById(R.id.title_dynamic_text);
            artistText=(TextView)itemView.findViewById(R.id.artist_dynamic_text);
        }
    }
}
