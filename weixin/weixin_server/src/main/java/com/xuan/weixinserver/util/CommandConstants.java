/*
 * @(#)CommandConstants.java    Created on 2012-3-2
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.util;

/**
 * 一些消息的命令
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-28 上午10:25:25 $
 */
public abstract class CommandConstants {
    // 系统辅助消息
    public static final int TOK_FROM_WEIXIN_HELP_MSG = 0x00009001;// 系统帮助消息
    public static final int TOK_FROM_WEIXIN_HELP_RESP_MSG = 0x80009001;

    public static final int TOK_GET_LOG_MSG = 0x00009002;// 获取日志消息

    // 命令常量定义，请求与响应配对使用，值相差0x80000000
    public static final int TOK_FROM_CLIENT_COMMON_MSG = 0x00001100; // 普通消息
    public static final int TOK_FROM_CLIENT_COMMON_RESP_MSG = 0x80001100; //响应

    public static final int TOK_SPLITED = 0x00005000; // 表示被分割的消息

}
