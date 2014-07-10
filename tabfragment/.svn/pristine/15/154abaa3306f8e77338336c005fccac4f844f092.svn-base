/* 
 * @(#)BaseFragmentActivity.java    Created on 2014-7-10
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.tabfragment.mcall;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.winupon.andframe.bigapple.ioc.app.AnFragmentActivity;

/**
 * FragmentActivity基类，实现了与Fragment之间的通讯机制
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午3:07:21 $
 */
public class CallFragmentActivity extends AnFragmentActivity implements CallByFragmentListener {
    @Override
    public void callByFragment(int command, Object... data) {
        // 如果这个Activity需要被Fragment调用就复写这个类
    }

    /**
     * 子类可以调用该方法调用指定的Fragment
     * 
     * @param tag
     * @param command
     * @param data
     */
    protected void callFragment(String tag, int command, Object... data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (null == fragment) {
            Log.d("------------------------", "没有这个Fragment");
        }
        else {
            CallByActivityListener callByActivityListener = (CallByActivityListener) fragment;
            callByActivityListener.callByActivity(command, data);
        }
    }
}
