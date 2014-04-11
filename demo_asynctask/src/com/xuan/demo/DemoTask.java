/* 
 * @(#)DemoTask.java    Created on 2013-4-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo;

import android.content.Context;

import com.xuan.demo.asynctask.AbstractTask;
import com.xuan.demo.asynctask.helper.Result;

/**
 * 使用例子
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-4-10 上午10:18:33 $
 */
public class DemoTask extends AbstractTask<String> {
	public DemoTask(Context context) {
		super(context);
	}

	public DemoTask(Context context, boolean isShowProgressDialog) {
		super(context, isShowProgressDialog);
	}

	@Override
	protected Result<String> doHttpRequest(Object... objects) {
		String name = (String) objects[0];

		// 模拟耗时操作
		try {
			Thread.sleep(1000 * 5);
		} catch (Exception e) {
		}

		return new Result<String>(true, "hello:" + name);
	}

}
