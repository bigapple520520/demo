package com.xuan.weixinclient.service.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xuan.weixinclient.service.DataService;

/**
 * 获取数据全量表数据的Service实现，Json的数据结构如下：<br>
 *{"success":"1","message":"{"serviceId":"111","tables":[{"tableName":"aaa", "tableData":[{"name":"xuan","age","111"}]}]}"}
 *
 * @author xuan
 * @version 创建时间：2014-7-25 下午4:27:31
 */
@Service
public class DataServiceImpl implements DataService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String getAllDataJsonStr() {
		try {
			//整个返回
			JSONObject ret = new JSONObject();

			//返回文本
			JSONObject message = new JSONObject();

			//多张表
			JSONArray tables = new JSONArray();

			//一张表
			JSONObject table = new JSONObject();

			//多条数据
			JSONArray tableLinesData = new JSONArray();

			//一条数据
			JSONObject tableLineData1 = new JSONObject();
			tableLineData1.put("name", "xuan");
			tableLineData1.put("age", "19");

			//一条数据
			JSONObject tableLineData2 = new JSONObject();
			tableLineData2.put("name", "anan");
			tableLineData2.put("age", "21");

			tableLinesData.put(tableLineData1);
			tableLinesData.put(tableLineData2);

			table.put("tableName", "aaa");
			table.put("tableData", tableLinesData);

			tables.put(table);

			message.put("serviceId", "111");
			message.put("tables", tables);

			ret.put("type", "1");
			ret.put("message", message);

			return ret.toString();
		} catch (Exception e) {
			log.error("扫描全部获取数据异常，原因：" + e.getMessage(),e);
		}

		return null;
	}

}
