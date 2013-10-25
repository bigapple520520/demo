package com.xuan.demo.cache;

import com.xuan.demo.cache.impl.LruCacheImpl;

/**
 * ��ȡcache�Ĺ�����
 * 
 * @author xuan
 */
public abstract class CacheFactory {
	private static final int DEFAULT_SIZE = 20;
	private static Cache<String, Object> defalutCache;

	/**
	 * ��ȡһ��Ĭ�ϴ�С�����Ļ���
	 * 
	 * @return
	 */
	public static Cache<String, Object> getDefaultCache() {
		if (null == defalutCache) {
			defalutCache = new LruCacheImpl<String, Object>(DEFAULT_SIZE);
		}

		return defalutCache;
	}

}
