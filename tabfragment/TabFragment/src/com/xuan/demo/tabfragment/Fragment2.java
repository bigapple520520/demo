package com.xuan.demo.tabfragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.demo.tabfragment.mcall.CallFragment;
import com.xuan.tabframe_demo.R;

/**
 * 子界面2，当从子界面到这个界面时，生命周期是这样的<br>
 * 
 * ----------------------Fragment1:onPause<br>
 * ----------------------Fragment1:onStop<br>
 * ----------------------Fragment1:onDestroyView<br>
 * ----------------------Fragment2:onAttach<br>
 * ----------------------Fragment2:onCreate<br>
 * ----------------------Fragment2:onCreateView<br>
 * ----------------------Fragment2:onActivityCreated<br>
 * ----------------------Fragment2:onStart<br>
 * ----------------------Fragment2:onResume
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午1:33:51 $
 */
public class Fragment2 extends CallFragment {

    @Override
    public void onAttach(Activity activity) {
        Log.d("-------------------------", "----------------------Fragment2:onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("-------------------------", "-----------------------Fragment2:onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("-------------------------", "-------------------------Fragment2:onCreateView");
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d("-------------------------", "----------------------Fragment2:onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d("-------------------------", "----------------------Fragment2:onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("-------------------------", "----------------------Fragment2:onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("-------------------------", "----------------------Fragment2:onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("-------------------------", "----------------------Fragment2:onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("-------------------------", "----------------------Fragment2:onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("-------------------------", "----------------------Fragment2:onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("-------------------------", "----------------------Fragment2:onDetach");
        super.onDetach();
    }

}
