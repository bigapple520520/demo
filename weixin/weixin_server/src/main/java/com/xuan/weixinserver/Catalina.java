/*
 * @(#)Catalina.java    Created on 2012-2-23
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xuan.weixinserver.server.WeiXinServer;
import com.xuan.weixinserver.wx.WxInit;

/**
 * wpchat启动类
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-28 上午10:26:19 $
 */
public class Catalina {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        WeiXinServer server = (WeiXinServer) applicationContext.getBean("weiXinServer");
        server.startServer();
        WxInit.getInstance().init();// 启动微信配置
    }

}
