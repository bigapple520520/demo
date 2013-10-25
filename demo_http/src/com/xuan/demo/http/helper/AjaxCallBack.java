package com.xuan.demo.http.helper;

/**
 * http???è°???? * 
 * @author xuan
 */
public interface AjaxCallBack {

	/**
	 * ????¹æ?
	 * 
	 * @param isSuccess
	 *            è¡¨ç¤º???è¯·æ????
	 * @param result
	 *            è¿???????	 * @param e
	 *            å¦???ºç?å¼?¸¸ä¼??å¼?¸¸
	 */
	public void callBack(boolean isSuccess, String result, Exception e);
}
