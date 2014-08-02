/*
 * @(#)PlugIn.java    Created on 2014-6-27
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx.action;

/**
 * PlugIn 接口，实现了这个接口才可以配置成 PlugIn。适用于需要随着微信启动而一起启动的类
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-27 下午1:41:12 $
 */
public interface Plugin {

    /**
     * 随着微信启动而启动
     *
     * @param actionContext
     */
    void init();

    /**
     * 随着微信关闭而销毁（暂未实现）
     */
    void destroy();

}
