package com.xuan.weixinserver.message;

import com.xuan.weixinserver.message.common.AbstractTypeMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 通用消息发送
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-21 下午4:13:18 $
 */
public class CommonMessage extends  AbstractTypeMessage{

	public CommonMessage(){}

	public CommonMessage(int type){
		super(type);
	}

	public CommonMessage(int type ,String message){
		super(type, message);
	}

	@Override
	public int getCommand() {
		return CommandConstants.TOK_FROM_CLIENT_COMMON_MSG;
	}

}
