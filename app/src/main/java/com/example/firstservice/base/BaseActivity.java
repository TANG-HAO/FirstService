package com.example.firstservice.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    //资源管理
    protected Resources resources;
    //上下环境
    //protected Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        //context = this;
        resources = getResources();
        onCreate();
    }

    protected  void onCreate(){
        initView();
        initData();
        initListener();
    }
    /**
     * 首次   必先初始化  view
     */
    public abstract void initView();
    /**
     * 初始化数据   从网络 获取数据  数据添加到  view  等逻辑 工作
     */
    public abstract void initData();
    /**
     * 初始化  view listener 的工作
     */
    public abstract void initListener();

    public abstract int getContentView();

}
