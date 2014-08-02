/*
 * @(#)FromHelpClearCacheMessage.java    Created on 2014-4-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.message.help;

import java.nio.ByteBuffer;

import com.winupon.base.wpcf.util.StringUtils;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 系统帮助工具消息
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 下午4:13:18 $
 */
public class FromWeixinHelpMessage extends AbstractMessage {
    public static final int TYPE_CLEAR_CACHE = 1;
    public static final int TYPE_ONLINE_NUM = 2;
    public static final int TYPE_MESSAGE_SENT_SIZE = 3;
    public static final int TYPE_JVM_MEMORY = 4;
    public static final int TYPE_FRONCLIENTMSGMESSAGE_SUB_SIZE = 5;
    public static final int TYPE_WPDY_FRONCLIENTMSGMESSAGE_SUB_SIZE = 6;
    public static final int TYPE_GET_CURRENT_LOG = 7;
    public static final int TYPE_SET_LOG_LEVEL = 8;

    // 处理逻辑状态：1、清理所有缓存；2、统计在线人数；3、当前发送的消息但还没收到响应的消息数
    // 4、查看虚拟机内存使用状态，5、FromClientMsgMessage发送未响应数，6、WpdyFromClientMsgMessage发送未响应数
    // 7、获取当前日志，8、设置日志级别
    private int type;
    private String md5;// 校验值
    private int messageLength;
    private String message;

    public FromWeixinHelpMessage() {
    }

    public FromWeixinHelpMessage(int type, String md5) {
        this.type = type;
        this.md5 = md5;
    }

    public FromWeixinHelpMessage(int type, String md5, String message) {
        this.type = type;
        this.md5 = md5;
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

            ByteBuffer buf = ByteBuffer.allocate(messageLength + 40);
            buf.putInt(type);
            buf.put(StringUtils.getBytes(md5, UTF8));

            buf.putInt(messageLength);
            if (messageLength > 0) {
                buf.put(StringUtils.getBytes(message, UTF8));
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

        byte[] bs = new byte[32];
        buf.get(bs);
        md5 = StringUtils.newString(bs, UTF8);

        messageLength = buf.getInt();
        if (messageLength > 0) {
            bs = new byte[messageLength];
            buf.get(bs);
            message = StringUtils.newString(bs, UTF8);
        }

        return this;
    }

    @Override
    public int getCommand() {
        return CommandConstants.TOK_FROM_WEIXIN_HELP_MSG;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
