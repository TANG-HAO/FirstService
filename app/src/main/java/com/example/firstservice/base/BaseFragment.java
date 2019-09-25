package com.example.firstservice.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setContentView(), container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        context = view.getContext();
        onViewCreated(view);
    }

    private void onViewCreated(View view) {
        initView(view);
        initData();
        initListener();
    }

    public  abstract  int setContentView();//接口设置加载的布局
    /**
     * 首次   必先初始化  view
     */
    public abstract void initView(View view);
    /**
     * 初始化数据   从网络 获取数据  数据添加到  view  等逻辑 工作
     */
    public abstract void initData();
    /**
     * 初始化  view listener 的工作
     */
    public abstract void initListener();

}
