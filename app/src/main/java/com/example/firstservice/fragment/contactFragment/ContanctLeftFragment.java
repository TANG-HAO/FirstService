package com.example.firstservice.fragment.contactFragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstservice.R;
import com.example.firstservice.activity.contanctActivity.ContanctRightActivity;
import com.example.firstservice.base.BaseFragment;
import com.example.firstservice.bean.Contanctor;

import java.util.ArrayList;
import java.util.List;

public class ContanctLeftFragment extends BaseFragment {
    private boolean isTwoPane;
    private ContanctLeftFragment.ContanctAdapter contanctAdapter;
    private List<Contanctor> contanctorList = new ArrayList<>();
    private static final String TAG = "ContanctLeftFragment";
    @Override
    public int setContentView() {
        return R.layout.contact_left_frag;
    }

    @Override
    public void initView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.contact_left_fragment_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        contanctAdapter=new ContanctLeftFragment.ContanctAdapter(contanctorList);
        Log.d(TAG,"List<Contanctor>=====>"+contanctorList.size());
        recyclerView.setAdapter(contanctAdapter);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{

            readContanctor();

            Log.d(TAG,"List<Contanctor>=====>"+contanctorList.size());
        }
    }
    private List<Contanctor> readContanctor() {
        Cursor cursor=null;
        try{

            cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if(cursor!=null){
                while (cursor.moveToNext()){

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Contanctor contanctor = new Contanctor(name, phone);
                    contanctorList.add(contanctor);
                }
            }
            contanctAdapter.notifyDataSetChanged();//todo

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return contanctorList;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){//todo 不起作用？？？
                    askForPermission();
                }else {
                    if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        readContanctor();
                    }else {
                        Toast.makeText(getActivity(), "Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
        }

    }

    private void askForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Permission!");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.contanct_right_fragment)!=null){
            isTwoPane=true;
        }else {
            isTwoPane=false;
        }
    }

    public  class ContanctAdapter extends RecyclerView.Adapter<ContanctAdapter.ViewHolder> {
        private final List<Contanctor> mlist;


        public ContanctAdapter(List<Contanctor> contanctorList) {
            this.mlist=contanctorList;
        }

        @NonNull
        @Override
        public ContanctAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contanct_left_fragment_recycleview_item, parent, false);
            final ContanctAdapter.ViewHolder viewHolder = new ContanctAdapter.ViewHolder(view);
            //对列表项添加事件监听
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contanctor contanctor = mlist.get(viewHolder.getAdapterPosition());//获取支配器中当前viewHolder的数组位置
                    Log.d(TAG, "onClick: ====>"+isTwoPane);
                    if(isTwoPane){
                        //刷新右边fragment的数据
                        ContanctRightFragment contanctRightFragment=(ContanctRightFragment)getFragmentManager().findFragmentById(R.id.contanct_right_fragment);
                        contanctRightFragment.refresh(contanctor.getName(),contanctor.getPhone());
                    }else {
                        ContanctRightActivity.actionStart(getActivity(),contanctor.getName(),contanctor.getPhone());
                    }


                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Contanctor contanctor = mlist.get(position);
            holder.nameText.setText(contanctor.getName());
        }


        @Override
        public int getItemCount() {
            return mlist.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {//根据列表项的最外层布局，实例化其中的控件
            private TextView nameText;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameText=(TextView)itemView.findViewById(R.id.contanct_left_fragment_recycleview_item_nametext);
            }
        }
    }
}
