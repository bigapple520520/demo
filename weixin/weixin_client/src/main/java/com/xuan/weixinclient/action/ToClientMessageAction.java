package com.xuan.weixinclient.action;

import com.xuan.weixinclient.utils.JsonUtils;
import com.xuan.weixinserver.message.ToClientMessage;
import com.xuan.weixinserver.message.ToClientRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;

/**
 * 服务器过来的消息
 *
 * @author xuan
 * @version 创建时间：2014-7-31 下午1:58:44
 */
public class ToClientMessageAction extends BasicAction {

	@Override
	protected void doDealMessage(AbstractMessage abstractMessage) {
		ToClientMessage message = (ToClientMessage)abstractMessage;

		switch (message.getType()) {
		case ToClientMessage.ACTION_SYNC_DATA:
			dealSyncDataFromServer(message.getType(), message.getMessage());
			break;
		}
	}

	//处理来自服务器端的数据同步
	private void dealSyncDataFromServer(int type, String message){
		/*读取*/

		responseMessage(new ToClientRespMessage(type, JsonUtils.getMessage("同步成功")));
	}

}
