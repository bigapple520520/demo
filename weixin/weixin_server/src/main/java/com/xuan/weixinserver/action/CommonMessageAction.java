package com.xuan.weixinserver.action;

import com.xuan.weixinserver.message.CommonMessage;
import com.xuan.weixinserver.message.CommonRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;


/**
 * 通用消息处理
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 下午4:31:44 $
 */
public class CommonMessageAction extends BasicAction {
	@Override
	protected void doDealMessage(AbstractMessage abstractMessage) {
		CommonMessage message = (CommonMessage)abstractMessage;
		System.out.println("收到：" + message.getMessage());

		responseMessage(new CommonRespMessage(1, "数据已同步成功"));
	}

}
