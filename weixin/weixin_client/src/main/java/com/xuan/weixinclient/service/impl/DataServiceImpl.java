package com.xuan.weixinclient.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xuan.weixinclient.service.DataService;
import com.xuan.weixinclient.utils.ExcelUtils;
import com.xuan.weixinclient.utils.JsonDataUtils;
import com.xuan.weixinserver.entity.ServiceData;

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
			ServiceData serviceData = ExcelUtils.loadFromfile("D://111.xls");

			if(null == serviceData){
				return null;
			}

			return JsonDataUtils.encodeJsonStrFromServiceData(serviceData, serviceData.getServiceId());
		} catch (Exception e) {
			log.error("扫描全部获取数据异常，原因：" + e.getMessage(),e);
		}

		return null;
	}

}
