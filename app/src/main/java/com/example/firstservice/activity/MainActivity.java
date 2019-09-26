package com.example.firstservice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
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
import com.example.firstservice.activity.rightmenu_activity.PersonActivity;
import com.example.firstservice.base.BaseActivity1;
import com.example.firstservice.fragment.MianFragment;
import com.example.firstservice.service.DownloadService;
import com.example.firstservice.service.MyIntentService;
import com.example.firstservice.service.Myservice;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends BaseActivity1 implements View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final int CHOOSE_PHOTO = 2;
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
    private NetWorkChangeReceiver netWorkChangeReceiver;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().hide();
        //replaceFragment(new MianFragment());
        initView();
        initOnClick();

        //监听网络变化
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        registerReceiver(netWorkChangeReceiver,intentFilter);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        if((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))!=
        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

    }

    private void initOnClick() {
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
        //侧滑菜单监听
        navigationView.setCheckedItem(R.id.nav_forceoffline);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void initView() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            //getSupportActionBar().setTitle(R.string.title);
            //getSupportActionBar().setHomeButtonEnabled(true);
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
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
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.nav_view);

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
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_forceoffline:
                Log.d(TAG,"强制下线");
                Intent intent = new Intent(getPackageName() + ".FORCE_OFFLINE");
                sendBroadcast(intent);
                break;
            case R.id.nav_friends:
                Intent intent1 = new Intent(this, PersonActivity.class);
                startActivity(intent1);
            case R.id.icon_image:
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }else {
                    openAlbum();
                }
        }
        return false;
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
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
            case 2:
                if(grantResults.length>0&&grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(netWorkChangeReceiver);
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

    private class NetWorkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isAvailable()){
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);//安卓4.0以上
                } else {
                    handleImageBeforeOnKitKat(data);//以下
                }
                break;
                default:
                    break;
        }


    }

    private void handleImageBeforeOnKitKat(Intent data) {

    }

    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();

    }
}
