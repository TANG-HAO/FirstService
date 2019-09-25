package com.example.firstservice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.firstservice.R;
import com.example.firstservice.activity.contanctActivity.ContanctActivity;
import com.example.firstservice.fragment.MianFragment;
import com.example.firstservice.service.DownloadService;
import com.example.firstservice.service.MyIntentService;
import com.example.firstservice.service.Myservice;


public class MainActivity extends AppCompatActivity implements View.OnLongClickListener{
    private Toolbar toolbar;
    private Button bindButton;
    private Button unbindButton;
    private Button startIntentServiceButton;
    private Button startDownloadButton;
    private Button pauseDownloadButton;
    private Button cancleDownloadButton;
    private Button recycleViewButton;
    private DownloadService.DownloadBinder downloadBinder;
    private Button fragmentButton;
    private static final String TAG =MainActivity.class.getName();
    private Button newsButton;
    private Button contanctButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().hide();
        //replaceFragment(new MianFragment());
        initView();
        bindButton.setOnClickListener(new onBindClick());
        bindButton.setOnClickListener(new onBindClick());
        startIntentServiceButton.setOnClickListener(new onBindClick());
        startDownloadButton.setOnClickListener(new onBindClick());
        pauseDownloadButton.setOnClickListener(new onBindClick());
        cancleDownloadButton.setOnClickListener(new onBindClick());
        recycleViewButton.setOnClickListener(new onBindClick());
        fragmentButton.setOnLongClickListener(this);
        newsButton.setOnClickListener(new onBindClick());
        contanctButton.setOnClickListener(new onBindClick());

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        if((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

    }



    private void initView() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        bindButton=(Button)findViewById(R.id.bindButton);
        unbindButton=(Button)findViewById(R.id.unbindButton);
        startIntentServiceButton=(Button)findViewById(R.id.intent_button);
        startDownloadButton=(Button)findViewById(R.id.start_download);
        pauseDownloadButton=(Button)findViewById(R.id.pause_download);
        cancleDownloadButton=(Button)findViewById(R.id.cancle_download);
        recycleViewButton=(Button)findViewById(R.id.to_recycle_view);
        fragmentButton=(Button)findViewById(R.id.fragmentButton);
        newsButton=(Button)findViewById(R.id.news_button);
        contanctButton=(Button)findViewById(R.id.contanctButton);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_start:
                Intent intent = new Intent(MainActivity.this, Myservice.class);
                startService(intent);
                break;
            case R.id.menu_close:
                Intent intent1 = new Intent(MainActivity.this,Myservice.class);
                stopService(intent1);
                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return  true;
    }

    private ServiceConnection connection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG,"disconnect excuted");
        }
    };

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.fragmentButton :
                Intent intent = new Intent(MainActivity.this,FragmentsActivity.class);
                startActivity(intent);
                break;
            default:break;
        }
        return  true;
    }


    private class onBindClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bindButton :
                    Intent bindintent = new Intent(MainActivity.this, Myservice.class);
                    bindService(bindintent,connection,BIND_AUTO_CREATE);
                    break;
                case R.id.unbindButton:
                    Intent intent1 = new Intent(MainActivity.this, Myservice.class);
                    unbindService(connection);
                    break;
                case R.id.intent_button:
                    Log.d(TAG,"Thread id is"+Thread.currentThread().getId());
                    Intent intent2 = new Intent(MainActivity.this, MyIntentService.class);
                    startService(intent2);
                    break;
                case R.id.start_download:
                    String url="https://dl.google.com/dl/android/studio/install/3.5.0.21/android-studio-ide-191.5791312-windows.exe";
                    downloadBinder.startDownload(url);
                    break;
                case R.id.pause_download:

                    downloadBinder.pauseDownload();
                    break;
                case R.id.cancle_download:
                    downloadBinder.cancelDownload();
                    break;
                case R.id.to_recycle_view:
                    Intent intent = new Intent(MainActivity.this,ReclyViewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.news_button:
                    Intent intent4 = new Intent(MainActivity.this,NewsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.contanctButton:
                    Intent intent5 = new Intent(MainActivity.this, ContanctActivity.class);
                    startActivity(intent5);
                    break;
                default:break;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void replaceFragment(Fragment fragment,int startAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_main_contanier,fragment);
        if (startAddToBackStack==1){
            transaction.addToBackStack(null);//将碎片添加到返回栈
        }
        transaction.commit();
    }
}
