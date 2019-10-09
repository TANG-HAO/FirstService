package com.example.firstservice.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

public abstract class RecycleOnscrollListener extends OnScrollListener {

    private boolean isSlidingUp = false;//标记是否正在向上滑动

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

        //当不滑动时
        if(newState==RecyclerView.SCROLL_STATE_IDLE){
            //获取最后一个完全显示的itemPosition
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            int itemCount = manager.getItemCount();

            //判断是否滑动到乐最后一个item，并且是向上滑动
            if(lastItemPosition==(itemCount - 1)&&isSlidingUp){
                //加载更多
                onLoadMore();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        super.onScrolled(recyclerView,dx,dy);
        //大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUp = dy > 0;
    }

    /**
     * 加载更多回调
     */
    protected abstract void onLoadMore();
}
