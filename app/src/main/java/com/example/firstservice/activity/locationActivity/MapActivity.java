package com.example.firstservice.activity.locationActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.firstservice.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MapActivity";
    private MapView mMapView;
    List<String> permissionList = new ArrayList<String>();
    public LocationClient mlocationClient;
    private TextView jingduText;
    private TextView weiduText;
    private TextView locateModeText;
    private TextView countryText;
    private TextView provinceText;
    private TextView cityText;
    private BaiduMap baidumap;
    private boolean isFirstLocate = true;
    private UiSettings mUiSettings;
    private boolean isenabled = true;
    private MyOrientationListener myOrientationListener;//指南针
    private BitmapDescriptor bitmapDescriptor;
    private float mLastX;//传感器传过来的值
    private ImageView reLocationImage;
    private BDLocation location;//传值需要
    private LatLng latLng;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        //SDKInitializer.setCoordType(CoordType.BD09LL);
        //启动定位平台，注册位置监听
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mlocationClient = new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_map);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        baidumap = mMapView.getMap();//在地图中地位到自己的地址
        baidumap.setMyLocationEnabled(true);//开启地图的定位图层
        //baidumap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//地图类型

        //实例化UiSettings类对象
        mUiSettings = baidumap.getUiSettings();
//通过设置enable为true或false 选择是否显示指南针
        mUiSettings.setCompassEnabled(isenabled);
        baidumap.setCompassPosition(new Point(120, 600));

        jingduText = (TextView) findViewById(R.id.jingdu);
        weiduText = (TextView) findViewById(R.id.weidu);
        locateModeText = (TextView) findViewById(R.id.locat_mode);
        countryText = (TextView) findViewById(R.id.locat_country);
        provinceText = (TextView) findViewById(R.id.locat_province);
        cityText = (TextView) findViewById(R.id.locat_city);
        reLocationImage = (ImageView) findViewById(R.id.relocation);
        //requestLocation();
        //检查权限
        checkPermission();
        onclick();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        baidumap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mlocationClient.stop();
        //停止方向传感器
        myOrientationListener.stop();
        super.onDestroy();

    }

    /**
     * 检查权限
     */
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    /**
     * 得到授权结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantresult : grantResults) {
                        if (grantresult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同一所有权限才能使用本f服务", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 权限检查完，开启定位平台，监听定位
     */
    private void requestLocation() {
        initLocation();//实时获取
        initMyOrien();
        if (!mlocationClient.isStarted()) {
            mlocationClient.start();
            //开启方向传感器
            myOrientationListener.star();
        }


    }

    /**
     * 实时获取位置
     */
    private void initLocation() {
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setScanSpan(1000);//更新间隔
        locationClientOption.setIsNeedAddress(true);//设置需要位置信息
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//高精度
        locationClientOption.setCoorType("bd09ll");//定位准确
        locationClientOption.setNeedDeviceDirect(true);//需要设备方向
        mlocationClient.setLocOption(locationClientOption);
    }

    /**
     * 重写事件监听 点击触发
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relocation:
                Log.d(TAG, "重新定位");
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                baidumap.animateMapStatus(mapStatusUpdate);
                break;

        }
    }

    /**
     * 定位监听器，从他获取的值bdLocation中获取信息
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mapView 销毁后不在处理新接收的位置
                    if (bdLocation == null || mMapView == null) {
                        return;
                    }
                    jingduText.setText(new DecimalFormat("#.000000").format(bdLocation.getLongitude()));
                    weiduText.setText(new DecimalFormat("#.000000").format(bdLocation.getLatitude()));
                    countryText.setText(bdLocation.getCountry());
                    provinceText.setText(bdLocation.getProvince());
                    cityText.setText(bdLocation.getCity());
                    if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                        locateModeText.setText("GPS定位");
                        navigateTo(bdLocation);

                    } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                        locateModeText.setText("网络定位");
                        navigateTo(bdLocation);

                    }
//配置定位图层显示方式，使用自己的定位图标
// LocationMode定位模式有三种：普通模式，跟随模式，罗盘模式，在这使用普通模式
                    MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor,0xAAFFFF88,0xAA00FF00);

                    baidumap.setMyLocationConfigeration(configuration);

                }
            });
        }
    }

    /**
     * 地图上地位当前位置
     *
     * @param bdLocation
     */
    private void navigateTo(BDLocation bdLocation) {
        if (isFirstLocate) {
            latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
            baidumap.animateMapStatus(statusUpdate);
            statusUpdate = MapStatusUpdateFactory.zoomTo(16f);
            baidumap.animateMapStatus(statusUpdate);
            isFirstLocate = false;
        }
        drawMyLocation(bdLocation);
    }

    /**
     * 在地图中画出自己
     *
     * @param bdLocation
     */
    private void drawMyLocation(BDLocation bdLocation) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mLastX)// 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        baidumap.setMyLocationData(locData);
    }

    /**
     * 依靠传感器获取值
     */
    private void initMyOrien() {
        //初始化图标
        // bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.northdirection);
        //方向传感器
        myOrientationListener = new MyOrientationListener(this);

        myOrientationListener.setmOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mLastX = x;
            }
        });
    }

    public void  setCompassMode(){
       // baidumap.setMyLocationConfiguration(new MyLocationConfiguration());
    }

    public void onclick() {
        reLocationImage.setOnClickListener(this);
    }


}


