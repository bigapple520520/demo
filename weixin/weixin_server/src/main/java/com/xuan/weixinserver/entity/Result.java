package com.xuan.weixinserver.entity;
/**
 * 收到数据请求封装
 *
 * @author xuan
 * @version 创建时间：2014-7-31 下午2:09:59
 */
public class Result<T> {
	private String success;
	private String message;
	private T data;

	public Result(){
	}

	public Result(String success, String message){
		this.success = success;
		this.message = message;
	}

	public Result(String success, String message, T data){
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
