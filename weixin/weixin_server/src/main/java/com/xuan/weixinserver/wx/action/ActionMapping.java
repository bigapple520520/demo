/*
 * @(#)MessageDealMap.java    Created on 2014-6-18
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx.action;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xuan.weixinserver.wx.interceptor.Interceptor;

/**
 * 解析wxmessage.xml后存放着处理类的映射关系
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-18 上午9:31:24 $
 */
public abstract class ActionMapping {
    public static final ConcurrentHashMap<Integer, ActionSupport> actionMap = new ConcurrentHashMap<Integer, ActionSupport>();
    public static final CopyOnWriteArrayList<Interceptor> interceptorList = new CopyOnWriteArrayList<Interceptor>();
    public static final CopyOnWriteArrayList<Plugin> pluginList = new CopyOnWriteArrayList<Plugin>();

    // ---------------------------------------action

    /**
     * 存入消息处理，在应用起来的时候使用
     *
     * @param command
     * @param messageDeal
     */
    public static void putAction(int command, ActionSupport action) {
        actionMap.put(command, action);
    }

    /**
     * 获取消息处理
     *
     * @param command
     * @return
     */
    public static ActionSupport getAction(int command) {
        return actionMap.get(command);
    }

    /**
     * 处理器个数
     *
     * @return
     */
    public static int getActionSize() {
        return actionMap.size();
    }

    // ---------------------------------------interceptor

    /**
     * 存入拦截器
     *
     * @param interceptor
     */
    public static void putInterceptor(Interceptor interceptor) {
        interceptorList.add(interceptor);
    }

    /**
     * 获取拦截器
     *
     * @param index
     * @return
     */
    public static Interceptor getInterceptor(int index) {
        return interceptorList.get(index);
    }

    /**
     * 获取拦截器的数量
     *
     * @return
     */
    public static int getInterceptorSize() {
        return interceptorList.size();
    }

    /**
     * 获取拦截器列表
     *
     * @return
     */
    public static CopyOnWriteArrayList<Interceptor> getInterceptors() {
        return interceptorList;
    }

    // --------------------------------------- plugin
    /**
     * 存入启动器
     *
     * @param interceptor
     */
    public static void putPlugin(Plugin plugin) {
        pluginList.add(plugin);
    }

    /**
     * 获取启动器
     *
     * @param index
     * @return
     */
    public static Plugin getPlugin(int index) {
        return pluginList.get(index);
    }

    /**
     * 获取启动器的数量
     *
     * @return
     */
    public static int getPluginSize() {
        return pluginList.size();
    }

}
