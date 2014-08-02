package com.xuan.monitor.utils;

import javax.swing.JOptionPane;

/**
 * 弹出框封装
 *
 * @author xuan
 * @version 创建时间：2014-7-26 下午3:23:42
 */
public abstract class DialogUtils {

	/**
	 * 提示框
	 *
	 * @param message
	 */
	public static void showInfo(String message){
		JOptionPane.showMessageDialog(null, message, "提示", JOptionPane.INFORMATION_MESSAGE);
	}

}
