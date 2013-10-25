/* 
 * @(#)ZxingConfig.java    Created on 2013-10-24
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.zxingdemo.utils;

import android.text.TextUtils;

/**
 * 正对ZxingUtils参数配置
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-24 上午11:33:56 $
 */
public class ZxingEncodeConfig {
    public static int DEFAUL_BITMAP_WIDTH = 300;
    public static int DEFAUL_BITMAP_HEIGHT = 300;
    public static boolean DEFAUL_IS_SAVE_FILE = false;
    public static int DEFAULT_BGCOLOR = 0xFFFFFFFF;// 白色
    public static int DEFAULT_COLOR = 0xFF000000;// 黑色

    private int bitmapWidth = DEFAUL_BITMAP_WIDTH;
    private int bitmapHeight = DEFAUL_BITMAP_HEIGHT;

    private boolean isSaveFile = DEFAUL_IS_SAVE_FILE;
    private String saveFileName;

    private int bgColor = DEFAULT_BGCOLOR;
    private int color = DEFAULT_COLOR;

    private String encoding;

    public ZxingEncodeConfig() {
    }

    public ZxingEncodeConfig(String saveFileName) {
        if (!TextUtils.isEmpty(saveFileName)) {
            this.isSaveFile = true;
            this.saveFileName = saveFileName;
        }
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public boolean isSaveFile() {
        return isSaveFile;
    }

    public void setSaveFile(boolean isSaveFile) {
        this.isSaveFile = isSaveFile;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

}
