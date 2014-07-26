package com.xuan.weixinserver.message;

import com.xuan.weixinserver.message.common.AbstractTypeMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 服务器端发送个客户端
 *
 * @author xuan
 * @version 创建时间：2014-7-26 下午3:02:02
 */
public class ToClientRespMessage extends AbstractTypeMessage{
	public ToClientRespMessage(){}

	public ToClientRespMessage(int type){
		super(type);
	}

	public ToClientRespMessage(int type ,String message){
		super(type, message);
	}

	@Override
	public int getCommand() {
		return CommandConstants.TOK_TO_CLIENT_RESP_MSG;
	}

}
