package com.example.firstservice.activity.fragmentsActivity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.firstservice.R;
import com.example.firstservice.activity.fragmentsActivity.fragments.MusicFragment;
import com.example.firstservice.activity.fragmentsActivity.fragments.NovelFragment;
import com.example.firstservice.base.BaseActivity1;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FragmentsActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<String> mTitleList=new ArrayList<>();//页卡标题集合
    private ArrayList<Fragment> mViewList=new ArrayList<>();//页卡视图集合
    private Fragment musicFragment,novelFragment;//碎片
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        initBefore();//提前实例化
        addContent();//添加内容
        initPage();//实现功能
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        //initTab();//初始化tableLayout
    }



    private void initPage() {

        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()) );


    }

    /**
     * 为viewPage添加内容和标题
     */
    private void addContent() {
        mTitleList.add("music");
        mTitleList.add("novel");

        mViewList.add(musicFragment);
        mViewList.add(novelFragment);
    }

    /**
     * 实例化
     */
    private void initBefore() {
        viewPager=(ViewPager)findViewById(R.id.fragments_viewPager);
        tabLayout=(TabLayout)findViewById(R.id.fragments_tabLayout);

        musicFragment=new MusicFragment();
        novelFragment=new NovelFragment();

    }

    @Override
    public void onClick(View view) {

    }


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {


        public MyFragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mViewList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
