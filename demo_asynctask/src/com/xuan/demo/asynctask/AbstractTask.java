/* 
 * @(#)AbstractTask.java    Created on 2013-2-17
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.xuan.demo.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.demo.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.demo.asynctask.helper.Result;

/**
 * 耗时任务类的基类，采用了监听器设计模式，模板方法,注意这个任务类的实例只能在UI线程中被创建
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-17 下午4:32:49 $
 */
public abstract class AbstractTask<T> extends
		AsyncTask<Object, Integer, Result<T>> {
	private final Context context;
	private boolean isShowProgressDialog = true;// 默认显示

	private AsyncTaskSuccessCallback<T> asyncTaskSuccessCallback;// 成功回调
	private AsyncTaskFailCallback<T> asyncTaskFailCallback;// 失败回调

	private ProgressDialog progressDialog;// 提示框

	public AbstractTask(Context context) {
		this.context = context;
	}

	public AbstractTask(Context context, boolean isShowProgressDialog) {
		this.context = context;
		this.isShowProgressDialog = isShowProgressDialog;
	}

	@Override
	protected void onPreExecute() {
		if (isShowProgressDialog) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("请稍后...");
			progressDialog.setCancelable(true);
			progressDialog.show();
		}
	}

	@Override
	protected Result<T> doInBackground(Object... objects) {
		Result<T> result = null;
		try {
			result = doHttpRequest(objects);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	protected void onPostExecute(Result<T> result) {
		if (result.isSuccess()) {
			if (null != asyncTaskSuccessCallback) {
				asyncTaskSuccessCallback.successCallback(result);
			} else {
				if (isShowProgressDialog && !TextUtils.isEmpty(result.getMessage())) {
					Toast.makeText(context, result.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			if (null != asyncTaskFailCallback) {
				asyncTaskFailCallback.failCallback(result);
			} else {
				String errorMessage = result.getMessage();
				if (isShowProgressDialog && !TextUtils.isEmpty(errorMessage)) {
					errorMessage = errorMessage.substring(errorMessage
							.indexOf(":") + 1, result.getMessage().length());
					Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}

		if (isShowProgressDialog) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 设置成功监听
	 * 
	 * @param asyncTaskSuccessCallback
	 */
	public void setAsyncTaskSuccessCallback(
			AsyncTaskSuccessCallback<T> asyncTaskSuccessCallback) {
		this.asyncTaskSuccessCallback = asyncTaskSuccessCallback;
	}

	/**
	 * 设置失败监听
	 * 
	 * @param asyncTaskFailCallback
	 */
	public void setAsyncTaskFailCallback(
			AsyncTaskFailCallback<T> asyncTaskFailCallback) {
		this.asyncTaskFailCallback = asyncTaskFailCallback;
	}

	/**
	 * http请求现在这里，需要子类自己实现
	 * 
	 * @param params
	 * @return
	 */
	protected abstract Result<T> doHttpRequest(Object... objects);

}
