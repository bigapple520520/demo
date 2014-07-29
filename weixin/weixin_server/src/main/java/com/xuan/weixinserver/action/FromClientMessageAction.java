package com.xuan.weixinserver.action;

import javax.annotation.Resource;

import net.zdsoft.keel.util.Validators;

import org.json.JSONObject;

import com.xuan.weixinserver.client.CacheMap;
import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.message.FromClientMessage;
import com.xuan.weixinserver.message.FromClientRespMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.service.ServiceDataService;
import com.xuan.weixinserver.util.Constants;
import com.xuan.weixinserver.util.JsonUtils;

/**
 * 来自客户端的消息处理
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 下午4:31:44 $
 */
public class FromClientMessageAction extends BasicAction {
	@Resource
	private ServiceDataService serviceDataService;

	@Override
	protected void doDealMessage(AbstractMessage abstractMessage) {
		FromClientMessage message = (FromClientMessage)abstractMessage;

		String ret = null;
		int type = message.getType();
		switch (type) {
		case Constants.ACTION_GET_DATA:
			ret = deal1(type, message.getMessage());
			break;
		case Constants.ACTION_SYNC_DATA:
			ret = deal2(type, message.getMessage());
			break;
		}

		if(null == ret){
			ret = JsonUtils.getError("逻辑没有响应");
		}
		responseMessage(new FromClientRespMessage(type, ret));
	}

	//处理客户端过来的数据同步
	private String deal2(int type, String message){
		log.debug(message);

		String retStr = null;
		try {
			/*解码Json数据成对象*/
			ServiceData serviceData = serviceDataService.decodeServiceDataFromJsonStr(message);
			if(null == serviceData){
				retStr = JsonUtils.getError("数据未处理");
			}else{
				/*解码成功后替换服务器的数据*/
				CacheMap.putServiceData(serviceData.getServiceId(), serviceData);
				retStr = JsonUtils.getMessage("数据同步成功");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			retStr = JsonUtils.getError("数据处理异常，原因：" + e.getMessage());
		}

		return retStr;
	}

	//处理监控程序过来的请求，查询数据状态
	private String deal1(int type, String message){
		try {
			JSONObject object = new JSONObject(message);
			String serviceId = JsonUtils.getString(object, "serviceId");
			String tableName = JsonUtils.getString(object, "tableName");

			/*数据校验*/
			if(Validators.isEmpty(serviceId)){
				return JsonUtils.getError("serviceId不能为空");
			}

			if(Validators.isEmpty(tableName)){
				return JsonUtils.getError("table不能为空");
			}

			 /*获取服务器缓存的数据*/
			ServiceData serviceData = CacheMap.getServiceData(serviceId);
			if(null == serviceData){
				return JsonUtils.getError("对应serviceId["+serviceId+"]服务的数据不存在，请确认服务号，或者确认该服务器有无连接到中央服务器");
			}

			/*把数据编码成Json格式，方便传输*/
			String retStr = serviceDataService.encodeJsonStrFromServiceData(serviceData, serviceId);
			if(Validators.isEmpty(retStr)){
				return JsonUtils.getError("获取数据失败，请看下有没有日报报错");
			}

			/*返回数据*/
			return JsonUtils.getMessage(retStr);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return JsonUtils.getError("数据处理异常，原因：" + e.getMessage());
		}
	}

}
