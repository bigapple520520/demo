/* 
 * @(#)CallByFragmentListener.java    Created on 2014-7-10
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo.tabfragment.mcall;

/**
 * Fragment和Activity通讯的接口，Activity实现这个接口，然后它的实例在传递给Fragment之后就可以向上转型了
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-7-10 下午2:10:04 $
 */
public interface CallByFragmentListener {

    /**
     * Activity实现这个接口，供Fragment调用
     * 
     * @param fragmentTag
     *            可以表示调用命令
     * @param data
     *            可以传递数据
     */
    public void callByFragment(int command, Object... data);

}
