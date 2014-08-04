/*
 * @(#)Catalina.java    Created on 2012-2-23
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinclient;

import java.util.concurrent.TimeUnit;

import com.winupon.base.wpcf.util.SecurityUtils;
import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.weixinclient.client.ApplicationConfigHelper;
import com.xuan.weixinclient.client.MsgClient;
import com.xuan.weixinclient.task.ScanDataTask;
import com.xuan.weixinclient.task.ScheduledTaskExecutorFactory;
import com.xuan.weixinserver.wx.WxInit;


/**
 * 客户端程序启动
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-28 上午10:26:19 $
 */
public class Catalina {
	public static final int INTERVAL = 10;

    public static void main(String[] args) {
    	WxInit.getInstance().init();

    	String username = "xuan";
    	String password = "123456";

    	//String loginId = SecurityUtils.encodeByMD5(username+password);

    	String loginId = UUIDUtils.createId();
    	String token = SecurityUtils.encodeByMD5(loginId);
    	String ip = ApplicationConfigHelper.getConnectIp();
    	int port = Integer.valueOf(ApplicationConfigHelper.getConnectPort());

    	System.out.println("开始连接服务器，连接ip["+ip+"],port["+port+"],loginId["+loginId+"]");

    	MsgClient.getInstance().init(ip, port, loginId, token);

    	if(MsgClient.getInstance().isLogined()){
    		System.out.println("服务器连接成功，每隔["+INTERVAL+"]秒，就会想中央服务器同步一次数据");
    		ScheduledTaskExecutorFactory.getScheduledTaskExecutor().scheduleWithFixedDelay(new ScanDataTask(), INTERVAL, INTERVAL, TimeUnit.SECONDS);
    	}
    }

}
