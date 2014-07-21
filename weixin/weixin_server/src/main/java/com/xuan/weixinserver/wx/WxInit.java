/*
 * @(#)WxMessageInit.java    Created on 2014-6-17
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.wx.action.ActionMapping;
import com.xuan.weixinserver.wx.action.ActionSupport;
import com.xuan.weixinserver.wx.action.Plugin;
import com.xuan.weixinserver.wx.interceptor.Interceptor;

/**
 * 从配置文件中加载消息和消息处理器
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-17 下午5:20:37 $
 */
public class WxInit {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static WxInit instance = new WxInit();

    private WxInit() {
    }

    public static WxInit getInstance() {
        return instance;
    }

    /**
     * 解析wxmessage.xml文件，初始化消息和消息处理器对应类
     */
    public void init() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dom = domFactory.newDocumentBuilder();
            Document document = dom.parse(getClass().getClassLoader().getResourceAsStream("wx.xml"));

            parseMessagesMapping(document);
            parsePlugins(document);
            parseInterceptors(document);

            System.out.println("init wxmessage.xml success!--Action num[" + ActionMapping.getActionSize()
                    + "]。Interceptor num[" + ActionMapping.getInterceptorSize() + "]。Plugin num["
                    + ActionMapping.getPluginSize() + "]");
        }
        catch (Exception e) {
            log.error("解析wxmessage.xml错误，这导致微信服务器无法正常使用，请联系开发。原因：" + e.getMessage(), e);
        }
    }

    // 解析消息对应的处理器
    private void parseMessagesMapping(Document document) throws Exception {
        NodeList messagesList = document.getElementsByTagName("messages");

        for (int i = 0, n = messagesList.getLength(); i < n; i++) {
            Element messages = (Element) messagesList.item(i);
            NodeList messageList = messages.getElementsByTagName("message");
            for (int j = 0, m = messageList.getLength(); j < m; j++) {
                Element message = (Element) messageList.item(j);
                AbstractMessage am = (AbstractMessage) Class.forName(message.getAttribute("class")).newInstance();

                NodeList dealList = message.getElementsByTagName("action");
                if (dealList.getLength() > 0) {
                    Element deal = (Element) dealList.item(0);
                    ActionMapping.putAction(am.getCommand(), (ActionSupport) Class.forName(deal.getAttribute("class"))
                            .newInstance());
                }
            }
        }
    }

    // 解析启动器
    private void parsePlugins(Document document) throws Exception {
        NodeList pluginsList = document.getElementsByTagName("plugins");
        if (pluginsList.getLength() > 0) {
            Element plugins = (Element) pluginsList.item(0);
            NodeList pluginList = plugins.getElementsByTagName("plugin");
            for (int i = 0, n = pluginList.getLength(); i < n; i++) {
                Element plugin = (Element) pluginList.item(i);
                Plugin p = (Plugin) Class.forName(plugin.getAttribute("class")).newInstance();
                p.init();// 初始化
                ActionMapping.putPlugin(p);
            }
        }
    }

    // 解析声明的拦截器
    private void parseInterceptors(Document document) throws Exception {
        NodeList interceptorsList = document.getElementsByTagName("interceptors");
        if (interceptorsList.getLength() > 0) {
            Element interceptors = (Element) interceptorsList.item(0);
            NodeList interceptorList = interceptors.getElementsByTagName("interceptor");
            for (int i = 0, n = interceptorList.getLength(); i < n; i++) {
                Element interceptor = (Element) interceptorList.item(i);
                Interceptor mdi = (Interceptor) Class.forName(interceptor.getAttribute("class")).newInstance();
                mdi.init();// 初始化
                ActionMapping.putInterceptor(mdi);
            }
        }
    }

    public static void main(String[] args) {
        WxInit wxMessageInit = new WxInit();
        wxMessageInit.init();
    }

}
