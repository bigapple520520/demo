package com.xuan.weixinserver.message;

import com.xuan.weixinserver.message.common.AbstractTypeMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 响应
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-21 下午4:13:18 $
 */
public class CommonRespMessage extends AbstractTypeMessage {

	public CommonRespMessage(){}

	public CommonRespMessage(int type){
		super(type);
	}

	public CommonRespMessage(int type ,String message){
		super(type, message);
	}

	@Override
	public int getCommand() {
		return CommandConstants.TOK_FROM_CLIENT_COMMON_RESP_MSG;
	}

}
