package com.xuan.demo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.xuan.demo.http.helper.AjaxCallBack;
import com.xuan.demo.http.helper.AjaxParams;

/**
 * Http操作类
 * 
 * @author xuan
 */
public class AnHttp {
	private static final String TAG = "bigapple.AnHttp";

	private int connectionTimeout = 1000 * 12;// 默认12S
	private int readTimeout = 1000 * 12;// 默认12S
	private String encode = "utf-8";// 默认使用utf-8编码

	/**
	 * GET请求，异步
	 * 
	 * @param url
	 * @param callBack
	 */
	public void get(String url, final AjaxParams params, AjaxCallBack callBack) {
		get(url, params, callBack, false);
	}

	/**
	 * GET请求，同步 || 异步
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 * @param isSync
	 */
	public void get(final String url, final AjaxParams params,
			final AjaxCallBack callBack, boolean isSync) {
		if (isSync) {
			doGet(url, params, callBack);
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					doGet(url, params, callBack);
				}
			}).start();
		}
	}

	/**
	 * GET鼻祖方法
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 */
	private void doGet(String url, AjaxParams params, AjaxCallBack callBack) {
		if (null != params) {
			url += params.toString();
		}

		Log.d(TAG, url);

		String result = null;
		BufferedReader reader = null;
		try {
			URLConnection connection = new URL(url).openConnection();
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);

			connection.connect();
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encode), 8 * 1024);
			String line = null;
			StringBuilder buffer = new StringBuilder();
			line = reader.readLine();
			if (line != null) {
				buffer.append(line);
				while ((line = reader.readLine()) != null) {
					buffer.append("\n" + line);
				}
			}

			result = buffer.toString();
		} catch (IOException e) {
			if (null != callBack) {
				callBack.callBack(false, "连接异常", e);
			}
			return;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					if (null != callBack) {
						callBack.callBack(false, "连接关闭异常", e1);
					}
					return;
				}
			}
		}

		if (null != callBack) {
			callBack.callBack(true, result, null);
		}
	}

	/**
	 * POST请求，异步
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 */
	public void post(final String url, final AjaxParams params,
			final AjaxCallBack callBack) {
		post(url, params, callBack, false);
	}

	/**
	 * POST请求，同步 || 异步
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 * @param isSync
	 */
	public void post(final String url, final AjaxParams params,
			final AjaxCallBack callBack, boolean isSync) {
		if (isSync) {
			doPost(url, params, callBack);
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					doPost(url, params, callBack);
				}
			}).start();
		}
	}

	/**
	 * POST鼻祖方法
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 */
	private void doPost(String url, AjaxParams params, AjaxCallBack callBack) {
		if (null == params) {
			params = new AjaxParams();
		}

		Log.v(TAG, url + params.toString());

		LinkedList<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();

		for (Map.Entry<String, String> entry : params.getParamsMap().entrySet()) {
			try {
				paramsList.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			} catch (Exception e) {
				if (null != callBack) {
					callBack.callBack(false, null, e);
				}
				return;
			}
		}

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(paramsList, encode));

			HttpResponse response = client.execute(httpPost); // 执行POST方法

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String reslut = EntityUtils.toString(response.getEntity(),
						encode);
				if (null != callBack) {
					callBack.callBack(true, reslut, null);
				}
			} else {
				if (null != callBack) {
					callBack.callBack(false, "请求错误", null);
				}
			}
		} catch (Exception e) {
			if (null != callBack) {
				callBack.callBack(false, "请求异常", null);
			}
		}
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}
}