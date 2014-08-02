/*
 * @(#)MessageInterceptor.java    Created on 2014-6-19
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx.interceptor;

import com.xuan.weixinserver.wx.action.ActionInvoker;


/**
 * 在调用消息处理器之前的拦截器。类似web的拦截器<br>
 * 使用方法：继承他，然后在wxmessage.xml中配置，这样所有调用消息处理器之前都会调用这个拦截器
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-19 下午6:07:03 $
 */
public interface Interceptor {

    /**
     * 拦截器的初始化，在微信启动解析wxmessage.xml中被初始化
     */
    void init();

    /**
     * 微信关闭时销毁（暂未实现）
     */
    void destroy();

    /**
     * 拦截方法。
     *
     * @param actionInvoker
     * @return
     * @throws Exception
     */
    void intercept(ActionInvoker actionInvoker) throws Exception;

}
