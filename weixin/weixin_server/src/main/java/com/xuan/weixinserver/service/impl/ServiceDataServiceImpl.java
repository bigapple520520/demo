/*
 * @(#)AccountExtServiceImpl.java    Created on 2013-5-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import net.zdsoft.keel.util.Validators;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.entity.Table;
import com.xuan.weixinserver.entity.TableLine;
import com.xuan.weixinserver.service.ServiceDataService;
import com.xuan.weixinserver.util.JsonUtils;


/**
 * 服务数据接口实现，数据的Json格式这样如下：<br>
 * {"success":"1","message":"{"serviceId":"111","tables":[{"tableName":"aaa", "tableData":[{"name":"xuan","age","111"}]}]}"}
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午11:02:12 $
 */
@Service
public class ServiceDataServiceImpl implements ServiceDataService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public ServiceData decodeServiceDataFromJsonStr(String jsonStr) {
		if(Validators.isEmpty(jsonStr)){
			return null;
		}

		try {
			JSONObject object = new JSONObject(jsonStr);
			String success = JsonUtils.getString(object, "success");
			if("1".equals(success)){
				JSONObject messageObj = object.getJSONObject("message");
				String serviceId = JsonUtils.getString(messageObj, "serviceId");

				ServiceData serviceData = new ServiceData();
				serviceData.setServiceId(serviceId);
				serviceData.setLastSyncTime(new Date());

				JSONArray tableArray = messageObj.getJSONArray("tables");
				for(int i=0,n=tableArray.length(); i<n; i++){
					JSONObject tableObj = (JSONObject)tableArray.get(i);
					String tableName = JsonUtils.getString(tableObj, "tableName");

					Table table = new Table();
					table.setName(tableName);

					JSONArray tableLineArray = tableObj.getJSONArray("tableData");
					for(int j=0,m=tableLineArray.length(); j<m; m++){
						JSONObject tableLineObj = (JSONObject)tableLineArray.get(j);
						String name = JsonUtils.getString(tableLineObj, "name");
						String value = JsonUtils.getString(tableLineObj, "value");
						String qualityCode = JsonUtils.getString(tableLineObj, "qualityCode");
						String updateTime = JsonUtils.getString(tableLineObj, "updateTime");

						TableLine tableLine = new TableLine();
						tableLine.put("name", name);
						tableLine.put("value", value);
						tableLine.put("qualityCode", qualityCode);
						tableLine.put("updateTime", updateTime);
						table.add("name", tableLine);
					}
					serviceData.addTable(tableName, table);
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}

		return null;
	}

	@Override
	public String encodeJsonStrFromServiceData(ServiceData serviceData, String serviceId) {
		if(null == serviceData){
			return null;
		}

		Map<String, Table> name2TableMap = serviceData.getMap();
		try {
			JSONObject retObj = new JSONObject();//整个返回对象

			JSONObject messageObj = new JSONObject();//message对象
			messageObj.put("serviceId", serviceId);
			for(Entry<String, Table> entry : name2TableMap.entrySet()){
				String tableName = entry.getKey();
				Table table = entry.getValue();

				JSONArray tableArray = new JSONArray();//表数组
				JSONObject tableObj = new JSONObject();//表
				JSONArray tableData = new JSONArray();//表数据

				Map<String, TableLine> name2TableLineMap = table.getMap();
				for(Entry<String, TableLine> name2TableLine : name2TableLineMap.entrySet()){
					TableLine tableLine = name2TableLine.getValue();

					for(Entry<String, String> name2Value : tableLine.getMap().entrySet()){
						JSONObject tableLineObj = new JSONObject();
						tableLineObj.put(name2Value.getKey(), name2Value.getValue());
						tableData.put(tableLineObj);
					}
				}

				tableObj.put("tableName", tableName);
				tableObj.put("tableData", tableData);

				tableArray.put(tableObj);

				messageObj.put("tables", tableArray);
			}

			retObj.put("success", "1");
			retObj.put("message", messageObj);

			return retObj.toString();
		} catch (Exception e) {
			log.error("解码ServiceData对象到Json串异常，原因："+e.getMessage(), e);
		}

		return null;
	}

}
