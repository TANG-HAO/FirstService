package com.example.firstservice.activity.contanctActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.firstservice.R;
import com.example.firstservice.base.BaseActivity;
import com.example.firstservice.bean.Contanctor;
import com.example.firstservice.fragment.contactFragment.ContanctRightFragment;

import java.io.Serializable;

public class ContanctRightActivity extends BaseActivity {



    public static void  actionStart(Context context, String name, String phone){
        Contanctor contanctor = new Contanctor(name, phone);
        Bundle bundle = new Bundle();
        //bundle.putString("name",name);
        //bundle.putString("phone",phone);
        bundle.putSerializable("contanctor",contanctor);
        Intent intent = new Intent(context, ContanctRightActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Contanctor contanctor =(Contanctor) getIntent().getExtras().getSerializable("contanctor");
        ContanctRightFragment fragmentById = (ContanctRightFragment) getSupportFragmentManager().findFragmentById(R.id.contanct_right_fragment);
        fragmentById.refresh(contanctor.getName(),contanctor.getPhone());

    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentView() {
        return R.layout.contact_right_activity;
    }
}
