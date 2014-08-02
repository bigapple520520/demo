/*
 * @(#)FromWeixinHelpRespMessage.java    Created on 2014-4-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.message.help;

import com.xuan.weixinserver.message.common.AbstractTypeMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 响应
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 下午4:25:30 $
 */
public class FromWeixinHelpRespMessage extends AbstractTypeMessage {
    public static final int TYPE_SUCCESS = 1;// 成功
    public static final int TYPE_MD5_ERROR = 2;// MD5校验错误
    public static final int TYPE_FAIL = 3;// 失败

    public FromWeixinHelpRespMessage() {
    }

    public FromWeixinHelpRespMessage(int type) {
        super(type);
    }

    public FromWeixinHelpRespMessage(int type, String message) {
        super(type, message);
    }

    @Override
    public int getCommand() {
        return CommandConstants.TOK_FROM_WEIXIN_HELP_RESP_MSG;
    }

}
