package com.xuan.weixinserver.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个这样的对象表示一个工程
 *
 * @author xuan
 * @version 创建时间：2014-7-25 下午2:51:22
 */
public class ServiceData {
	/**
	 * 一个工程的标识
	 */
	private String serviceId;

	/**
	 * 如果服务器端修改了数据，那设置这个状态是true，知道发起同步成功后再设置为false。<br>
	 * 所以如果客户端过来进行数据同步时，如果发现这个标识是true的，就先不进行同步处理
	 */
	private volatile boolean isDirty = false;

	/**
	 * 存放一堆数据库表，key是表名，value是一个表的对象
	 */
	private final Map<String, Table> map = new ConcurrentHashMap<String, Table>();

	/**
	 * 添加或者更新表，如果已经存在会进行覆盖替换，达到更新的效果
	 *
	 * @param key
	 * @param table
	 */
	public void putTable(String key, Table table){
		map.put(key, table);
	}

	/**
	 * 获取数据表
	 *
	 * @param key
	 * @return
	 */
	public Table getTable(String key){
		return map.get(key);
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

}
