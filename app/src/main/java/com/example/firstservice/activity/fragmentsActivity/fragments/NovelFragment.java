package com.example.firstservice.activity.fragmentsActivity.fragments;
import	java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.firstservice.R;
import com.example.firstservice.activity.fragmentsActivity.fragments.NovelFragments.BookFragment;
import com.example.firstservice.activity.fragmentsActivity.fragments.NovelFragments.MyFragment;
import com.example.firstservice.activity.fragmentsActivity.fragments.NovelFragments.RankFragment;
import com.example.firstservice.activity.fragmentsActivity.fragments.NovelFragments.SortFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class NovelFragment extends androidx.fragment.app.Fragment {
    private View view;
    private BottomNavigationView bottomNavigation;
    private ViewPager viewPager;
    private List<String> titles = new ArrayList<String> ();
    private List<Fragment> views = new ArrayList<Fragment> ();
    private Fragment booksfragment,sortFragment,rankFragment,myFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.novel_fragment,container,false);
        initView();
        setContent();
        return view;
    }

    /**
     * 设置viewPager的内容
     */
    private void setContent() {
        titles.add("书架");
        titles.add("排行");
        titles.add("分类");
        titles.add("我的");

        views.add(booksfragment);
        views.add(sortFragment);
        views.add(rankFragment);
        views.add(myFragment);
    }

    /**
     * 实例化控件
     */
    private void initView() {
        bottomNavigation=(BottomNavigationView)view.findViewById(R.id.novel_bottom_navagion);
        viewPager=(ViewPager)view.findViewById(R.id.nov_container);

        booksfragment=new BookFragment();
        sortFragment=new SortFragment();
        rankFragment=new RankFragment();
        myFragment=new MyFragment();
    }

    public class NovelFragmentPageHelpAdapter extends FragmentPagerAdapter {

        public NovelFragmentPageHelpAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return views.get(position);
        }

        @Override
        public int getCount() {
            return views.size();
        }

    }
}
