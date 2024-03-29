package com.example.firstservice.adapter.personAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstservice.R;
import com.example.firstservice.activity.rightmenu_activity.itemhelper.PersonRecycleItemTouchHelper;
import com.example.firstservice.bean.MyPerson;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> implements PersonRecycleItemTouchHelper.ItemTouchHelperCallback {
    private List<MyPerson> mlist;
    private static final String TAG = "PersonAdapter";
    private Context mcontext;

    public PersonAdapter(List<MyPerson> mlist) {
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        if(mcontext==null){
            mcontext=parent.getContext();
        }
        final ViewHolder holder = new ViewHolder(view);
        holder.personCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "请添加响应事件", Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//绑定数据，
        MyPerson person = mlist.get(position);
        holder.personText.setText(person.getPersoName());
        Glide.with(mcontext).load(person.getPersonImageId()).into(holder.personImage);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public void onMove(int currentPosition, int targetPosition) {//移动列表项，并刷新
        Collections.swap(mlist,currentPosition,targetPosition);
        notifyItemMoved(currentPosition,targetPosition);
    }

    @Override
    public void onItemDelete(int currentPosition) {//移除列表项并刷新
        mlist.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {//声明viewholder中的控件，并实例化
        private CardView personCard;
        private ImageView personImage;
        private TextView personText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personCard=(CardView)itemView;
            personImage=(CircleImageView)itemView.findViewById(R.id.person_image);
            personText=(TextView)itemView.findViewById(R.id.person_info);
        }
    }

}
