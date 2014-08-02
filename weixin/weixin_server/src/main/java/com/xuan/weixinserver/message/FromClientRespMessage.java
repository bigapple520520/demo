package com.xuan.weixinserver.message;

import com.xuan.weixinserver.message.common.AbstractTypeMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 客户端发送给服务器端的消息，响应
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-21 下午4:13:18 $
 */
public class FromClientRespMessage extends AbstractTypeMessage {
	public FromClientRespMessage(){}

	public FromClientRespMessage(int type){
		super(type);
	}

	public FromClientRespMessage(int type ,String message){
		super(type, message);
	}

	@Override
	public int getCommand() {
		return CommandConstants.TOK_FROM_CLIENT_RESP_MSG;
	}

}
