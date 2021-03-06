package com.xuan.weixinserver.entity;

import java.util.Date;
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
	 * 记录最近一次同步时间
	 */
	private Date lastSyncTime;

	/**
	 * 如果服务器端修改了数据，那设置这个状态是true，知道发起同步成功后再设置为false。<br>
	 * 所以如果客户端过来进行数据同步时，如果发现这个标识是true的，就先不进行同步处理
	 */
	private volatile boolean isDirty = false;

	/**
	 * 对应客户端登录上来的id，用来通知客户端数据的变化
	 */
	private String loginId;

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
	public void addTable(String key, Table table){
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

	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}

	public Map<String, Table> getMap() {
		return map;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

}
