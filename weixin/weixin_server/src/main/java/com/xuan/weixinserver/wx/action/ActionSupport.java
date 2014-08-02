/*
 * @(#)MessageDealSupport.java    Created on 2014-6-18
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx.action;

import com.xuan.weixinserver.message.common.AbstractMessage;


/**
 * 类似ActionSupport，MVC中的控制层
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-18 下午2:07:25 $
 */
public abstract class ActionSupport {
    private ActionContext context;// 处理器需要用到的参数打包在这里

    /**
     * 处理消息方法
     *
     * @param abstractMessage
     * @param params
     */
    public void dealMessage(AbstractMessage abstractMessage, ActionContext actionContext) {
        this.context = actionContext;
        doDealMessage(abstractMessage);
    }

    /**
     * 子类需要实现的
     *
     * @param abstractMessage
     * @return 返回消息如果是null不做处理，如果是一个消息就发送
     */
    protected abstract void doDealMessage(AbstractMessage abstractMessage);

    public ActionContext getActionContext() {
        return context;
    }

}
