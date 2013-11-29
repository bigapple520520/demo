/* 
 * @(#)DemoSlidingActivity.java    Created on 2013-11-6
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.slidingmenu.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xuan.slidingmenu.R;
import com.xuan.slidingmenu.lib.SlidingMenu;
import com.xuan.slidingmenu.lib.app.SlidingActivity;

public class DemoSlidingActivity extends SlidingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_content);
        setBehindContentView(R.layout.demo_behind);
        getSlidingMenu().setSecondaryMenu(R.layout.demo_behind);
        getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
        getSlidingMenu().setBehindOffset(50);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        DemoSlidingActivity.this,
                        getSlidingMenu().getmViewBehind().getLeft() + "---"
                                + getSlidingMenu().getmViewBehind().getRight(), Toast.LENGTH_LONG).show();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        DemoSlidingActivity.this,
                        getSlidingMenu().getmViewAbove().getLeft() + "---"
                                + getSlidingMenu().getmViewAbove().getRight(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
