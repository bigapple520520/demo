/* 
 * @(#)AsyncTaskSuccessCallback.java    Created on 2013-2-17
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.asynctask.callback;

import com.xuan.demo.asynctask.helper.Result;

/**
 * ��ʱ����ɹ��ص��ӿ�
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-17 ����4:28:41 $
 */
public interface AsyncTaskSuccessCallback<T> {
	/**
	 * ִ�з���
	 * 
	 * @param result
	 */
	public void successCallback(Result<T> result);
}
