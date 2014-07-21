/*
 * @(#)ParamsForDeal.java    Created on 2014-1-3
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx.action;

import java.util.HashMap;

/**
 * 消息处理Action的上下文，可以用来传递上下文参数
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-3 上午10:31:00 $
 */
public class ActionContext {
    private String loginId;
    private String messageId;

    public ActionContext() {
    }

    public ActionContext(String loginId, String messageId) {
        this.loginId = loginId;
        this.messageId = messageId;
    }

    private final HashMap<String, Object> parameters = new HashMap<String, Object>();

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // -------------------------传递参数------------------------------------------------------
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

}
