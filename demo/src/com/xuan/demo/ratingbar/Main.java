/* 
 * @(#)Main.java    Created on 2013-11-11
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.ratingbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;

import com.xuan.demo.R;

/**
 * 平分控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-11-11 下午1:30:21 $
 */
public class Main extends Activity {
    private RatingBar ratingbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingbar_demo);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
    }

}
