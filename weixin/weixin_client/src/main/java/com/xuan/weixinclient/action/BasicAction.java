/*
 * @(#)AbstractDealMessage.java    Created on 2013-7-18
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinclient.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuan.weixinserver.message.common.AbstractMessage;
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
    	return false;
    }



}
