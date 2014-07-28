package com.xuan.weixinserver.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xuan.weixinserver.entity.ServiceData;

/**
 * 缓存数据类
 *
 * @author xuan
 * @version 创建时间：2014-7-25 下午2:49:20
 */
public abstract class CacheMap {

	/**
	 * 缓存着各个工程项目中的数据，key是工程的serviceId，value是数据库表的集合
	 */
	private static final Map<String, ServiceData> serviceId2DerviceDataMap = new ConcurrentHashMap<String, ServiceData>();

	/**
	 * 获取数据
	 *
	 * @param serviceId
	 * @return
	 */
	public static ServiceData getServiceData(String serviceId){
		return serviceId2DerviceDataMap.get(serviceId);
	}

	/**
	 * 放入数据
	 *
	 * @param serviceId
	 * @param serviceData
	 */
	public static void putServiceData(String serviceId, ServiceData serviceData){
		serviceId2DerviceDataMap.put(serviceId, serviceData);
	}

}
