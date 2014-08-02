/*
 * @(#)Catalina.java    Created on 2014-4-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.monitor;

import com.xuan.monitor.frame.MainFrame;


/**
 * 启动入口
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 上午11:33:05 $
 */
public class Catalina {
    public static void main(String[] args) {
//    	String username = "anan";
//    	String password = "123456";
//
//    	try {
//    		String loginId = SecurityUtils.encodeByMD5(username+password);
//        	String token = SecurityUtils.encodeByMD5(loginId);
//        	MsgClient.getInstance().init("127.0.0.1", 10000, loginId, token);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        new MainFrame();
    }

}
