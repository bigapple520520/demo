package com.xuan.weixinserver.action;

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
	private final Map<String,ServiceData> serviceId2DerviceDataMap = new ConcurrentHashMap<String, ServiceData>();

}
