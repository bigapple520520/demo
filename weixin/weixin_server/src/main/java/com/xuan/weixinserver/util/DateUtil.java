/*
 * @(#)DateUtil.java    Created on 2013-10-12
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-10-12 下午2:24:16 $
 */
public class DateUtil {

    /**
     * 取得指定秒数后的时间
     *
     * @param date
     *            基准时间
     * @param secondsAmount
     *            指定秒数，允许为负数
     * @return 指定秒数后的时间
     */
    public static Date addSeconds(Date date, int secondsAmount) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, secondsAmount);
        return calendar.getTime();
    }

}
