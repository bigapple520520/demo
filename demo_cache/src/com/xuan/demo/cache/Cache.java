package com.xuan.demo.cache;

/**
 * һ���򵥵�cache�ӿ�
 * 
 * @author xuan
 */
public interface Cache<K, V> {

	/**
	 * ���뻺��,����֮ǰ���滻��ֵ
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public V put(K key, V value);

	/**
	 * ��ȡ����
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * ����ָ��key�Ļ���
	 * 
	 * @param key
	 * @return
	 */
	public V remove(K key);

	/**
	 * ɾ�����л���
	 */
	public void removeAll();

	/**
	 * ����ռ�û���
	 * 
	 * @return
	 */
	public int size();

	/**
	 * ���ػ��������������ʼ����ʱ��Ķ���ֵ
	 * 
	 * @return
	 */
	public int maxSize();

	/**
	 * ���ػ���ĸ���ָ��
	 * 
	 * @return
	 */
	@Override
	public String toString();

}
