/*
 * @(#)AbstractTypeMessage.java    Created on 2013-11-15
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.message.common;

import java.nio.ByteBuffer;

/**
 * 适用于只需要返回一个type的消息或者附带一个message提示的基类。比较通用
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-11-15 下午8:04:11 $
 */
public abstract  class AbstractTypeMessage extends AbstractMessage {
    private int type;

    private int messageLength;
    private String message;

    public AbstractTypeMessage() {
    }

    public AbstractTypeMessage(int type) {
        this.type = type;
    }

    public AbstractTypeMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public byte[] getBytes() {
        try {
            byte[] messageBytes = null;
            if (null != message) {
                messageBytes = message.getBytes(UTF8);
                messageLength = messageBytes.length;
            }

            ByteBuffer buf = ByteBuffer.allocate(messageLength + 8);

            buf.putInt(type);
            buf.putInt(messageLength);
            if (messageLength > 0) {
                buf.put(com.winupon.base.wpcf.util.StringUtils.getBytes(message, UTF8));
            }

            buf.flip();
            byte[] bs = new byte[buf.remaining()];
            buf.get(bs);
            return bs;
        }
        catch (Exception e) {
            log.error("", e);
        }

        // if exception
        return null;
    }

    @Override
    public AbstractMessage valueOf(byte[] body) {
        ByteBuffer buf = ByteBuffer.wrap(body);

        type = buf.getInt();
        messageLength = buf.getInt();
        if (messageLength > 0) {
            byte[] bs = new byte[messageLength];
            buf.get(bs);
            message = com.winupon.base.wpcf.util.StringUtils.newString(bs, UTF8);
        }

        return this;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
