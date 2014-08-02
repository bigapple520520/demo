/*
 * @(#)ApplicationConfigHelper.java    Created on 2012-1-19
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id: ApplicationConfigHelper.java 24913 2012-02-15 10:55:51Z huangwq $
 */
package com.xuan.weixinserver.helper;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统配置，可以用来判断系统属于那个版本
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-28 上午10:24:47 $
 */
public abstract class ApplicationConfigHelper {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfigHelper.class);

    private static String APP_EDITION = "app.edition";

    private static String appEdition;// 标识系统的版本

    static {
        Properties p = new Properties();
        try {
            p.load(ApplicationConfigHelper.class.getResourceAsStream("/applicationConfig.properties"));
        }
        catch (IOException e) {
            logger.error("ApplicationConfigHelper", "", e);
        }

        appEdition = p.getProperty(APP_EDITION, "common");
    }

    public static String getAppEdition() {
        return appEdition;
    }

    public static void setAppEdition(String appEdition) {
        ApplicationConfigHelper.appEdition = appEdition;
    }

    /**
     * 通用版本
     *
     * @return
     */
    public static boolean isCommonEdition() {
        return "common".equals(appEdition);
    }

    /**
     * 宁波人人通版本
     *
     * @return
     */
    public static boolean isNbrrtEdition() {
        return "nbrrt".equals(appEdition);
    }

    public static void main(String[] args) {
        System.out.println(isCommonEdition());
    }

}
