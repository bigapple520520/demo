/*
 * @(#)WeiXinServerSendHelper.java    Created on 2013-5-20
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.server;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.future.WriteFuture;

import com.xuan.weixinserver.message.common.AbstractMessage;

/**
 * 各种发送消息
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-20 下午6:20:32 $
 */
public abstract class WeiXinServerSendHelper {
    /**
     * 发送消息
     *
     * @param username
     * @param messageId
     * @param m
     * @return
     */
    public static WriteFuture sendMessage(String loginId, String messageId, AbstractMessage message) {
        return WeiXinServer.getWpcfServer().sendMessage(loginId, messageId, message.getCommand(), message.getBytes());
    }

    /**
     * 发送被分割的多个消息,任何一个发送失败则认为发送失败
     *
     * @param abstractMessages
     * @return
     */
    public static boolean sendSplitedMessages(String loginId, String messageId,
            AbstractMessage... abstractMessages) {
        if (StringUtils.isEmpty(messageId)) {
            messageId = com.winupon.base.wpcf.util.UUIDUtils.createId();
        }

        for (AbstractMessage message : abstractMessages) {
            WriteFuture future = WeiXinServerSendHelper.sendMessage(loginId, messageId, message);
            future.awaitUninterruptibly();
            if (!future.isDone() || !future.isWritten()) {
                return false;// 一个发送失败则不再发送
            }
        }

        return true;
    }

}
