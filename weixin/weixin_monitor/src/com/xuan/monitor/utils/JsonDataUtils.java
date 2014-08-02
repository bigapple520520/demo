package com.xuan.monitor.utils;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuan.utils.Validators;
import com.xuan.weixinserver.entity.Constants;
import com.xuan.weixinserver.entity.Result;
import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.entity.Table;
import com.xuan.weixinserver.entity.TableLine;

/**
 * Json串和ServiceData对象编码解码，Json的统一数据格式如下：<br>
 * {"success":"1","message":"{"serviceId":"111","tables":[{"tableName":"aaa", "tableData":[{"name":"xuan","age","111"}]}]}"}
 *
 * @author xuan
 * @version 创建时间：2014-7-29 下午7:43:37
 */
public abstract class JsonDataUtils {
	private static final Logger log = LoggerFactory.getLogger(JsonDataUtils.class);

	/**
	 * Json串解码成java对象
	 *
	 * @param jsonStr
	 * @return
	 */
	public static Result<ServiceData> decodeServiceDataFromJsonStr(String jsonStr) {
		if(Validators.isEmpty(jsonStr)){
			return new Result<ServiceData>(Constants.SUCCESS_0, "Json串是空的");
		}

		try {
			JSONObject object = new JSONObject(jsonStr);
			String success = JsonUtils.getString(object, Constants.JSON_TAG_SUCCESS);
			if(Constants.SUCCESS_1.equals(success)){
				//对方传送过来的的是成功的数据，表明message里面是有效的Json串数据
				JSONObject messageObj = object.getJSONObject(Constants.JSON_TAG_MESSAGE);
				String serviceId = JsonUtils.getString(messageObj, "serviceId");

				ServiceData serviceData = new ServiceData();
				serviceData.setServiceId(serviceId);
				serviceData.setLastSyncTime(new Date());

				JSONArray tableArray = messageObj.getJSONArray("tables");
				for(int i=0, n=tableArray.length(); i<n; i++){
					JSONObject tableObj = (JSONObject)tableArray.get(i);
					String tableName = JsonUtils.getString(tableObj, "tableName");

					Table table = new Table();
					table.setName(tableName);

					JSONArray tableLineArray = tableObj.getJSONArray("tableData");
					for(int j=0,m=tableLineArray.length(); j<m; j++){
						JSONObject tableLineObj = (JSONObject)tableLineArray.get(j);
						String key = JsonUtils.getString(tableLineObj, Constants.TABLE_COLUMN_1);
						String value = JsonUtils.getString(tableLineObj, Constants.TABLE_COLUMN_2);
						String qualityCode = JsonUtils.getString(tableLineObj, Constants.TABLE_COLUMN_3);
						String updateTime = JsonUtils.getString(tableLineObj, Constants.TABLE_COLUMN_4);

						TableLine tableLine = new TableLine();
						tableLine.put(Constants.TABLE_COLUMN_1, key);
						tableLine.put(Constants.TABLE_COLUMN_2, value);
						tableLine.put(Constants.TABLE_COLUMN_3, qualityCode);
						tableLine.put(Constants.TABLE_COLUMN_4, updateTime);
						table.add(key, tableLine);
					}

					serviceData.addTable(tableName, table);
				}


				return new Result<ServiceData>(Constants.SUCCESS_1, "数据解析成功", serviceData);
			}else{
				//对方传送过来的状态为失败的状态
				return new Result<ServiceData>(Constants.SUCCESS_0, JsonUtils.getString(object, Constants.JSON_TAG_MESSAGE), null);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return new Result<ServiceData>(Constants.SUCCESS_0,"解析异常，原因："+e.getMessage(), null);
		}
	}

	/**
	 * java对象编码成Json串
	 *
	 * @param serviceData
	 * @param serviceId
	 * @return
	 */
	public static String encodeJsonStrFromServiceData(ServiceData serviceData, String serviceId) {
		if(null == serviceData || Validators.isEmpty(serviceId)){
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

					JSONObject tableLineObj = new JSONObject();
					for(Entry<String, String> name2Value : tableLine.getMap().entrySet()){
						tableLineObj.put(name2Value.getKey(), name2Value.getValue());
						tableData.put(tableLineObj);
					}
				}

				tableObj.put("tableName", tableName);
				tableObj.put("tableData", tableData);

				tableArray.put(tableObj);

				messageObj.put("tables", tableArray);
			}

			retObj.put(Constants.JSON_TAG_SUCCESS, Constants.SUCCESS_1);
			retObj.put(Constants.JSON_TAG_MESSAGE, messageObj);

			return retObj.toString();
		} catch (Exception e) {
			log.error("解码ServiceData对象到Json串异常，原因："+e.getMessage(), e);
		}
		
		return null;
	}

}
