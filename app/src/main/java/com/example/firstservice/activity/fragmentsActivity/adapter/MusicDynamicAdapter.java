package com.example.firstservice.activity.fragmentsActivity.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.bean.Music;

import java.util.List;

public class MusicDynamicAdapter extends RecyclerView.Adapter<MusicDynamicAdapter.ViewHolder> {
    private List<Music> musicList;
    public MusicDynamicAdapter(List<Music> musicList) {
        this.musicList= musicList;
    }

    @NonNull
    @Override
    public MusicDynamicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicDynamicAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
