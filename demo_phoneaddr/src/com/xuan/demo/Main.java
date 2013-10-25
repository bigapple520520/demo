package com.xuan.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xuan.demo.phoneaddr.PhoneAddrUtils;
import com.xuan.demo.phoneaddr.helper.PhoneAddrEntity;

/**
 * 记得加读取联系人的权限<br>
 * <uses-permission android:name="android.permission.READ_CONTACTS"/>
 * 
 * @author xuan
 */
public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 获取手机通讯录
		List<PhoneAddrEntity> paeList = PhoneAddrUtils.getAllPhoneAddrs(this);

		// 转化成字符串数组方便显示
		ArrayList<String> paeStrList = new ArrayList<String>();
		for (PhoneAddrEntity pae : paeList) {
			String id = "id:" + pae.getId();
			String name = "name:" + pae.getName();
			String phone = "phone:" + pae.getPhone();
			paeStrList.add(id + "\n" + name + "\n" + phone);
		}

		// 创建设配器
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				paeStrList.toArray(new String[paeStrList.size()]));

		// 列表设置数据
		ListView phoneAddrsListView = (ListView) findViewById(R.id.phoneAddrsListView);
		phoneAddrsListView.setAdapter(adapter);
	}
}
