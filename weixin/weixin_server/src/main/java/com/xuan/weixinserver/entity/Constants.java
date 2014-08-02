/*
 * @(#)Constants.java    Created on 2014-2-28
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.entity;

/**
 * 系统参数常量
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-2-28 下午4:59:09 $
 */
public abstract class Constants {
	//数据库表字段名称
	public static final String TABLE = "root";
	public static final String TABLE_COLUMN_1 = "key";
	public static final String TABLE_COLUMN_2 = "value";
	public static final String TABLE_COLUMN_3 = "qualityCode";
	public static final String TABLE_COLUMN_4 = "updateTime";

	//传输Json格式数据时，用来表示操作是否成功的状态
	public static final String SUCCESS_1 = "1";
	public static final String SUCCESS_0 = "0";

	//返回Json串数据的常量
	public static final String JSON_TAG_SUCCESS = "success";
	public static final String JSON_TAG_MESSAGE = "message";
}
