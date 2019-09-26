package com.example.firstservice.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.firstservice.R;
import com.example.firstservice.activity.MainActivity;
import com.example.firstservice.activity.controller.ActivityController;
import com.example.firstservice.activity.forceoffline.LoginActivity;
import com.example.firstservice.service.Myservice;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity1  extends AppCompatActivity {
    private ForceoffLineReciver forceoffLineReciver;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);//方便对activity批量操作

    }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }



}
