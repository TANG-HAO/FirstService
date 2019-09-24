package com.example.firstservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.firstservice.R;
import com.example.firstservice.fragment.NewsContentFragment;

public class NewsContentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content_acv);
        String title = (String) getIntent().getExtras().get("title");
        String content = (String) getIntent().getExtras().get("content");
        NewsContentFragment fragmentById =(NewsContentFragment)getSupportFragmentManager().
                findFragmentById(R.id.news_content_fragment);
        fragmentById.refresh(title,content);


    }
    public static  void actionStart(Context context,String title, String content){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putString("content",content);
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
