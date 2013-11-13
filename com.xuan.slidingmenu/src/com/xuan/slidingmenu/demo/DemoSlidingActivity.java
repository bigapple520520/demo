/* 
 * @(#)DemoSlidingActivity.java    Created on 2013-11-6
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.slidingmenu.demo;

import android.os.Bundle;

import com.xuan.slidingmenu.R;
import com.xuan.slidingmenu.lib.app.SlidingActivity;

public class DemoSlidingActivity extends SlidingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_content);
        setBehindContentView(R.layout.demo_behind);
    }

}
