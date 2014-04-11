/* 
 * @(#)StartApplication.java    Created on 2013-6-24
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo;

import android.app.Application;
import android.content.res.Configuration;

import com.xuan.demo.db.DBHelper;

/**
 * 程序的入口处
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-6-24 上午9:32:42 $
 */
public class StartApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化数据库版本号和数据库名
        DBHelper.init(1, "demo_db");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
