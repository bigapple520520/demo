package com.xuan.demo.tabfragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.winupon.andframe.bigapple.ioc.InjectView;
import com.winupon.andframe.bigapple.ioc.ViewUtils;
import com.xuan.demo.tabfragment.mcall.CallFragment;
import com.xuan.tabframe_demo.R;

/**
 * 子界面1，当界面第一次启动的时候，生命周期如下<br>
 * 
 * ----------------------Fragment1:onAttach <br>
 * ----------------------Fragment1:onCreate <br>
 * ----------------------Fragment1:onCreateView <br>
 * ----------------------Fragment1:onActivityCreated <br>
 * ----------------------Fragment1:onStart <br>
 * ----------------------Fragment1:onResume<br>
 * 
 * 当他的宿主Activity被销毁时发生的生命周期如下<br>
 * 
 * ----------------------Fragment1:onPause<br>
 * ----------------------Fragment1:onStop<br>
 * ----------------------Fragment1:onDestroyView<br>
 * ----------------------Fragment1:onDestroy<br>
 * ----------------------Fragment1:onDetach<br>
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午1:33:39 $
 */
public class Fragment1 extends CallFragment {
    @InjectView(R.id.button1)
    private Button button1;

    @InjectView(R.id.button2)
    private Button button2;

    /**
     * 可以给宿主Activity调用
     */
    @Override
    public void callByActivity(int command, Object... data) {
        if (1 == command) {
            String msg = (String) data[0];
            Log.d("------------------", "command:" + command + "data:" + msg);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d("-------------------------", "----------------------Fragment1:onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("-------------------------", "-----------------------Fragment1:onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("-------------------------", "-------------------------Fragment1:onCreateView");

        View parentView = inflater.inflate(R.layout.fragment1, container, false);
        ViewUtils.inject(this, parentView);// 可以这样使用bigapple里面的注解注入

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(1, "我调用了，怎么了");
            }
        });

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
        Log.d("-------------------------", "----------------------Fragment1:onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d("-------------------------", "----------------------Fragment1:onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("-------------------------", "----------------------Fragment1:onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("-------------------------", "----------------------Fragment1:onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("-------------------------", "----------------------Fragment1:onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("-------------------------", "----------------------Fragment1:onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("-------------------------", "----------------------Fragment1:onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("-------------------------", "----------------------Fragment1:onDetach");
        super.onDetach();
    }

}
