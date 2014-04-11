/* 
 * @(#)TextUtils.java    Created on 2013-4-16
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.textviewhtml;

/**
 * html拼接的一些工具类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-4-16 下午6:12:08 $
 */
public abstract class HtmlTextUtils {

    /**
     * 生成图片的html
     * 
     * @return
     */
    public static String getImgHtml(String str) {
        return "<img src='#'>".replace("#", str);
    }

    /**
     * 生成图片的html
     * 
     * @param resid
     * @return
     */
    public static String getImgHtml(int resid) {
        return getImgHtml(String.valueOf(resid));
    }

}
