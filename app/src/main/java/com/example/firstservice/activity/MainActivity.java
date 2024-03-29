package com.example.firstservice.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
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
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.baidu.mapapi.map.MapView;
import com.example.firstservice.R;
import com.example.firstservice.activity.contanctActivity.ContanctActivity;
import com.example.firstservice.activity.locationActivity.MapActivity;
import com.example.firstservice.activity.rightmenu_activity.PersonActivity;
import com.example.firstservice.activity.rightmenu_activity.PersonActivity_tem;
import com.example.firstservice.base.BaseActivity1;
import com.example.firstservice.bean.ImageData;
import com.example.firstservice.fragment.MianFragment;
import com.example.firstservice.fragment.activitycahngefragmnet.HomeFragement;
import com.example.firstservice.fragment.activitycahngefragmnet.MenuFragment;
import com.example.firstservice.mail.MailActivity;
import com.example.firstservice.service.DownloadService;
import com.example.firstservice.service.MyIntentService;
import com.example.firstservice.service.Myservice;
import com.example.firstservice.utils.TimeChange;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.sql.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity1 implements View.OnLongClickListener, NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int CHOOSE_PHOTO = 2;
    private static final int REQUEST_SMALL_IMAGE_CUTTING = 1;
    private static final int AFTER_CROP = 3;
    private static final String HEAD = "head";//存贮头像的文件名
    private static final String IMPAGEPATH = "imagePath";
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
    private static final String TAG = MainActivity.class.getName();
    private Button newsButton;
    private Button contanctButton;
    private NetWorkChangeReceiver netWorkChangeReceiver;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CircleImageView picture;
    private String imagePath;
    private Uri uri;
    private MapView mMapView;
    private BottomNavigationView bottomNavigationView;

    private HomeFragement homeFragement;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tem);

        homeFragement=new HomeFragement();
        initView();
        //getActionBar().hide();
        //replaceFragment(new MianFragment());
        LitePal.getDatabase();


        //监听网络变化
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        registerReceiver(netWorkChangeReceiver, intentFilter);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        if ((ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        initOnClick();
    }

    private void replaceFragment(Fragment fragement) {
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framgment_container,fragement);
        transaction.commit();
    }

    private void initOnClick() {

        bindButton.setOnClickListener(new onBindClick());
        unbindButton.setOnClickListener(new onBindClick());
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
        picture.setOnClickListener(new onBindClick());

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }



        bindButton = (Button) findViewById(R.id.bindButton);
        unbindButton = (Button) findViewById(R.id.unbindButton);
        startIntentServiceButton = (Button) findViewById(R.id.intent_button);
        startDownloadButton = (Button) findViewById(R.id.start_download);
        pauseDownloadButton = (Button) findViewById(R.id.pause_download);
        cancleDownloadButton = (Button) findViewById(R.id.cancle_download);
        recycleViewButton = (Button) findViewById(R.id.to_recycle_view);
        fragmentButton = (Button) findViewById(R.id.fragmentButton);
        newsButton = (Button) findViewById(R.id.news_button);
        contanctButton = (Button) findViewById(R.id.contanctButton);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //picture=(CircleImageView)findViewById(R.id.icon_image);
        View header = navigationView.inflateHeaderView(R.layout.nav_header);//
        picture = (CircleImageView) header.findViewById(R.id.icon_image);
        readImagePathByLitePal();
        //greateImageView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_start:
                Intent intent = new Intent(MainActivity.this, Myservice.class);
                startService(intent);
                break;
            case R.id.menu_close:
                Intent intent1 = new Intent(MainActivity.this, Myservice.class);
                stopService(intent1);
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

                default:
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "disconnect excuted");
        }
    };

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.fragmentButton:
                Intent intent = new Intent(MainActivity.this, FragmentsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_forceoffline:
                Log.d(TAG, "强制下线");
                Intent intent = new Intent(getPackageName() + ".FORCE_OFFLINE");
                sendBroadcast(intent);
                break;
            case R.id.nav_friends:
                Intent intent1 = new Intent(this, PersonActivity_tem.class);
                startActivity(intent1);
                break;
            case R.id.nav_call://调用联系人
                Runnable myRunable = new Runnable() {
                    @Override
                    public void run() {
                        //startOtherApplication("com.example.contactlisttest");
                        startOtherApplication("com.example.contactlisttest");
                    }
                };
                new Thread(myRunable).start();
                break;
            case R.id.nav_location:
                Intent intent2 = new Intent(this, MapActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_mail:
                Intent intent3 = new Intent(MainActivity.this, MailActivity.class);
                Log.d(TAG, "正在进入网页");
                startActivity(intent3);
                break;
            case R.id.nav_others1:
                Intent intent4 = new Intent(this, com.example.firstservice.activity.fragmentsActivity.FragmentsActivity.class);
                startActivity(intent4);
//            case R.id.home:
//                replaceFragment(new HomeFragement());
//                break;
//            case R.id.menu:
//                replaceFragment(new MenuFragment());
//                break;
//            case R.id.my:
//                replaceFragment(new MyFragement());
//                break;
        }
        return true;
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        // 判断系统中是否有处理该 Intent 的 Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CHOOSE_PHOTO);
        } else {
            Toast.makeText(MainActivity.this, "未找到图片查看器", Toast.LENGTH_SHORT).show();
        }
    }


    private class onBindClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bindButton:
                    Intent bindintent = new Intent(MainActivity.this, Myservice.class);
                    bindService(bindintent, connection, BIND_AUTO_CREATE);
                    break;
                case R.id.unbindButton:
                    Intent intent1 = new Intent(MainActivity.this, Myservice.class);
                    unbindService(connection);
                    break;
                case R.id.intent_button:
                    Log.d(TAG, "Thread id is" + Thread.currentThread().getId());
                    Intent intent2 = new Intent(MainActivity.this, MyIntentService.class);
                    startService(intent2);
                    break;
                case R.id.start_download:
                    String url = "https://dl.google.com/dl/android/studio/install/3.5.0.21/android-studio-ide-191.5791312-windows.exe";
                    downloadBinder.startDownload(url);
                    break;
                case R.id.pause_download:

                    downloadBinder.pauseDownload();
                    break;
                case R.id.cancle_download:
                    downloadBinder.cancelDownload();
                    break;
                case R.id.to_recycle_view:
                    Intent intent = new Intent(MainActivity.this, ReclyViewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.news_button:
                    Intent intent4 = new Intent(MainActivity.this, NewsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.contanctButton:
                    Intent intent5 = new Intent(MainActivity.this, ContanctActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.icon_image:
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setItems(R.array.items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        cropPhoto();//直接拍照
                                        Toast.makeText(MainActivity.this, "裁剪", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        openAlbum();//直接在选择图片好后返回到一个activity对图片进行裁剪

                                        Toast.makeText(MainActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                        builder.create().show();


                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 图片剪切
     */
    /**
     * 裁剪
     */
    private void cropPhoto() {
        // File file = new FileStorage().createCropFile();
        // Uri outputUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        //  intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, AFTER_CROP);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
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

    private void replaceFragment(Fragment fragment, int startAddToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_main_contanier, fragment);
        if (startAddToBackStack == 1) {
            transaction.addToBackStack(null);//将碎片添加到返回栈
        }
        transaction.commit();
    }

    private class NetWorkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, "当前未选择图片", Toast.LENGTH_SHORT).show();
        }
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


    //版本之前
    private void handleImageBeforeOnKitKat(Intent data) {
        uri = data.getData();
        imagePath = getImagePath(uri, null);
        diaplayImage(imagePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型地Uri，直接获取图片的路径即可
            imagePath = uri.getPath();
        }
        //修改为裁剪后引入
        diaplayImage(imagePath);


    }

    private void diaplayImage(String imagePath) {
        if (!imagePath.isEmpty()) {
            writeImagePathByLitePal(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String imagePath = null;
        //通过uri和selection来获取真实图片的路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return imagePath;
    }

    /**
     * 将图片路径存入文件中
     *
     * @param imagePath
     */
    @SuppressLint("CommitPrefEdits")
    public void writeImagePath(String imagePath) {
        SharedPreferences.Editor head = getSharedPreferences(HEAD, MODE_PRIVATE).edit();
        head.putString(IMPAGEPATH, imagePath);
        head.apply();
    }

    /**
     * 将图片从文件中读取出来
     */
    public void readImagePath() {
        SharedPreferences readhead = getSharedPreferences(HEAD, MODE_PRIVATE);
        imagePath = readhead.getString(IMPAGEPATH, "");
        if (imagePath.isEmpty()) {
            picture.setImageResource(R.drawable.ic_bubble_chart_black_24dp);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }
    }

    /**
     * 将图片路径存入文件中数据库
     *
     * @param imagePath
     */
    public void writeImagePathByLitePal(String imagePath) {
        ImageData imageData = new ImageData(imagePath, TimeChange.timeOfDataToSql());
        Log.d(TAG, imagePath + "==========");
        Log.d(TAG, TimeChange.timeOfDataToSql().toString() + "==========");
        if (imageData.save()) {
            Toast.makeText(this, "图片存储成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "图片存储失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 将图片从文件中读取出来
     */
    public void readImagePathByLitePal() {
        List<ImageData> impagePaths = DataSupport.select("imagePath").find(ImageData.class);

        //Log.d(TAG, impagePaths.size() + "==========="+impagePaths.get(0).getDate().toString());
        if (impagePaths.size() == 0) {
            picture.setImageResource(R.drawable.ic_bubble_chart_black_24dp);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(impagePaths.get(0).getImagePath());
            picture.setImageBitmap(bitmap);
        }
    }

    /**
     * 根据包名调用其他程序
     *
     * @param appPackageName
     */
    public void startOtherApplication(String appPackageName) {

        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "未装该应用", Toast.LENGTH_SHORT).show();
        } finally {

        }
    }


    /**
     * uri启动，需要在启动的app的配置文件中添加schema android  app
     *
     * @param packageName
     * @param className
     */

    public void startOtherApplicationActivity(String packageName, String className) {
        Uri uri = Uri.parse("android://app");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void greateImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.center_direction);
        imageView.setX(20);
        imageView.setY(20);
    }



}
