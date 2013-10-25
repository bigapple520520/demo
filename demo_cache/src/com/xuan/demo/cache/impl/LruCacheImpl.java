package com.xuan.demo.cache.impl;

import android.support.v4.util.LruCache;

import com.xuan.demo.cache.Cache;

/**
 * 使用安卓自带的缓存实现
 * 
 * @author xuan
 */
public class LruCacheImpl<K, V> implements Cache<K, V> {
	private final LruCache<K, V> lruCache;

	public LruCacheImpl(int maxSize) {
		lruCache = new LruCache<K, V>(maxSize);
	}

	/**
	 * 存入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public V put(K key, V value) {
		return lruCache.put(key, value);
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public V get(K key) {
		return lruCache.get(key);
	}

	/**
	 * 清理指定key的缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public V remove(K key) {
		return lruCache.remove(key);
	}

	/**
	 * 删除所有缓存
	 */
	@Override
	public void removeAll() {
		lruCache.evictAll();
	}

	/**
	 * 返回占用缓存
	 * 
	 * @return
	 */
	@Override
	public int size() {
		return lruCache.size();
	}

	/**
	 * 返回缓存最大容量，初始化的时候的定义值
	 * 
	 * @return
	 */
	@Override
	public int maxSize() {
		return lruCache.maxSize();
	}

	/**
	 * 返回缓存的各种指数
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("size[" + size() + "]");
		sb.append("maxSize" + maxSize() + "]");
		return sb.toString();
	}

}
