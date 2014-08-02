/*
 * @(#)ApplicationConfigHelper.java    Created on 2012-1-19
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id: ApplicationConfigHelper.java 24913 2012-02-15 10:55:51Z huangwq $
 */
package com.xuan.weixinclient.client;

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

    private static String appEdition;
    private static String appEditionName;
    private static String connectIp;
    private static String connectPort;
    private static String dataFilePath;
    
    static {
        Properties p = new Properties();
        try {
            p.load(ApplicationConfigHelper.class.getResourceAsStream("/applicationConfig.properties"));
        }
        catch (IOException e) {
            logger.error("ApplicationConfigHelper", e.getMessage(), e);
        }
        
        appEdition = p.getProperty("app.edition", "V1.0");
        appEditionName = p.getProperty("app.edition.name", "客户端收集数据程序");
        connectIp = p.getProperty("connect.ip", "127.0.0.1");
        connectPort = p.getProperty("connect.port", "10000");
        dataFilePath = p.getProperty("data.file.path", "C://data.xls");
    }

	public static String getAppEdition() {
		return appEdition;
	}

	public static void setAppEdition(String appEdition) {
		ApplicationConfigHelper.appEdition = appEdition;
	}

	public static String getAppEditionName() {
		return appEditionName;
	}

	public static void setAppEditionName(String appEditionName) {
		ApplicationConfigHelper.appEditionName = appEditionName;
	}

	public static String getConnectIp() {
		return connectIp;
	}

	public static void setConnectIp(String connectIp) {
		ApplicationConfigHelper.connectIp = connectIp;
	}

	public static String getConnectPort() {
		return connectPort;
	}

	public static void setConnectPort(String connectPort) {
		ApplicationConfigHelper.connectPort = connectPort;
	}

	public static String getDataFilePath() {
		return dataFilePath;
	}

	public static void setDataFilePath(String dataFilePath) {
		ApplicationConfigHelper.dataFilePath = dataFilePath;
	}

	public static Logger getLogger() {
		return logger;
	}

}
