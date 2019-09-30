package com.example.firstservice.activity.rightmenu_activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.firstservice.R;
import com.example.firstservice.activity.rightmenu_activity.itemhelper.PersonRecycleItemTouchHelper;
import com.example.firstservice.adapter.personAdapter.PersonAdapter;
import com.example.firstservice.base.BaseActivity1;
import com.example.firstservice.bean.MyPerson;

import java.util.ArrayList;
import java.util.List;

public class PersonActivity extends BaseActivity1 {
    private RecyclerView personRecycle;
    private Toolbar toolbar;
    private List<MyPerson> mlist=new ArrayList<>();
    private PersonAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        personRecycle=(RecyclerView)findViewById(R.id.activity_person_recycle_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        personRecycle.setLayoutManager(gridLayoutManager);
        mlist=createList();
        adapter = new PersonAdapter(mlist);
        personRecycle.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new PersonRecycleItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(personRecycle);

        showSwipRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.per_main,menu);
        return  true;
    }
    public List<MyPerson> createList(){

        for (int i = 0; i <20; i++) {
            MyPerson myPerson = new MyPerson("沛东",R.drawable.haizei);
            mlist.add(myPerson);
        }
        return  mlist;
    }
    public void showSwipRefresh(){
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swip_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPerson();
            }
        });
    }

    private void refreshPerson() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mlist=createList();//列表数据的刷新
                        adapter.notifyDataSetChanged();
                        //adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(PersonActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }


}
