package com.xuan.weixinserver.message;

import com.xuan.weixinserver.message.common.AbstractTypeMessage;
import com.xuan.weixinserver.util.CommandConstants;

/**
 * 客户端发送给服务器端的消息
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-21 下午4:13:18 $
 */
public class FromClientMessage extends AbstractTypeMessage{
	public static final int ACTION_GET_DATA = 1;//监控程序向中央服务器获取数据
	public static final int ACTION_SYNC_DATA = 2;//客户端程序向中央服务器推送数据，保持数据的同步
	public static final int ACTION_MODIFY_DATA = 3;//监控程序向中央服务器请求修改数据

	public FromClientMessage(){}

	public FromClientMessage(int type){
		super(type);
	}

	public FromClientMessage(int type ,String message){
		super(type, message);
	}

	@Override
	public int getCommand() {
		return CommandConstants.TOK_FROM_CLIENT_MSG;
	}

}
