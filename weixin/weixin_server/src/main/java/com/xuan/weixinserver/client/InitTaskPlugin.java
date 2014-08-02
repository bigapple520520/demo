/*
 * @(#)InitTaskPlugin.java    Created on 2014-6-27
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.client;

import com.xuan.weixinserver.wx.action.Plugin;

/**
 * 初始化任务类
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-27 下午1:49:58 $
 */
public class InitTaskPlugin implements Plugin {

    @Override
    public void init() {
        System.out.println("init InitTaskPlugin success!");
    }

    @Override
    public void destroy() {
    }

}
