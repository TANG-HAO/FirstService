package com.example.firstservice.fragment.contactFragment;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.firstservice.R;
import com.example.firstservice.base.BaseFragment;

public class ContanctRightFragment extends BaseFragment {
    private View view;
    public void refresh(String name, String phone) {
        TextView nameText=(TextView)view.findViewById(R.id.name);
        TextView phoneText=(TextView)view.findViewById(R.id.phone);
        nameText.setText(name);
        phoneText.setText(phone);
    }

    @Override
    public int setContentView() {
        return R.layout.contact_right_fragment_layout;
    }

    @Override
    public void initView(View view) {
        this.view=view;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
