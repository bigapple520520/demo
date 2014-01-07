/* 
 * @(#)Main.java    Created on 2013-11-11
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.ratingbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.winupon.andframe.bigapple.utils.ToastUtils;
import com.xuan.demo.R;

/**
 * 平分控件
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-11-11 下午1:30:21 $
 */
public class RatingBarMain extends Activity {
    private RatingBar ratingbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratingbar_demo);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);

        ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ToastUtils.displayTextLong(RatingBarMain.this, String.valueOf(rating));
            }
        });
    }

}
