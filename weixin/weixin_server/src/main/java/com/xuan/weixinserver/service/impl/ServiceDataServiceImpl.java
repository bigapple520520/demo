/*
 * @(#)AccountExtServiceImpl.java    Created on 2013-5-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.service.impl;

import net.zdsoft.keel.util.Validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xuan.weixinserver.entity.ServiceData;
import com.xuan.weixinserver.service.ServiceDataService;
import com.xuan.weixinserver.util.JsonDataUtils;


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

		return JsonDataUtils.decodeServiceDataFromJsonStr(jsonStr);
	}

	@Override
	public String encodeJsonStrFromServiceData(ServiceData serviceData, String serviceId) {
		if(null == serviceData){
			return null;
		}

		return JsonDataUtils.encodeJsonStrFromServiceData(serviceData, serviceId);
	}

}
