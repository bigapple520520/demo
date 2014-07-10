package com.xuan.demo.tabfragment;

import android.app.Activity;
import android.os.Bundle;

import com.xuan.tabframe_demo.R;

/**
 * 普通的Activity
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午1:42:54 $
 */
public class OtherActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
    }

}
