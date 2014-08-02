/*
 * @(#)SplitedMessage.java    Created on 2012-9-20
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.message.common;

import java.nio.ByteBuffer;

import com.xuan.weixinserver.util.CommandConstants;

/**
 * 分割的小消息
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-9-20 下午04:33:30 $
 */
public class SplitedMessage extends AbstractMessage {
    private int sequence;// 序列号，从0开始

    private int splitedNum;// 分割的包的总个数
    private int originalLength;// 被分割前的包的字节长度
    private int originalCommand;// 原先的消息的command
    private String md5;// 原消息的字节的md5

    private int bodyLength;// body的长度
    private byte[] body;

    public SplitedMessage() {
    }

    public SplitedMessage(int sequence, int splitedNum, int originalLength, int originalCommand, String md5, byte[] body) {
        this.sequence = sequence;
        this.splitedNum = splitedNum;
        this.originalLength = originalLength;
        this.originalCommand = originalCommand;
        this.md5 = md5;
        this.body = body;
        this.bodyLength = body.length;
    }

    @Override
    public int getCommand() {
        return CommandConstants.TOK_SPLITED;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buf = ByteBuffer.allocate(body.length + 56);
        buf.putInt(sequence);
        buf.putInt(splitedNum);
        buf.putInt(originalLength);
        buf.putInt(originalCommand);
        if (sequence == 0) {
            buf.put(com.winupon.base.wpcf.util.StringUtils.getBytes(md5, ISO8859_1));
        }
        buf.putInt(body.length);
        buf.put(body);
        buf.flip();
        byte[] bs = new byte[buf.remaining()];
        buf.get(bs);
        return bs;
    }

    @Override
    public AbstractMessage valueOf(byte[] body) {
        ByteBuffer buf = ByteBuffer.wrap(body);
        sequence = buf.getInt();
        splitedNum = buf.getInt();
        originalLength = buf.getInt();
        originalCommand = buf.getInt();
        if (sequence == 0) {
            byte[] bs = new byte[32];
            buf.get(bs);
            this.md5 = com.winupon.base.wpcf.util.StringUtils.newString(bs, ISO8859_1);
        }
        bodyLength = buf.getInt();
        byte[] bs = new byte[bodyLength];
        buf.get(bs);
        this.body = bs;

        return this;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getOriginalCommand() {
        return originalCommand;
    }

    public void setOriginalCommand(int originalCommand) {
        this.originalCommand = originalCommand;
    }

    public int getOriginalLength() {
        return originalLength;
    }

    public void setOriginalLength(int originalLength) {
        this.originalLength = originalLength;
    }

    public int getSplitedNum() {
        return splitedNum;
    }

    public void setSplitedNum(int splitedNum) {
        this.splitedNum = splitedNum;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
