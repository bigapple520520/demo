/* 
 * @(#)CallByActivityListener.java    Created on 2014-7-10
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.tabfragment.mcall;

/**
 * Fragment实现这个接口，供Activity调用
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午2:58:23 $
 */
public interface CallByActivityListener {
    /**
     * Fragment实现这个接口，供Activity调用
     * 
     * @param fragmentTag
     *            可以表示调用命令
     * @param data
     *            可以传递数据
     */
    public void callByActivity(int command, Object... data);
}
