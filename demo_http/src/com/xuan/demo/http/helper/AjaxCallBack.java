package com.xuan.demo.http.helper;

/**
 * http???�???? * 
 * @author xuan
 */
public interface AjaxCallBack {

	/**
	 * ????��?
	 * 
	 * @param isSuccess
	 *            表示???请�????
	 * @param result
	 *            �???????	 * @param e
	 *            �???��?�?���??�?��
	 */
	public void callBack(boolean isSuccess, String result, Exception e);
}
