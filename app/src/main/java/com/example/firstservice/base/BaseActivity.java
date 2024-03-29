package com.example.firstservice.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstservice.R;
import com.example.firstservice.activity.MainActivity;
import com.example.firstservice.activity.controller.ActivityController;
import com.example.firstservice.activity.forceoffline.LoginActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private ForceoffLineReciver forceoffLineReciver;
    //资源管理
    protected Resources resources;
    //上下环境
    //protected Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);//方便对activity批量操作

        setContentView(getContentView());
        //context = this;
        resources = getResources();
        onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
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


    private class ForceoffLineReciver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Warning")
                    .setCancelable(false)
                    .setMessage(R.string.forceInfo);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityController.finishAll();//确定后清除所有的activity
                    Intent intent1 = new Intent(context, LoginActivity.class);
                    context.startActivity(intent1);
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(getPackageName()+".FORCE_OFFLINE");
         forceoffLineReciver= new ForceoffLineReciver();
        registerReceiver(forceoffLineReciver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(forceoffLineReciver!=null){
            unregisterReceiver(forceoffLineReciver);
            forceoffLineReciver=null;
        }
    }
}
