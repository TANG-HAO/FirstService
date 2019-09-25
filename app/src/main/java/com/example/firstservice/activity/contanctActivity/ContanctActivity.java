package com.example.firstservice.activity.contanctActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.adapter.contanctAdapter.ContanctAdapter;
import com.example.firstservice.base.BaseActivity;
import com.example.firstservice.bean.Contanctor;
import com.example.firstservice.fragment.contactFragment.ContanctLeftFragment;

import java.util.ArrayList;
import java.util.List;

public class ContanctActivity extends BaseActivity {
    private ContanctLeftFragment.ContanctAdapter contanctAdapter;
    private List<Contanctor> contanctorList = new ArrayList<>();
    private static final String TAG = "ContanctActivity";
    @Override
    public void initView() {

    }

    @Override
    public void initData() {
//        List<Contanctor> mlist=readContanctor();
//        contanctAdapter.
    }

//    private List<Contanctor> readContanctor() {
//        Cursor cursor=null;
//        try{
//
//            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//            if(cursor!=null){
//                while (cursor.moveToNext()){
//
//                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    Contanctor contanctor = new Contanctor(name, phone);
//                    contanctorList.add(contanctor);
//                }
//            }
//            contanctAdapter.notifyDataSetChanged();//todo
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if(cursor!=null){
//                cursor.close();
//            }
//        }
//
//        return contanctorList;
//    }

    @Override
    public void initListener() {

    }

    @Override
    public int getContentView() {
        return R.layout.contanct_main_activity_layout;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case 1:
//                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    readContanctor();
//                }else {
//                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
//                }
//                break;
//                default:
//        }
//
//    }
}
