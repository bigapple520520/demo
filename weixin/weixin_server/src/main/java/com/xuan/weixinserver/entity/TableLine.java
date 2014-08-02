package com.xuan.weixinserver.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代表一张表中的一行数据
 *
 * @author xuan
 * @version 创建时间：2014-7-25 下午3:02:38
 */
public class TableLine {
	/**
	 * 用来存放一张表中的一行数据，key可以是字段名称，value就是对应的值
	 */
	private final Map<String, String> map = new ConcurrentHashMap<String, String>();

	/**
	 * 新增或者修改数据元，如果已存在会被覆盖，达到修改的目的
	 * @param key
	 * @param value
	 */
	public void put(String key, String value){
		map.put(key, value);
	}

	/**
	 * 获取对应数据元的值
	 *
	 * @param key
	 */
	public String get(String key){
		return map.get(key);
	}

	public Map<String, String> getMap() {
		return map;
	}

}
