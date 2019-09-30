package com.example.firstservice.fragment.activitycahngefragmnet;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.firstservice.R;
import com.example.firstservice.activity.FragmentsActivity;
import com.example.firstservice.activity.MainActivity;
import com.example.firstservice.activity.MainActivity_tem;
import com.example.firstservice.activity.NewsActivity;
import com.example.firstservice.activity.ReclyViewActivity;
import com.example.firstservice.activity.contanctActivity.ContanctActivity;
import com.example.firstservice.service.DownloadService;
import com.example.firstservice.service.MyIntentService;
import com.example.firstservice.service.Myservice;

import static android.content.Context.BIND_AUTO_CREATE;

public class MenuFragment extends Fragment implements View.OnLongClickListener {
    private Button bindButton;
    private Button unbindButton;
    private Button startIntentServiceButton;
    private Button startDownloadButton;
    private Button pauseDownloadButton;
    private Button cancleDownloadButton;
    private Button recycleViewButton;
    private DownloadService.DownloadBinder downloadBinder;
    private Button fragmentButton;
    private static final String TAG = "MenuFragment";
    private Button newsButton;
    private Button contanctButton;
    private Activity activity=(MainActivity)getActivity();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,false);
        //initView();
        //initOnClick();
        //Intent intent = new Intent(activity, DownloadService.class);
        //activity.startService(intent);
        //activity.bindService(intent, connection, BIND_AUTO_CREATE);
        return view;
    }

    private void initView() {
        bindButton = (Button) getActivity().findViewById(R.id.bindButton);
        unbindButton = (Button) getActivity().findViewById(R.id.unbindButton);
        startIntentServiceButton = (Button) getActivity().findViewById(R.id.intent_button);
        startDownloadButton = (Button) getActivity().findViewById(R.id.start_download);
        pauseDownloadButton = (Button) getActivity().findViewById(R.id.pause_download);
        cancleDownloadButton = (Button) getActivity().findViewById(R.id.cancle_download);
        recycleViewButton = (Button) getActivity().findViewById(R.id.to_recycle_view);
        fragmentButton = (Button) getActivity().findViewById(R.id.fragmentButton);
        newsButton = (Button) getActivity().findViewById(R.id.news_button);
        contanctButton = (Button) getActivity().findViewById(R.id.contanctButton);
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
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.fragmentButton:
                Intent intent = new Intent(getActivity(), FragmentsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    private class onBindClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bindButton:
                    Intent bindintent = new Intent(getActivity(), Myservice.class);
                    getActivity().bindService(bindintent, connection, BIND_AUTO_CREATE);
                    break;
                case R.id.unbindButton:
                    Intent intent1 = new Intent(getActivity(), Myservice.class);
                    getActivity().unbindService(connection);
                    break;
                case R.id.intent_button:
                    Log.d(TAG, "Thread id is" + Thread.currentThread().getId());
                    Intent intent2 = new Intent(getActivity(), MyIntentService.class);
                    getActivity().startService(intent2);
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
                    Intent intent = new Intent(getActivity(), ReclyViewActivity.class);
                    startActivity(intent);
                    break;
                case R.id.news_button:
                    Intent intent4 = new Intent(getActivity(), NewsActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.contanctButton:
                    Intent intent5 = new Intent(getActivity(), ContanctActivity.class);
                    startActivity(intent5);
                    break;
                default:
                    break;
            }
        }
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}
