package com.xuan.tabframe_demo.frame1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.tabframe_demo.R;

public class Fragment2 extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        Log.e("-------------------------", "----------------------Fragment2:onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("-------------------------", "-----------------------Fragment2:onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("-------------------------", "-------------------------Fragment2:onCreateView");
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e("-------------------------", "----------------------Fragment2:onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e("-------------------------", "----------------------Fragment2:onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("-------------------------", "----------------------Fragment2:onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("-------------------------", "----------------------Fragment2:onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("-------------------------", "----------------------Fragment2:onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.e("-------------------------", "----------------------Fragment2:onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e("-------------------------", "----------------------Fragment2:onAttach");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e("-------------------------", "----------------------Fragment2:onDetach");
        super.onDetach();
    }

}
