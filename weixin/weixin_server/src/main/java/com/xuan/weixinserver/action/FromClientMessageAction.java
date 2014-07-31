package com.xuan.weixinserver.action;

import java.util.Date;

import javax.annotation.Resource;

import net.zdsoft.keel.util.Validators;

import org.json.JSONObject;

import com.winupon.base.wpcf.util.DateUtils;
import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.weixinserver.client.CacheMap;
import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.entity.Table;
import com.xuan.weixinserver.entity.TableLine;
import com.xuan.weixinserver.message.FromClientMessage;
import com.xuan.weixinserver.message.FromClientRespMessage;
import com.xuan.weixinserver.message.ToClientMessage;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.service.ServiceDataService;
import com.xuan.weixinserver.util.DateUtil;
import com.xuan.weixinserver.util.JsonDataUtils;
import com.xuan.weixinserver.util.JsonUtils;
import com.xuan.weixinserver.wx.session.WxSessionManager;

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

		log.debug(message.getMessage());

		String ret = null;
		int type = message.getType();
		switch (type) {
		case FromClientMessage.ACTION_GET_DATA:
			ret = getData(type, message.getMessage());
			break;
		case FromClientMessage.ACTION_SYNC_DATA:
			ret = syncData(type, message.getMessage());
			break;
		case FromClientMessage.ACTION_MODIFY_DATA:
			ret = modifyData(type, message.getMessage());
			break;
		}

		if(null == ret){
			ret = JsonUtils.getError("逻辑没有响应");
		}
		responseMessage(new FromClientRespMessage(type, ret));
	}

	//监控程序修改数据
	private String modifyData(int type, String message){
		String retStr = null;
		try {
			/*解析数据*/
			JSONObject messageObj = new JSONObject(message);
			String serviceId = JsonUtils.getString(messageObj, "serviceId");
			String tableName = JsonUtils.getString(messageObj, "tableName");
			String queryKey = JsonUtils.getString(messageObj, "queryKey");//根据key定位到哪条数据
			String name = JsonUtils.getString(messageObj, "name");
			String value = JsonUtils.getString(messageObj, "value");

			/*数据校验*/
			if(Validators.isEmpty(serviceId)){
				return JsonUtils.getError("serviceId不能为空");
			}

			if(Validators.isEmpty(tableName)){
				return JsonUtils.getError("tableName不能为空");
			}

			if(Validators.isEmpty(queryKey)){
				return JsonUtils.getError("queryKey不能为空");
			}

			if(Validators.isEmpty(name)){
				return JsonUtils.getError("name不能为空");
			}

			if(Validators.isEmpty(value)){
				return JsonUtils.getError("value不能为空");
			}

			/*修改服务器端的数据*/
			ServiceData serviceData = CacheMap.getServiceData(serviceId);
			if(null == serviceData){
				return JsonUtils.getError("服务号"+serviceId+"]的数据不存在。请确认客户端是否有开启。");
			}

			Table table = serviceData.getTable(tableName);
			if(null == table){
				return JsonUtils.getError("服务号"+serviceId+"]，表名["+tableName+"]的数据不存在。请确认。");
			}

			TableLine tableLine = table.get(queryKey);
			if(null == tableLine){
				return JsonUtils.getError("服务号"+serviceId+"]，表名["+tableName+"]，变量名["+queryKey+"]，的数据不存在。请确认。");
			}

			String oldValue = tableLine.get(name);
			if(null == oldValue){
				return JsonUtils.getError("服务号"+serviceId+"]，表名["+tableName+"]，变量名["+queryKey+"]，字段["+name+"]，的数据不存在。请确认。");
			}

			tableLine.put(name, value);
			tableLine.put("updateTime", DateUtils.date2StringBySecond(new Date()));
			serviceData.setDirty(true);

			/*把数据同步到客户端*/
			String loginId = serviceData.getLoginId();
			if(!WxSessionManager.getInstance().hasSession(loginId)){
				return JsonUtils.getError("客户端程序不在线了，请确认一下客户端程序是否有启动");
			}

			ToClientMessage toClientMessage = new ToClientMessage(ToClientMessage.ACTION_SYNC_DATA,JsonDataUtils.encodeJsonStrFromServiceData(serviceData, serviceId));
			sendMessage(loginId, UUIDUtils.createId(), toClientMessage);

			/*返回成功的状态给修改着*/
     		retStr = JsonUtils.getMessage("修改成功，请刷新数据");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			retStr = JsonUtils.getError("数据处理异常，原因：" + e.getMessage());
		}

		return retStr;
	}

	//处理客户端过来的数据同步
	private String syncData(int type, String message){
		String retStr = null;
		try {
			/*解码Json数据成对象*/
			ServiceData serviceData = serviceDataService.decodeServiceDataFromJsonStr(message);
			if(null == serviceData){
				retStr = JsonUtils.getError("解析后serviceData数据是空");
			}else{
				/*判断服务器端是否还有数据未同步的*/
				if(serviceData.isDirty()){
					if(DateUtil.addSeconds(new Date(), -40).after(serviceData.getLastSyncTime())){
						serviceData.setDirty(false);//超过40秒数据还是未同步过就设置已同步
					}

					retStr = JsonUtils.getError("服务器有数据在忘客户端同步中");
				}else{
					/*解码成功后替换服务器的数据*/
					serviceData.setLoginId(getActionContext().getLoginId());//客户端登录id
					CacheMap.putServiceData(serviceData.getServiceId(), serviceData);
					retStr = JsonUtils.getMessage("数据同步成功");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			retStr = JsonUtils.getError("数据处理异常，原因：" + e.getMessage());
		}

		return retStr;
	}

	//处理监控程序过来的请求，查询数据状态
	private String getData(int type, String message){
		try {
			JSONObject object = new JSONObject(message);
			String serviceId = JsonUtils.getString(object, "serviceId");
			String tableName = JsonUtils.getString(object, "tableName");

			/*数据校验*/
			if(Validators.isEmpty(serviceId)){
				return JsonUtils.getError("serviceId不能为空");
			}

			if(Validators.isEmpty(tableName)){
				return JsonUtils.getError("tableName不能为空");
			}

			 /*获取服务器缓存的数据*/
			ServiceData serviceData = CacheMap.getServiceData(serviceId);
			if(null == serviceData){
				return JsonUtils.getError("对应serviceId["+serviceId+"]服务的数据不存在，请确认服务号，或者确认该服务器有无连接到中央服务器");
			}

			/*把数据编码成Json格式，方便传输*/
			String retStr = serviceDataService.encodeJsonStrFromServiceData(serviceData, serviceId);
			if(Validators.isEmpty(retStr)){
				return JsonUtils.getError("获取数据失败，请看下有没有日志报错");
			}

			/*返回数据*/
			return retStr;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return JsonUtils.getError("数据处理异常，原因：" + e.getMessage());
		}
	}

}
