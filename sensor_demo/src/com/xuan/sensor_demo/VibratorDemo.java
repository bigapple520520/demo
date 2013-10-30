/* 
 * @(#)VibratorDemo.java    Created on 2013-10-30
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.sensor_demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;

/**
 * Vibrator测试
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-30 上午9:26:24 $
 */
public class VibratorDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // vibrator.vibrate(5000);
        vibrator.vibrate(new long[] { 1000, 1000, 5000, 5000 }, 2);
    }

}
