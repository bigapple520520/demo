/*
 * @(#)DealMessageInvoker.java    Created on 2013-7-22
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx.action;

import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.wx.interceptor.Interceptor;

/**
 * 策略模式中，执行策略的那个人（一直觉得策略模式和命令模式很像的有木有）<br />
 * 注意：每定义一个消息处理器，必须要在wxmessage.xml进行注册配置，不然无法进行匹配处理
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-7-22 上午9:50:57 $
 */
public class ActionInvoker {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private AbstractMessage message;// 待处理的消息
    private ActionSupport action;// 消息的业务逻辑处理
    private ActionContext context;// 上下文，可以用来传递参数

    private final Stack<Interceptor> interceptors = new Stack<Interceptor>();

    public ActionInvoker(AbstractMessage message, ActionContext context) {
        this.message = message;
        this.context = context;
    }

    /**
     * 执行策略
     *
     * @param abstractDealMessage
     */
    public void dealMessage() {
        if (null == message) {
            log.error("消息是空错误，检查是否注册了消息类");
            return;
        }

        action = ActionMapping.getAction(message.getCommand());
        if (null == action) {
            log.error("未注册消息处理类，请在wxmessage.xml里进行配置，消息类型是:" + message.getClass());
            return;
        }

        try {
            action = action.getClass().newInstance();
        }
        catch (Exception e) {
            log.error("反射构造消息处理类错误，原因：" + e, e);
        }

        if (null != action) {
            addInterceptors(ActionMapping.getInterceptors());// 设置该消息处理的拦截器

            try {
                invoke();// 以责任链模式执行拦截器，直到执行到最后执行消息处理器
            }
            catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public AbstractMessage getMessage() {
        return message;
    }

    public void setMessage(AbstractMessage message) {
        this.message = message;
    }

    public ActionSupport getAction() {
        return action;
    }

    public void setAction(ActionSupport action) {
        this.action = action;
    }

    public ActionContext getContext() {
        return context;
    }

    public void setContext(ActionContext context) {
        this.context = context;
    }

    // -----------------------设置当前消息的拦截器--------------------------------------------------------
    private void addInterceptors(CopyOnWriteArrayList<Interceptor> actionInterceptors) {
        int n = actionInterceptors.size();
        for (int i = n - 1; i >= 0; i--) {
            interceptors.push(actionInterceptors.get(i));
        }
    }

    // 执行处理消息的action
    private void invokeAction() throws Exception {
        action.dealMessage(message, context);
    }

    /**
     * 如果有拦截器执行拦截器，没有就执行处理消息的action
     *
     * @return
     * @throws Exception
     */
    public void invoke() throws Exception {
        if (interceptors.isEmpty()) {
            invokeAction();// 执行消息处理器
        }
        else {
            Interceptor interceptor = interceptors.pop();// 继续执行拦截器
            interceptor.getClass().newInstance().intercept(this);
        }
    }

}
