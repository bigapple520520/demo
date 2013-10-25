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
 * ��ȡͨѶ¼�Ĺ�����
 * 
 * @author xuan
 */
public abstract class PhoneAddrUtils {
	// ��Ҫ��ѯ���ݵ��ֶ�
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };

	// ��ѯ��ַ��ʵ�ʶ�Ӧcontent:// com.android.contacts/data/phones
	private static final Uri URI = Phone.CONTENT_URI;

	// ע���Ӧ�����PHONES_PROJECTION����
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;// ��ϵ����ʾ����
	private static final int PHONES_NUMBER_INDEX = 1;// �绰����
	private static final int PHONES_CONTACT_ID_INDEX = 2;// ��ϵ�˵�ID

	/**
	 * ��ȡ�ֻ��е�����ͨѶ¼
	 * 
	 * @param context
	 * @return
	 */
	public static List<PhoneAddrEntity> getPhoneAddrs(Context context) {
		List<PhoneAddrEntity> ret = new ArrayList<PhoneAddrEntity>();

		ContentResolver resolver = context.getContentResolver();

		// ��ȡ�ֻ���ϵ��
		Cursor cursor = resolver
				.query(URI, PHONES_PROJECTION, null, null, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {

				// �õ��ֻ�����
				String phone = cursor.getString(PHONES_NUMBER_INDEX);

				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phone)) {
					continue;
				}

				// �õ���ϵ������
				String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);

				// �õ���ϵ��ID
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
	 * ��ȡ����sim���е�ͨѶ¼
	 * 
	 * @param context
	 * @return
	 */
	public static List<PhoneAddrEntity> getSimPhoneAddrs(Context context) {
		List<PhoneAddrEntity> ret = new ArrayList<PhoneAddrEntity>();

		ContentResolver resolver = context.getContentResolver();

		// ��ȡSims����ϵ��
		Uri uri = Uri.parse("content://icc/adn");
		Cursor cursor = resolver
				.query(uri, PHONES_PROJECTION, null, null, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				// �õ��ֻ�����
				String phone = cursor.getString(PHONES_NUMBER_INDEX);

				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phone)) {
					continue;
				}

				// �õ���ϵ������
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
	 * ��ȡ����ͨѶ¼�������ֻ��к�sim���е�
	 * 
	 * @param context
	 * @return
	 */
	public static List<PhoneAddrEntity> getAllPhoneAddrs(Context context) {
		List<PhoneAddrEntity> ret = new ArrayList<PhoneAddrEntity>();

		ret = getPhoneAddrs(context);

		List<PhoneAddrEntity> simAddressList = getSimPhoneAddrs(context);
		ret.addAll(simAddressList);

		// ���˺���һ����
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
