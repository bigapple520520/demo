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
	/**
	 * 监控程序向中央服务器获取数据
	 */
	public static final int ACTION_GET_DATA = 1;

	/**
	 * 客户端程序向中央服务器推送数据，保持数据的同步
	 */
	public static final int ACTION_SYNC_DATA = 2;

	//-------------------------------------------数据库表字段名称-----------------------------------
	public static final String TABLE = "root";
	public static final String TABLE_COLUMN_1 = "key";
	public static final String TABLE_COLUMN_2 = "value";
	public static final String TABLE_COLUMN_3 = "qualityCode";
	public static final String TABLE_COLUMN_4 = "updateTime";

}
