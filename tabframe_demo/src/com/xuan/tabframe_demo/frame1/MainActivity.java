package com.xuan.tabframe_demo.frame1;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.winupon.andframe.bigapple.ioc.InjectView;
import com.winupon.andframe.bigapple.ioc.app.AnFragmentActivity;
import com.xuan.tabframe_demo.R;

/**
 * 主界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-25 下午6:55:29 $
 */
public class MainActivity extends AnFragmentActivity {
    @InjectView(R.id.tabhost)
    private FragmentTabHost tabhost;

    @InjectView(R.id.tab)
    private RadioGroup tab;

    private final Class<?>[] fragments = { Fragment1.class, Fragment2.class, Fragment3.class, Fragment4.class };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        // tabhost = (FragmentTabHost) findViewById(R.id.tabhost);
        // tab = (RadioGroup) findViewById(R.id.tab);

        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 设置子界面
        int count = fragments.length;
        for (int i = 0; i < count; i++) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
