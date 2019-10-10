package com.example.firstservice.activity.fragmentsActivity.adapter;

import android.app.Notification;
import android.app.NotificationManager;
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
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.bean.Music;
import com.example.firstservice.widget.MusicControllerBar;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
                MusicControllerBar.setMusicInfo(mediaPlayer,mlist.get(position));//传入自定义控件所需的参数
                //todo 创建通知
                NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
                builder.setContentTitle(mlist.get(position).title)
                        .setContentText(mlist.get(position).artist)
                        .setLargeIcon(mlist.get(position).albumPicture)
                        .set
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
        holder.circleImageView.setImageBitmap(music.albumPicture);//歌曲封面
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }



    /**
     * 子列表项外部控件
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView nameText;
        private TextView titleText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.album_picture);
            nameText=(TextView)itemView.findViewById(R.id.title_Text);
            titleText=(TextView)itemView.findViewById(R.id.name_Text);
        }
    }
}
