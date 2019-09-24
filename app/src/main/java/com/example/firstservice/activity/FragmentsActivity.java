package com.example.firstservice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.firstservice.R;
import com.example.firstservice.fragment.AnotherRightFragmnet;
import com.example.firstservice.fragment.RightFragment;

public class FragmentsActivity extends AppCompatActivity implements View.OnClickListener{
    private Button showRightButton;
    private Button switchFragmentButton;
    private TextView showText;
    private Fragment leftFragment;
    private Fragment rightFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        replaceFragment(new RightFragment());
        initView();
        buttonOnClick();
    }

    private void initView() {
        showRightButton=(Button)findViewById(R.id.show_right_fragment_button);
        switchFragmentButton=(Button)findViewById(R.id.switch_Fragment_Button);
        showText=(TextView)findViewById(R.id.right_fragment_content);
        leftFragment=getSupportFragmentManager().findFragmentById(R.id.left_fragment);
        rightFragment=getSupportFragmentManager().findFragmentById(R.id.right_fragment);
    }
    private void buttonOnClick(){
        showRightButton.setOnClickListener(this);
        switchFragmentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.show_right_fragment_button:
                if(showText.getVisibility()==View.INVISIBLE){
                    showText.setVisibility(View.VISIBLE);
                }else if(showText.getVisibility()==View.VISIBLE){
                    showText.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.switch_Fragment_Button:
                AnotherRightFragmnet anotherRightFragmnet = new AnotherRightFragmnet();
                replaceFragment(anotherRightFragmnet);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.fragment_container,fragment);
//        transaction.addToBackStack(null);//将碎片添加到返回栈
//        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_fragment_activity,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.start_right_fragment:

        }
        return super.onOptionsItemSelected(item);
    }
}
