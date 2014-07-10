package com.xuan.demo.tabfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.winupon.andframe.bigapple.ioc.InjectView;
import com.xuan.demo.tabfragment.mcall.CallByFragmentListener;
import com.xuan.demo.tabfragment.mcall.CallFragmentActivity;
import com.xuan.tabframe_demo.R;

/**
 * 主界面，继承bigapple框架中的AnFragmentActivity可以使用注解了
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-25 下午6:55:29 $
 */
public class MainActivity extends CallFragmentActivity implements CallByFragmentListener {
    @InjectView(R.id.tabhost)
    private FragmentTabHost tabhost;

    @InjectView(R.id.tab)
    private RadioGroup tab;

    /**
     * 主界面所需要管理的Fragment片段
     */
    private final Class<?>[] fragments = { Fragment1.class, Fragment2.class, Fragment3.class, Fragment4.class };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
    }

    private void initView() {
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 设置子界面
        for (int i = 0, n = fragments.length; i < n; i++) {
            TabSpec tabSpec = tabhost.newTabSpec(String.valueOf(i)).setIndicator(String.valueOf(i));
            tabhost.addTab(tabSpec, fragments[i], null);
        }

        tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                case R.id.tabbtn1:
                    tabhost.setCurrentTab(0);
                    break;
                case R.id.tabbtn2:
                    tabhost.setCurrentTab(1);
                    break;
                case R.id.tabbtn3:
                    tabhost.setCurrentTab(2);
                    break;
                case R.id.tabbtn4:
                    tabhost.setCurrentTab(3);
                    break;
                default:
                    break;
                }
            }
        });

        tabhost.setCurrentTab(0);
    }

    @Override
    public void callByFragment(int command, Object... data) {
        // 这个方法会被Fragment调用，用来实现Fragment和Activity之间的通讯
        if (1 == command) {
            String msg = (String) data[0];
            Log.d("------------------", "command:" + command + "data:" + msg);
        }

        // 下面可以找到Fragment的实例调用他的Public方法，这样就可以和Fragment通信了
        callFragment("1", 1, "ffffffff");
    }

}
