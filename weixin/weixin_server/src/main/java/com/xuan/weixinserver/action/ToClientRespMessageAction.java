package com.xuan.weixinserver.action;

import org.json.JSONObject;

import com.xuan.weixinserver.client.CacheMap;
import com.xuan.weixinserver.entity.Constants;
import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.message.ToClientMessage;
import com.xuan.weixinserver.message.ToClientRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.util.JsonUtils;

/**
 * 来自客户端的响应处理
 *
 * @author xuan
 * @version 创建时间：2014-7-26 下午3:42:52
 */
public class ToClientRespMessageAction extends BasicAction {

	@Override
	protected void doDealMessage(AbstractMessage abstractMessage) {
		ToClientRespMessage message = (ToClientRespMessage)abstractMessage;
		switch (message.getType()) {
		case ToClientMessage.ACTION_SYNC_DATA:
			dealSyncDataResponse(message.getType(), message.getMessage());
			break;
		}
	}

	//服务器向客户端同步数据，客户端处理完后进行响应的消息处理
	private void dealSyncDataResponse(int type, String message){
		try {
			JSONObject object = new JSONObject(message);

			if(Constants.SUCCESS_1.equals(object.getJSONObject("success"))){
				JSONObject messageObj = object.getJSONObject("message");
				String serviceId = JsonUtils.getString(messageObj, "serviceId");
				ServiceData serviceData = CacheMap.getServiceData(serviceId);
				if(null != serviceData){
					serviceData.setDirty(false);
				}
			}else{
				log.error("服务器同步数据到客户端，客户端返回状态错误，返回串："+message);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
