package com.xuan.demo.cache;

/**
 * 一个简单的cache接口
 * 
 * @author xuan
 */
public interface Cache<K, V> {

	/**
	 * 存入缓存,返回之前被替换的值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public V put(K key, V value);

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * 清理指定key的缓存
	 * 
	 * @param key
	 * @return
	 */
	public V remove(K key);

	/**
	 * 删除所有缓存
	 */
	public void removeAll();

	/**
	 * 返回占用缓存
	 * 
	 * @return
	 */
	public int size();

	/**
	 * 返回缓存最大容量，初始化的时候的定义值
	 * 
	 * @return
	 */
	public int maxSize();

	/**
	 * 返回缓存的各种指数
	 * 
	 * @return
	 */
	@Override
	public String toString();

}
