package com.example.firstservice.adapter.personAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstservice.R;
import com.example.firstservice.activity.rightmenu_activity.itemhelper.PersonRecycleItemTouchHelper;
import com.example.firstservice.bean.MyPerson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonAdapter_tem extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements PersonRecycleItemTouchHelper.ItemTouchHelperCallback {
    private List<MyPerson> mlist;
    private Context mContext;
    private final int ITEM_TYPE=0;//item布局类型
    private final int FOOTER_TYPE=1;//footer布局类型
    //加载状态
    public static final int LOADING=0;//加载中
    public static final int LOAD_FINISH=1;//加载结束
    public static final int LOAD_END=2;//加载到底

    private int loadState=LOAD_FINISH;//设置加载状态

    public PersonAdapter_tem(List<MyPerson> mlist) {
        this.mlist=mlist;
    }

    /**
     * 回调得到viewholder 穿给bindViewHolder
     * 或者设置监听
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        if(viewType==FOOTER_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, null);
            return  new FooterViewHolder(view);
        }else if(viewType==ITEM_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, null);
            return new ItemViewHolder(view);
        }
        return null;
    }

    /**
     * 为viewholder的控件绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            MyPerson person = mlist.get(position);
            itemViewHolder.personText.setText(person.getPersoName());
            Glide.with(mContext).load(person.getPersonImageId()).into(itemViewHolder.personImage);
        }else if(holder instanceof FooterViewHolder){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            if(loadState==LOADING){
                footerViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                footerViewHolder.tvMsg.setVisibility(View.VISIBLE);
                footerViewHolder.tvBottom.setVisibility(View.GONE);
            }else if(loadState==LOAD_FINISH){
                footerViewHolder.mProgressBar.setVisibility(View.INVISIBLE);
                footerViewHolder.tvMsg.setVisibility(View.INVISIBLE);
                footerViewHolder.tvBottom.setVisibility(View.GONE);
            }else if (loadState==LOAD_END){
                footerViewHolder.mProgressBar.setVisibility(View.GONE);
                footerViewHolder.tvMsg.setVisibility(View.GONE);
                footerViewHolder.tvBottom.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mlist.size()+1;  //多了footer控件
    }

    /**
     * item 布局holder
     */
    static  class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView personCard;
        private ImageView personImage;
        private TextView personText;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            personCard=(CardView)itemView;
            personImage=(CircleImageView)itemView.findViewById(R.id.person_image);
            personText=(TextView)itemView.findViewById(R.id.person_info);
        }
    }

    /**
     * footer 布局holder
     *
     */
    static  class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private TextView tvMsg;
        private TextView tvBottom;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progressBar);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            tvBottom = itemView.findViewById(R.id.tvBottom);
        }
    }

    /**
     * 获取列表项视图类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if(position+1==getItemCount()){
            return FOOTER_TYPE;
        }
        return ITEM_TYPE;
    }

    /**
     * 添加数据动态刷新
     * @param items
     */
    public void AddHeaderItem(ArrayList<MyPerson> items){
        mlist.addAll(0,items);
        notifyDataSetChanged();
    }

    /**
     * 添加加载数据
     * @param items
     */
    public void AddFooterItem(ArrayList<MyPerson> items){
        mlist.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 设置加载状态
     * @param loadState
     */
    public void setLoadState(int loadState){
        this.loadState=loadState;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager=(GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    //如果当前是footer的位置，那么该item占据n个单元格，正常情况下占据1个
                    return getItemViewType(position)==FOOTER_TYPE?gridLayoutManager.getSpanCount():1;
                }
            });
        }
    }

    /**
     * 移动列表项动态刷新
     * @param currentPosition
     * @param targetPosition
     */
    @Override
    public void onMove(int currentPosition, int targetPosition) {
        Collections.swap(mlist,currentPosition,targetPosition);
        notifyItemMoved(currentPosition,targetPosition);
    }

    /**
     * 删除数据动态刷新
     * @param currentPosition
     */
    @Override
    public void onItemDelete(int currentPosition) {
        mlist.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }
}
