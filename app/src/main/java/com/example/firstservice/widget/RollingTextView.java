package com.example.firstservice.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class RollingTextView extends TextView {
    public RollingTextView(Context context) {
        super(context);
    }

    public RollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取焦点
     * 实现多行跑马灯
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public void setEllipsize(TextUtils.TruncateAt where) {
        super.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }


    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(true);
    }

    @Override
    public void setFocusableInTouchMode(boolean focusableInTouchMode) {
        super.setFocusableInTouchMode(true);
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        super.setHorizontalScrollBarEnabled(true);
    }
}