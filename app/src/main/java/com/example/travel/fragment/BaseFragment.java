package com.example.travel.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public abstract class BaseFragment extends Fragment {
    protected View mRootView;

    protected abstract int initLayout() ;
    protected abstract void initView();
    protected abstract void initData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(initLayout(),container,false);
            initView();
        }
        //unbinder = ButterKnife.bind(this,mRootView);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class c) {
        Intent in = new Intent(getActivity(), c);
        startActivity(in);
    }

    protected void saveStringToSp(String key, String value) {
        SharedPreferences sp = getActivity().getSharedPreferences("travel_ttit",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.apply();
    }

    protected String getStringFromSp(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("travel_ttit",MODE_PRIVATE);
        return sp.getString(key,"");
    }

}
