package com.example.firstservice.activity.forceoffline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.firstservice.R;
import com.example.firstservice.activity.MainActivity;
import com.example.firstservice.base.BaseActivity;
import com.example.firstservice.base.BaseActivity1;

public class LoginActivity extends BaseActivity1 implements View.OnClickListener {
    private Button loginButton;
    private EditText accountText;
    private EditText passwordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton=(Button)findViewById(R.id.login);
        loginButton.setOnClickListener(this);

        accountText=(EditText) findViewById(R.id.account);
        passwordText=(EditText)findViewById(R.id.password);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                String account = accountText.getText().toString();
                String password = passwordText.getText().toString();
                if(!(account.isEmpty()&& password.isEmpty())){
                    if(account.equals("admin")&&password.equals("123456")){
                        Intent intent = new Intent(this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(this, "account or password unvalid", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "account or password is null", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
