/*
 * @(#)AccountExtService.java    Created on 2013-5-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.service;

import com.xuan.weixinserver.entity.ServiceData;

/**
 * 服务数据接口
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午11:00:50 $
 */
public interface ServiceDataService {

	/**
	 * 从Json串中解析出ServiceData数据
	 *
	 * @param message
	 * @return
	 */
	ServiceData decodeServiceDataFromJsonStr(String jsonStr);

	/**
	 * 从ServiceData数据中编码成Json串
	 *
	 * @param serviceData
	 * @return
	 */
	String encodeJsonStrFromServiceData(ServiceData serviceData, String serviceId);

}

