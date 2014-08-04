package com.xuan.weixinclient.action;

import com.xuan.weixinclient.client.ApplicationConfigHelper;
import com.xuan.weixinclient.utils.ExcelUtils;
import com.xuan.weixinclient.utils.JsonDataUtils;
import com.xuan.weixinclient.utils.JsonUtils;
import com.xuan.weixinserver.entity.Constants;
import com.xuan.weixinserver.entity.Result;
import com.xuan.weixinserver.entity.ServiceData;
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
		Result<ServiceData> result = JsonDataUtils.decodeServiceDataFromJsonStr(message);
		if(Constants.SUCCESS_1.equals(result.getSuccess())){
			ServiceData serviceData = result.getData();
			ExcelUtils.writeToFile(ApplicationConfigHelper.getDataFilePath(), serviceData);
			responseMessage(new ToClientRespMessage(type, JsonUtils.getMessage("同步成功")));
		}else{
			responseMessage(new ToClientRespMessage(type, JsonUtils.getError("同步失败")));
		}
	}

}
