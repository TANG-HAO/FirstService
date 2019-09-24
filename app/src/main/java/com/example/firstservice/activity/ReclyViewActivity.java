package com.example.firstservice.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.adapter.FruitAdapter;
import com.example.firstservice.bean.Fruit;

import java.util.ArrayList;
import java.util.List;

public class ReclyViewActivity extends AppCompatActivity {
    private List<Fruit> fruits=new ArrayList<>();
    private Fruit fruit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        FruitAdapter fruitAdapter = new FruitAdapter(fruits);
        recyclerView.setAdapter(fruitAdapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            Fruit apple=new Fruit("apple",R.mipmap.ic_launcher);
            fruits.add(apple);
        }
    }
}
