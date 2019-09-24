package com.example.firstservice.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.firstservice.R;

public class AnotherRightFragmnet extends Fragment {
    private static int count=0;
    private static final String TAG = "AnotherRightFragmnet";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.another_right_fragment, container, false);
        count++;
        Log.d(TAG,count+"=======");
        return view;
    }
}
