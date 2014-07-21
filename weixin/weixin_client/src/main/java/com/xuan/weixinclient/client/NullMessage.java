package com.xuan.weixinclient.client;

import com.xuan.weixinserver.message.common.AbstractMessage;


/**
 * 在接受回复消息之前，会先用这个空白消息占用，能收到回复消息后会被回复消息替换掉
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-6-13 下午03:19:11 $
 */
public class NullMessage extends AbstractMessage {
    public static final NullMessage NULL = new NullMessage();

    private NullMessage() {
    }

    @Override
    public int getCommand() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        return null;
    }

    @Override
    public AbstractMessage valueOf(byte[] body) {
        return null;
    }

}
