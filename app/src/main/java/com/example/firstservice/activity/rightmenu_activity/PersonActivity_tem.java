package com.example.firstservice.activity.rightmenu_activity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

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
import com.example.firstservice.adapter.personAdapter.PersonAdapter_tem;
import com.example.firstservice.base.BaseActivity1;
import com.example.firstservice.bean.MyPerson;
import com.example.firstservice.listener.RecycleOnscrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PersonActivity_tem extends BaseActivity1 {
    private RecyclerView personRecycle;
    private Toolbar toolbar;
    private List<MyPerson> mlist=new ArrayList<>();
    private PersonAdapter_tem adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int page=0;//上拉加载使用
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
        adapter = new PersonAdapter_tem(mlist);
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
    public ArrayList<MyPerson> createList(){
         ArrayList<MyPerson> persons=new ArrayList<>();
        for (int i = 0; i <20; i++) {
            MyPerson myPerson = new MyPerson("沛东"+i,R.drawable.haizei);
            persons.add(myPerson);
        }
        return  persons;
    }

    /**
     * 下拉刷新和上拉刷新
     */
    public void showSwipRefresh(){
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swip_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPerson();
            }
        });
        //上拉到底刷新
        personRecycle.addOnScrollListener(new RecycleOnscrollListener() {
            @Override
            protected void onLoadMore() {
                adapter.setLoadState(PersonAdapter_tem.LOADING);//显示加载进度
                if(mlist.size()<=60){
                    //模拟获取网络数据
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getData(page);//加载数据
                                    adapter.setLoadState(PersonAdapter_tem.LOAD_FINISH);
                                }
                            });
                        }
                    },3000);

                }else {
                    //显示加载到底
                    Toast.makeText(PersonActivity_tem.this, "资源加载完成", Toast.LENGTH_SHORT).show();
                    page=0;
                    adapter.setLoadState(PersonAdapter_tem.LOAD_END);
                }
            }
        });
    }

    /**
     * 加载数据
     * @param page
     */
    private void getData(int page) {
        int ind=20*page;
        ArrayList<MyPerson> persons=new ArrayList<>();
        for (int i = ind; i < 20 + ind; i++) {
            MyPerson myPerson = new MyPerson("沛东"+i,R.drawable.haizei);
            persons.add(myPerson);
        }
        if(page==0){
            mlist.addAll(persons);
        }else {
            adapter.AddFooterItem(persons);
        }
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
                        //模拟数据刷新
                        adapter.AddHeaderItem(createList());
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(PersonActivity_tem.this, "刷新成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }




}
