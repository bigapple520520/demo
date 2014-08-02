/*
 * @(#)ScheduledTaskExecutorFactory.java    Created on 2013-5-20
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinclient.task;

import net.zdsoft.keel.util.concurrent.ScheduledTaskExecutor;

/**
 * 线程池工厂类
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-20 上午10:51:53 $
 */
public abstract class ScheduledTaskExecutorFactory {
    private static ScheduledTaskExecutor scheduledTaskExecutor = new ScheduledTaskExecutor(10);

    /**
     * 返回一个线程池
     *
     * @return
     */
    public static ScheduledTaskExecutor getScheduledTaskExecutor() {
        return scheduledTaskExecutor;
    }

}
