/*
 * @(#)JsonUtils.java    Created on 2013-1-31
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json简单处理封装
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-1-31 下午1:39:11 $
 */
public abstract class JsonUtils {

    /**
     * 返回结果
     *
     * @param code
     * @param message
     * @return
     * @throws JSONException
     */
    public static String getRet(String code, String message) {
    	try {
    		JSONObject ret = new JSONObject();
            ret.put("success", code);
            ret.put("message", message);
            return ret.toString();
		} catch (Exception e) {
			return "{\"success\":\"0\",\"message\":\"jsonError\"}";
		}
    }

    /**
     * 获取json的错误提示
     *
     * @param error
     * @return
     */
    public static String getError(String error)  {
    	return getRet("0", error);
    }

    /**
     * 获取json的消息提示
     *
     * @param message
     * @return
     */
    public static String getMessage(String message){
    	return getRet("1", message);
    }

    public static String getString(JSONObject jsonObject, String name) throws Exception {
        return jsonObject.has(name) ? jsonObject.getString(name) : "";
    }

    public static int getInt(JSONObject jsonObject, String name) throws Exception {
        return jsonObject.has(name) ? jsonObject.getInt(name) : 0;
    }

    public static boolean getBoolean(JSONObject jsonObject, String name) throws Exception {
        return jsonObject.has(name) ? jsonObject.getBoolean(name) : false;
    }

    public static double getDouble(JSONObject jsonObject, String name) throws Exception {
        return jsonObject.has(name) ? jsonObject.getDouble(name) : 0;
    }

    public static long getLang(JSONObject jsonObject, String name) throws Exception {
        return jsonObject.has(name) ? jsonObject.getLong(name) : 0;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String name) throws Exception {
        return jsonObject.has(name) ? jsonObject.getJSONObject(name) : null;
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String name) throws Exception {
        return jsonObject.has(name) ? jsonObject.getJSONArray(name) : null;
    }

}
