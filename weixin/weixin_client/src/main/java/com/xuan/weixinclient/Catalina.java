/*
 * @(#)Catalina.java    Created on 2012-2-23
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinclient;

import net.zdsoft.keel.util.UUIDUtils;

import com.winupon.base.wpcf.util.SecurityUtils;
import com.xuan.weixinclient.client.MsgClient;


/**
 * 客户端程序启动
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-28 上午10:26:19 $
 */
public class Catalina {
    public static void main(String[] args) {
    	String loginId = UUIDUtils.newId();
    	System.out.println("loginId:"+loginId);

    	String token = SecurityUtils.encodeByMD5(loginId);
    	MsgClient.getInstance().init("192.168.0.103", 10000, loginId, token);
    }

}
