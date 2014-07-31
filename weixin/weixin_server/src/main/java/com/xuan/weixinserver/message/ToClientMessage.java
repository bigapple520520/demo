package com.xuan.weixinserver.message;

import com.xuan.weixinserver.message.common.AbstractTypeMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 服务器端发送给客户端
 *
 * @author xuan
 * @version 创建时间：2014-7-26 下午3:01:47
 */
public class ToClientMessage extends AbstractTypeMessage{
	public static final int ACTION_SYNC_DATA = 1;//服务器端向客户端通知修改数据

	public ToClientMessage(){}

	public ToClientMessage(int type){
		super(type);
	}

	public ToClientMessage(int type ,String message){
		super(type, message);
	}

	@Override
	public int getCommand() {
		return CommandConstants.TOK_TO_CLIENT_MSG;
	}

}
