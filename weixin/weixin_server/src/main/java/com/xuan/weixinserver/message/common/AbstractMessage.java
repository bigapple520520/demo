/*
 * @(#)AbstractMessage.java    Created on 2012-3-5
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.message.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winupon.base.wpcf.core.WPCFPConstants;
import com.winupon.base.wpcf.util.SecurityUtils;
import com.xuan.weixinserver.message.help.FromWeixinHelpMessage;
import com.xuan.weixinserver.message.help.FromWeixinHelpRespMessage;
import com.xuan.weixinserver.message.help.GetLogMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 所有消息的基类
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-3-5 下午04:09:56 $
 */
public abstract class AbstractMessage {
    protected static final Logger log = LoggerFactory.getLogger(AbstractMessage.class);

    public static final String ISO8859_1 = "iso8859-1";
    public static final String UTF8 = "UTF-8";

    private static final int MAX_PACKAGE_SIZE = Math.min(10 * 1024, WPCFPConstants.MAX_PACKAGE_SIZE - 100);

    /**
     * 子类实现，获得命令类型
     *
     * @return
     */
    public abstract int getCommand();

    /**
     * 子类实现，转换成字节数组
     */
    public abstract byte[] getBytes();

    /**
     * 子类实现，把字节数组转换成消息
     *
     * @param body
     * @return
     */
    public abstract AbstractMessage valueOf(byte[] body);

    /**
     * 把字节数组转换成消息
     *
     * @param body
     * @return
     */
    public static AbstractMessage fromBytes(int command, byte[] body) {
        try {
            AbstractMessage m = messageInstancesMap.get(command);
            if (m == null) {
                log.error("received not used command:{}", command);
                return null;
            }

            return m.getClass().newInstance().valueOf(body);
        }
        catch (Exception e) {
            log.error("", e);
        }

        return null;
    }

    /**
     * 把大消息分割成小消息以便分开发送
     *
     * @param bigMessage
     * @return
     */
    public static AbstractMessage[] splitBigMessage(AbstractMessage bigMessage) {
        byte[] bs = bigMessage.getBytes();

        if (bs.length <= MAX_PACKAGE_SIZE) {
            return new AbstractMessage[] { bigMessage };
        }
        else {
            String md5 = SecurityUtils.encodeByMD5(bs);
            int from = 0, to = MAX_PACKAGE_SIZE, i = 0;
            int count = (int) Math.ceil((float) bs.length / MAX_PACKAGE_SIZE);
            List<SplitedMessage> list = new ArrayList<SplitedMessage>();
            while (from < bs.length) {
                int end = Math.min(to, bs.length);
                byte[] subArray = new byte[end - from];
                System.arraycopy(bs, from, subArray, 0, end - from);
                SplitedMessage m = new SplitedMessage(i++, count, bs.length, bigMessage.getCommand(), md5, subArray);
                list.add(m);
                from = to;
                to += MAX_PACKAGE_SIZE;
            }

            return list.toArray(new SplitedMessage[list.size()]);
        }
    }

    // //////////////////////////////////新加了消息，请在这里注册消息和消息处理类好么/////////////////////////////////////
    public static final Map<Integer, AbstractMessage> messageInstancesMap = new HashMap<Integer, AbstractMessage>();
    static {
        // 系统处理部分
        messageInstancesMap.put(CommandConstants.TOK_FROM_WEIXIN_HELP_MSG, new FromWeixinHelpMessage());
        messageInstancesMap.put(CommandConstants.TOK_FROM_WEIXIN_HELP_RESP_MSG, new FromWeixinHelpRespMessage());

        messageInstancesMap.put(CommandConstants.TOK_GET_LOG_MSG, new GetLogMessage());
        messageInstancesMap.put(CommandConstants.TOK_SPLITED, new SplitedMessage());
    }

}
