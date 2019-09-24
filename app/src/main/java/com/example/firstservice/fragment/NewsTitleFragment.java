package com.example.firstservice.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.activity.NewsActivity;
import com.example.firstservice.activity.NewsContentActivity;
import com.example.firstservice.bean.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewsTitleFragment extends Fragment {
    private boolean isTwoPane;//创建属性判断是否为单双页

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_tilte_frag, container, false);
        RecyclerView newsRecycleView = (RecyclerView) view.findViewById(R.id.news_title_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newsRecycleView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(getNews());
        newsRecycleView.setAdapter(adapter);
        return view;
    }

    private List<News> getNews() {
        ArrayList<News> newsList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            News news = new News();
            news.setTitle("This is new Title"+i);
            news.setContent(getRandomLengthContent("This is new Title"+i+"."));
            newsList.add(news);
        }
        return newsList;
    }

    private String getRandomLengthContent(String s) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_layout)!=null){
            isTwoPane=true;
        }else {
            isTwoPane=false;
        }
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
        private List<News> newsList;

        public NewsAdapter(List<News> news) {
            this.newsList = news;
        }

        class ViewHolder extends RecyclerView.ViewHolder {//每个item的外层布局
            private TextView newsTitleText;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                newsTitleText=(TextView)itemView.findViewById(R.id.news_item_title); //todo 创建列表项布局文件 R.layout.news_item
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news = newsList.get(viewHolder.getAdapterPosition());//根据当前外层布局，获取适配器的位置
                    if(isTwoPane){
                        //双页模式，刷新NewsContentFragmnet中的内容
                        //从碎片管理器中找到NewsContentFragment碎片
                        NewsContentFragment contentFragment = (NewsContentFragment) getFragmentManager()
                                .findFragmentById(R.id.news_content_fragment);
                        //调用碎片的refresh方法刷新内容
                        contentFragment.refresh(news.getTitle(),news.getContent());
                    }else {
                        //单页的话,直接启动NewsContentActivity
                        NewsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent());//获取当碎片所在的activity
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            News news = newsList.get(position);//recycleView的列表项塞入数据
            holder.newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }



    }


}
