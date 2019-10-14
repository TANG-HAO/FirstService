package com.example.firstservice.adapter.contanctAdapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.adapter.FruitAdapter;
import com.example.firstservice.bean.Contanctor;
import com.example.firstservice.fragment.contactFragment.ContanctRightFragment;

import java.util.List;

public class ContanctAdapter extends RecyclerView.Adapter<ContanctAdapter.ViewHolder> {
    private final List<Contanctor> mlist;
    private boolean isTwoPane;
    private static final String TAG = "ContanctAdapter";

    public ContanctAdapter(List<Contanctor> contanctorList) {
        Log.d(TAG,"List<Contanctor>===>"+contanctorList.size());
        this.mlist=contanctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contanct_left_fragment_recycleview_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        //对列表项添加事件监听
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contanctor contanctor = mlist.get(viewHolder.getAdapterPosition());//获取支配器中当前viewHolder的数组位置
                if(isTwoPane){
                    //刷新右边fragment的数据
                    //ContanctRightFragment contanctRightFragment=(ContanctRightFragment)getFragmentMana

                }


            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {//根据列表项的最外层布局，实例化其中的控件
        private TextView nameText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText=(TextView)itemView.findViewById(R.id.contanct_left_fragment_recycleview_item_nametext);
        }
    }
}
