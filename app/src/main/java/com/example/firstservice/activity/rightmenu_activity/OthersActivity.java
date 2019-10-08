package com.example.firstservice.activity.rightmenu_activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.application.MyApplication;
import com.example.firstservice.base.BaseActivity1;
import com.example.firstservice.bean.Product;

import java.util.List;

public class OthersActivity extends BaseActivity1 {
    private RecyclerView others_recyclerView;
    private List<Product> products;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        initView();
    }

    private void initView() {
        others_recyclerView=(RecyclerView)findViewById(R.id.others_recycle_view);
    }
    private void  createData(){

    }
}
