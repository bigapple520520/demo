/* 
 * @(#)ZxingConfig.java    Created on 2013-10-24
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.zxingutils.lib.config;

/**
 * 正对ZxingUtils参数配置
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-24 上午11:33:56 $
 */
public class ZEConfig {
    private int bitmapWidth = 300;
    private int bitmapHeight = 300;

    private String saveFileName;// 存储二维码文件地址

    private int bgColor = 0xFFFFFFFF;// 白色
    private int color = 0xFF000000;// 黑色

    private String encoding;

    public ZEConfig() {
    }

    public ZEConfig(String saveFileName) {
        this.saveFileName = saveFileName;
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
