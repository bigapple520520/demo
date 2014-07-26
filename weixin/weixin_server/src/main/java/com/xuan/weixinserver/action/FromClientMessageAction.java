package com.xuan.weixinserver.action;

import org.json.JSONObject;

import com.xuan.weixinserver.client.CacheMap;
import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.message.FromClientMessage;
import com.xuan.weixinserver.message.FromClientRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.util.JsonUtils;

/**
 * 来自客户端的消息处理
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 下午4:31:44 $
 */
public class FromClientMessageAction extends BasicAction {
	@Override
	protected void doDealMessage(AbstractMessage abstractMessage) {
		FromClientMessage message = (FromClientMessage)abstractMessage;

		String ret = null;
		int type = message.getType();
		switch (type) {
		case 1:
			ret = deal1(type, message.getMessage());
			break;
		default:
			break;
		}

		if(null == ret){
			ret = JsonUtils.getError("逻辑没有响应");
		}
		responseMessage(new FromClientRespMessage(type, ret));
	}

	private String deal1(int type, String message){
		try {
			JSONObject object = new JSONObject(message);
			String serviceId = JsonUtils.getString(object, "serviceId");
			String table = JsonUtils.getString(object, "table");

			ServiceData serviceData = CacheMap.getServiceData(serviceId);
			if(null == serviceData){
				return JsonUtils.getError("对应serviceId["+serviceId+"]服务的数据不存在，请确认服务号，或者确认该服务器有无连接到中央服务器");
			}

			if(null == table){
				return JsonUtils.getError("对应serviceId["+serviceId+"]的服务table["+table+"]的数据不存在，请确认表名是否正确，或者确认该服务器有无连接到中央服务器");
			}

			return JsonUtils.getMessage("XXXXXXXXX");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return JsonUtils.getError("数据处理异常，原因：" + e.getMessage());
		}
	}

}
