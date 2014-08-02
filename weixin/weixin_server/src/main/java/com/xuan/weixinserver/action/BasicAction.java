/*
 * @(#)AbstractDealMessage.java    Created on 2013-7-18
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.action;

import org.apache.mina.core.future.WriteFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.server.WeiXinServerSendHelper;
import com.xuan.weixinserver.wx.action.ActionSupport;

/**
 * 处理消息的基类，从MVC角度考虑，整个类相当于web框架编码中的BasicAction
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-18 下午12:36:44 $
 */
public abstract class BasicAction extends ActionSupport {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 发送响应消息，回应
     *
     * @param abstractMessage
     * @return
     */
    protected boolean responseMessage(AbstractMessage abstractMessage) {
        return sendMessage(getActionContext().getLoginId(), getActionContext().getMessageId(), abstractMessage);
    }

    /**
     * 发送消息操作
     *
     * @param loginId
     * @param messageId
     * @param abstractMessage
     * @return
     */
    protected boolean sendMessage(String loginId, String messageId, AbstractMessage abstractMessage) {
        boolean isSuccess = false;
        int failTime = 0;
        try {
            while (failTime < 3) {// 尝试发送三次，失败则不再发送
                WriteFuture future = WeiXinServerSendHelper.sendMessage(loginId, messageId, abstractMessage);
                future.awaitUninterruptibly();
                if (future.isDone() && future.isWritten()) {
                    isSuccess = true;
                    break;
                }
                failTime++;
            }
        }
        catch (Exception e) {
            log.error("发送消息异常，原因：" + e.getMessage(), e);
        }

        return isSuccess;
    }

}
