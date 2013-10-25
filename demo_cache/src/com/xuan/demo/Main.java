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
		// ��������ָ��
		sb.append("��������ָ��:" + CacheFactory.getDefaultCache() + "\n");

		// ����Ҫ����Ķ������Ǵ漸���ַ���
		String key1 = "key1";
		String key2 = "key2";
		String key3 = "key3";
		String cacheStr1 = "���Ǳ����������1";
		String cacheStr2 = "���Ǳ����������2";
		String cacheStr3 = "���Ǳ����������3";
		CacheFactory.getDefaultCache().put(key1, cacheStr1);
		CacheFactory.getDefaultCache().put(key2, cacheStr2);
		sb.append("�����ˣ�[" + cacheStr1 + "]");
		sb.append("[" + cacheStr2 + "]\n");

		// ��������ָ��
		sb.append("��������ָ��:" + CacheFactory.getDefaultCache() + "\n");

		// ɾ����һ������
		CacheFactory.getDefaultCache().remove(key1);
		sb.append("ɾ����һ������[" + cacheStr1 + "]\n");

		// ��������ָ��
		sb.append("��������ָ��:" + CacheFactory.getDefaultCache() + "\n");

		// ȡ����������ʾ
		sb.append("����ȡ�ڶ������濴һ��:" + CacheFactory.getDefaultCache().get(key2)
				+ "\n");
		sb.append("����ȡ��һ����ɾ���Ļ��濴һ��:" + CacheFactory.getDefaultCache().get(key1)
				+ "\n");
		sb.append("����ȡһ�������ڵĻ��濴һ��:" + CacheFactory.getDefaultCache().get(key3)
				+ "\n");

		// �滻����
		String oldCacheStr = (String) CacheFactory.getDefaultCache().put(key2,
				cacheStr3);
		sb.append("�����û���3�滻����2��ķ��صĽ��:" + oldCacheStr + "\n");
		sb.append("�滻�����ǿ�������2���ڳ�ʲôֵ��:"
				+ CacheFactory.getDefaultCache().get(key2) + "\n");

		// ��������ָ��
		sb.append("��������ָ��:" + CacheFactory.getDefaultCache() + "\n");

		// ɾ��ȫ���󣬻�������ָ��
		CacheFactory.getDefaultCache().removeAll();
		sb.append("ɾ��ȫ���󣬻�������ָ��:" + CacheFactory.getDefaultCache() + "\n");

		TextView textView = (TextView) findViewById(R.id.textView);
		textView.setText(sb.toString());
	}

}
