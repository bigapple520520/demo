package com.xuan.demo.phoneaddr.helper;

/**
 * 通讯录中常用字段实体类
 * 
 * @author xuan
 */
public class PhoneAddrEntity {

	private long id;
	private String name;
	private String phone;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
