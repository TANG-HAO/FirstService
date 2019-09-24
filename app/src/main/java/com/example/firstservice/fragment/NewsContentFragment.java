package com.example.firstservice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firstservice.R;

public class NewsContentFragment extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.news_content_frag,container,false);
        return view;//返回视图
    }
    //刷新content的内容
    public void refresh(String newsTitle,String newsContent){
        View viewViewById = view.findViewById(R.id.visibility_layout);//从视图中获取组件(布局)
        viewViewById.setVisibility(View.VISIBLE);
        TextView tilteText = (TextView) view.findViewById(R.id.news_title);//获取组件
        TextView contentText = (TextView) view.findViewById(R.id.news_content);
        tilteText.setText(newsTitle);//设置值
        contentText.setText(newsContent);
    }
}
