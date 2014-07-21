/*
 * @(#)GetLogMessage.java    Created on 2014-5-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.message.help;

import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.util.CommandConstants;


/**
 * 获取日志消息
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-15 下午2:55:12 $
 */
public class GetLogMessage extends AbstractMessage {
    private byte[] bytes;

    public GetLogMessage() {
    }

    public GetLogMessage(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public int getCommand() {
        return CommandConstants.TOK_GET_LOG_MSG;
    }

    @Override
    public AbstractMessage valueOf(byte[] body) {
        this.bytes = body;
        return this;
    }

}
