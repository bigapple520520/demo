package com.xuan.tabframe_demo.frame1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.xuan.tabframe_demo.R;

public class Fragment1 extends Fragment {

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

        View parentView = inflater.inflate(R.layout.fragment1, container, false);
        Button button2 = (Button) parentView.findViewById(R.id.button2);

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OtherActivity.class);
                startActivity(intent);
            }
        });

        return parentView;
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
