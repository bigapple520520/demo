package com.xuan.weixinserver.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对应一张表的数据结构
 *
 * @author xuan
 * @version 创建时间：2014-7-25 下午3:01:17
 */
public class Table {
	/**
	 * 用来存放一张表的数据，其中key可以是每行数据的唯一标识例如id，value是一行数据对象
	 */
	private final Map<String, TableLine> map = new ConcurrentHashMap<String, TableLine>();

	/**
	 * 获取表中的一行数据
	 *
	 * @param key
	 * @return
	 */
	public TableLine get(String key){
		return map.get(key);
	}

	/**
	 * 新增或者修改一行数据，如果存在会被覆盖，达到更新的效果
	 * @param key
	 * @param tableLine
	 */
	public void put(String key, TableLine tableLine){
		map.put(key, tableLine);
	}

}
