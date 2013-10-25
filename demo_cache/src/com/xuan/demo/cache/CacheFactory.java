package com.xuan.demo.cache;

import com.xuan.demo.cache.impl.LruCacheImpl;

/**
 * 获取cache的工厂类
 * 
 * @author xuan
 */
public abstract class CacheFactory {
	private static final int DEFAULT_SIZE = 20;
	private static Cache<String, Object> defalutCache;

	/**
	 * 获取一个默认大小容量的缓存
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
