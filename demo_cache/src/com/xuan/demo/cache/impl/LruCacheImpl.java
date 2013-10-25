package com.xuan.demo.cache.impl;

import android.support.v4.util.LruCache;

import com.xuan.demo.cache.Cache;

/**
 * ʹ�ð�׿�Դ��Ļ���ʵ��
 * 
 * @author xuan
 */
public class LruCacheImpl<K, V> implements Cache<K, V> {
	private final LruCache<K, V> lruCache;

	public LruCacheImpl(int maxSize) {
		lruCache = new LruCache<K, V>(maxSize);
	}

	/**
	 * ���뻺��
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
	 * ��ȡ����
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public V get(K key) {
		return lruCache.get(key);
	}

	/**
	 * ����ָ��key�Ļ���
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public V remove(K key) {
		return lruCache.remove(key);
	}

	/**
	 * ɾ�����л���
	 */
	@Override
	public void removeAll() {
		lruCache.evictAll();
	}

	/**
	 * ����ռ�û���
	 * 
	 * @return
	 */
	@Override
	public int size() {
		return lruCache.size();
	}

	/**
	 * ���ػ��������������ʼ����ʱ��Ķ���ֵ
	 * 
	 * @return
	 */
	@Override
	public int maxSize() {
		return lruCache.maxSize();
	}

	/**
	 * ���ػ���ĸ���ָ��
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
