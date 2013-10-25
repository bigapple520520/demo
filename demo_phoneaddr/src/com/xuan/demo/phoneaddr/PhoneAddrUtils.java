package com.xuan.demo.phoneaddr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.xuan.demo.phoneaddr.helper.PhoneAddrEntity;

/**
 * 获取通讯录的工具类
 * 
 * @author xuan
 */
public abstract class PhoneAddrUtils {
	// 需要查询数据的字段
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };

	// 查询地址，实际对应content:// com.android.contacts/data/phones
	private static final Uri URI = Phone.CONTENT_URI;

	// 注意对应上面的PHONES_PROJECTION数组
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;// 联系人显示名称
	private static final int PHONES_NUMBER_INDEX = 1;// 电话号码
	private static final int PHONES_CONTACT_ID_INDEX = 2;// 联系人的ID

	/**
	 * 获取手机中的所有通讯录
	 * 
	 * @param context
	 * @return
	 */
	public static List<PhoneAddrEntity> getPhoneAddrs(Context context) {
		List<PhoneAddrEntity> ret = new ArrayList<PhoneAddrEntity>();

		ContentResolver resolver = context.getContentResolver();

		// 获取手机联系人
		Cursor cursor = resolver
				.query(URI, PHONES_PROJECTION, null, null, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {

				// 得到手机号码
				String phone = cursor.getString(PHONES_NUMBER_INDEX);

				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phone)) {
					continue;
				}

				// 得到联系人名称
				String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);

				// 得到联系人ID
				Long id = cursor.getLong(PHONES_CONTACT_ID_INDEX);

				PhoneAddrEntity pae = new PhoneAddrEntity();
				pae.setId(id);
				pae.setName(name);
				pae.setPhone(phone);
				ret.add(pae);
			}
		}

		cursor.close();
		return ret;
	}

	/**
	 * 获取所有sim卡中的通讯录
	 * 
	 * @param context
	 * @return
	 */
	public static List<PhoneAddrEntity> getSimPhoneAddrs(Context context) {
		List<PhoneAddrEntity> ret = new ArrayList<PhoneAddrEntity>();

		ContentResolver resolver = context.getContentResolver();

		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor cursor = resolver
				.query(uri, PHONES_PROJECTION, null, null, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				// 得到手机号码
				String phone = cursor.getString(PHONES_NUMBER_INDEX);

				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phone)) {
					continue;
				}

				// 得到联系人名称
				String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);

				PhoneAddrEntity pae = new PhoneAddrEntity();
				pae.setName(name);
				pae.setPhone(phone);
				ret.add(pae);
			}
		}

		return ret;
	}

	/**
	 * 获取所有通讯录，包括手机中和sim卡中的
	 * 
	 * @param context
	 * @return
	 */
	public static List<PhoneAddrEntity> getAllPhoneAddrs(Context context) {
		List<PhoneAddrEntity> ret = new ArrayList<PhoneAddrEntity>();

		ret = getPhoneAddrs(context);

		List<PhoneAddrEntity> simAddressList = getSimPhoneAddrs(context);
		ret.addAll(simAddressList);

		// 过滤号码一样的
		Set<String> phoneSet = new HashSet<String>();
		Iterator<PhoneAddrEntity> iter = ret.iterator();
		while (iter.hasNext()) {
			PhoneAddrEntity pae = iter.next();

			if (!phoneSet.contains(pae.getPhone())) {
				phoneSet.add(pae.getPhone());
			} else {
				iter.remove();
			}
		}

		return ret;
	}
}
