package com.example.firstservice.activity.fragmentsActivity.adapter;

import android.content.Context;
import android.media.MediaPlayer;
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

import java.io.IOException;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> implements AdapterView.OnItemClickListener {
    private List<Music> mlist;
    private Context mContext;
    private MediaPlayer mediaPlayer;
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
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Music music = mlist.get(position);
        holder.titleText.setText(music.title);//音乐名称
        holder.nameText.setText(music.artist);//演唱者
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(music.data);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(mContext, "nihao"+i, Toast.LENGTH_SHORT).show();
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
