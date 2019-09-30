package com.example.firstservice.activity.rightmenu_activity.itemhelper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;

public class PersonRecycleItemTouchHelper extends ItemTouchHelper.Callback {
    private static final String TAG = "PersonRecycleItemTouchH";
    private ItemTouchHelperCallback callback;

    /**
     * 调用该方法创建对象时传入由adapter封装的callbcak对象；
     * @param callback
     */
    public PersonRecycleItemTouchHelper(ItemTouchHelperCallback callback){
        this.callback=callback;
    }
    /**
     * 设置滑动类型标记
     *实现上下左右均可以滑动但是删除
     * @param recyclerView
     * @param viewHolder
     * @return
     *          返回一个整数类型的标识，用于判断Item那种移动行为是允许的
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        Log.e(TAG,"getMovenmentFlags:");
        return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
    }

    /**
     * item是否支持长按，默认为true支持
     * @return boolean
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }
    /**
     * item是否支持滑动，默认为true支持
     * @return boolean
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }
    /**
     * 拖拽切换Item的回调
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     *          如果Item切换了位置，返回true；反之，返回false
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        Log.e(TAG,"onMove:");
        callback.onMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }
    /**
     * 滑动Item
     *
     * @param viewHolder
     * @param direction
     *           Item滑动的方向
     */

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e(TAG, "onSwiped: ");
        callback.onItemDelete(viewHolder.getAdapterPosition());
    }
    /**
     * Item被选中时候回调
     *
     * @param viewHolder
     * @param actionState
     *          当前Item的状态
     *          ItemTouchHelper.ACTION_STATE_IDLE   闲置状态
     *          ItemTouchHelper.ACTION_STATE_SWIPE  滑动中状态
     *          ItemTouchHelper#ACTION_STATE_DRAG   拖拽中状态
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    public interface ItemTouchHelperCallback {

        void onMove(int currentPosition, int targetPosition);

        void onItemDelete(int currentPosition);
    }
    /**
     * 移动过程中绘制Item
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     *          X轴移动的距离
     * @param dY
     *          Y轴移动的距离
     * @param actionState
     *          当前Item的状态
     * @param isCurrentlyActive
     *          如果当前被用户操作为true，反之为false
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //滑动时自己实现背景及图片
        if (actionState==ItemTouchHelper.ACTION_STATE_SWIPE){

            //dX大于0时向右滑动，小于0向左滑动
            View itemView=viewHolder.itemView;//获取滑动的view
            Resources resources=    MyApplication.getContext().getResources();
            Bitmap bitmap= BitmapFactory.decodeResource(resources, R.drawable.delete);//获取删除指示的背景图片
            int padding =10;//图片绘制的padding
            int maxDrawWidth=2*padding+bitmap.getWidth();//最大的绘制宽度
            Paint paint=new Paint();
            paint.setColor(resources.getColor(android.R.color.holo_red_light));
            int x=Math.round(Math.abs(dX));
            int drawWidth=Math.min(x,maxDrawWidth);//实际的绘制宽度，取实时滑动距离x和最大绘制距离maxDrawWidth最小值
            int itemTop=itemView.getBottom()-itemView.getHeight();//绘制的top位置
            //向右滑动
            if(dX>0){
                //根据滑动实时绘制一个背景
                c.drawRect(itemView.getLeft(),itemTop,drawWidth+itemView.getLeft(),itemView.getBottom(),paint);
               // new Path().moveTo(itemView.getRight(),itemView.getBottom()-itemView.getWidth());
                //在背景上面绘制图片
                if (x>padding){//滑动距离大于padding时开始绘制图片
                    //指定图片绘制的位置
                    Rect rect=new Rect();//画图的位置
                    rect.left=itemView.getLeft()+padding;
                    rect.top=itemTop+(itemView.getBottom()-itemTop-bitmap.getHeight())/2;//图片居中
                    int maxRight=rect.left+bitmap.getWidth();

                    rect.right=Math.min(itemView.getLeft()+x,maxRight);
                    rect.bottom=rect.top+bitmap.getHeight();
                    //指定图片的绘制区域
                    Rect rect1=null;
                    if (x<maxRight-itemView.getLeft()){
                        rect1=new Rect();//不能再外面初始化，否则dx大于画图区域时，删除图片不显示
                        rect1.left=0;
                        rect1.top = 0;
                        rect1.bottom=bitmap.getHeight();
                        rect1.right=x-padding;
                    }

                    c.drawBitmap(bitmap,rect1,rect,paint);
                }
                //绘制时需调用平移动画，否则滑动看不到反馈
                itemView.setTranslationX(dX);
            }else if(dX<0) {
                //如果在getMovementFlags指定了向左滑动（ItemTouchHelper。START）时则绘制工作可参考向右的滑动绘制，也可直接使用下面语句交友系统自己处理
                //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                //根据滑动实时绘制一个背景
                int left=itemView.getRight()-drawWidth;//背景绘制的区域的左边，即从屏幕左边到绘制区域左边的距离
                int top=itemTop;//背景绘制的区域的上边，即从屏幕上边到绘制区域上边的距离
                int right=itemView.getRight();//背景绘制的区域的右边，即从屏幕左边到绘制区域左边的距离
                int bottom=itemView.getBottom();////背景绘制的区域的下边，即从屏幕上边到绘制区域下边的距离
                c.drawRect(left,top,right,bottom,paint);
                //在背景上面绘制图片
                if (x>padding){//滑动距离大于padding时开始绘制图片
                    //指定图片绘制的位置
                    Rect rect=new Rect();//画图的位置

                    rect.right=itemView.getRight()-padding;
                    rect.top=itemTop+(itemView.getBottom()-itemTop-bitmap.getHeight())/2;//图片居中
                    int minLeft=rect.right-bitmap.getWidth();
                    rect.left=Math.min(itemView.getRight()+x,minLeft);
                    rect.bottom=rect.top+bitmap.getHeight();
                    //指定图片的绘制区域
                    Rect rect1=null;
                    if (x<bitmap.getWidth()+2*padding){
                        rect1=new Rect();//不能再外面初始化，否则dx大于画图区域时，删除图片不显示
                        rect1.left=0;
                        rect1.top = 0;
                        rect1.bottom=bitmap.getHeight();
                        rect1.right=x-padding;
                    }
                    c.drawBitmap(bitmap,rect1,rect,paint);
                }
                //绘制时需调用平移动画，否则滑动看不到反馈
                itemView.setTranslationX(dX);
            }
            float alpha = 1.0f - Math.abs(dX) / (float) itemView.getWidth();
            itemView.setAlpha(alpha);


        }else {
            //拖动时有系统自己完成
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
