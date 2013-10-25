package com.xuan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.xuan.demo.cache.CacheFactory;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StringBuilder sb = new StringBuilder();
		// 缓存容量指数
		sb.append("缓存容量指数:" + CacheFactory.getDefaultCache() + "\n");

		// 生成要缓存的对象，我们存几个字符串
		String key1 = "key1";
		String key2 = "key2";
		String key3 = "key3";
		String cacheStr1 = "我是被缓存的内容1";
		String cacheStr2 = "我是被缓存的内容2";
		String cacheStr3 = "我是被缓存的内容3";
		CacheFactory.getDefaultCache().put(key1, cacheStr1);
		CacheFactory.getDefaultCache().put(key2, cacheStr2);
		sb.append("缓存了：[" + cacheStr1 + "]");
		sb.append("[" + cacheStr2 + "]\n");

		// 缓存容量指数
		sb.append("缓存容量指数:" + CacheFactory.getDefaultCache() + "\n");

		// 删除第一个缓存
		CacheFactory.getDefaultCache().remove(key1);
		sb.append("删除第一个缓存[" + cacheStr1 + "]\n");

		// 缓存容量指数
		sb.append("缓存容量指数:" + CacheFactory.getDefaultCache() + "\n");

		// 取几个缓存显示
		sb.append("我们取第二个缓存看一下:" + CacheFactory.getDefaultCache().get(key2)
				+ "\n");
		sb.append("我们取第一个被删除的缓存看一下:" + CacheFactory.getDefaultCache().get(key1)
				+ "\n");
		sb.append("我们取一个不存在的缓存看一下:" + CacheFactory.getDefaultCache().get(key3)
				+ "\n");

		// 替换缓存
		String oldCacheStr = (String) CacheFactory.getDefaultCache().put(key2,
				cacheStr3);
		sb.append("我们用缓存3替换缓存2后的返回的结果:" + oldCacheStr + "\n");
		sb.append("替换后我们看看缓存2现在成什么值了:"
				+ CacheFactory.getDefaultCache().get(key2) + "\n");

		// 缓存容量指数
		sb.append("缓存容量指数:" + CacheFactory.getDefaultCache() + "\n");

		// 删除全部后，缓存容量指数
		CacheFactory.getDefaultCache().removeAll();
		sb.append("删除全部后，缓存容量指数:" + CacheFactory.getDefaultCache() + "\n");

		TextView textView = (TextView) findViewById(R.id.textView);
		textView.setText(sb.toString());
	}

}
