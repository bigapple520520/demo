/*
 * @(#)LogbackUtils.java    Created on 2014-5-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.zdsoft.keel.util.Validators;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

/**
 * 用于获取 logback 日志文件和 logger 对象的工具类。
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-15 上午11:11:29 $
 */
public abstract class LogbackUtils {
    static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogbackUtils.class);

    private static String logDir = null;
    private static Logger logbackLogger = null;

    static {
        if (logger instanceof ch.qos.logback.classic.Logger) {
            logbackLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

            for (Iterator<Appender<ILoggingEvent>> iter = logbackLogger.iteratorForAppenders(); iter.hasNext();) {
                Appender<ILoggingEvent> appender = iter.next();
                if (appender instanceof FileAppender<?>) {
                    logDir = ((FileAppender<ILoggingEvent>) appender).getFile();
                    break;
                }
            }

            if (logDir != null) {
                logDir = logDir.substring(0, logDir.lastIndexOf('/'));
            }
            logger.debug("logDir: {}", logDir);
        }
    }

    /**
     * 获取系统中所有的 Logger 对象。
     *
     * @return logger对象列表
     */
    public static List<Logger> getAllLoggers() {
        if (logbackLogger == null) {
            return Collections.emptyList();
        }
        else {
            return logbackLogger.getLoggerContext().getLoggerList();
        }
    }

    /**
     * 设置 logger 对象的日志级别。
     *
     * @param loggerName
     *            logger 名称
     * @param level
     *            日志级别
     */
    public static void setLoggerLevel(String loggerName, Level level) {
        if (Validators.isEmpty(loggerName)) {
            throw new IllegalArgumentException("loggerName can't be empty");
        }

        if (level == null) {
            throw new IllegalArgumentException("level can't be null");
        }

        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(level);
    }

    /**
     * 取得所有日志文件。
     *
     * @return 日志文件列表
     */
    public static List<File> getAllLogFiles() {
        if (logDir == null) {
            return Collections.emptyList();
        }

        File file = new File(logDir);
        if (!file.exists()) {
            return Collections.emptyList();
        }

        List<File> logs = new ArrayList<File>();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                logs.add(files[i]);
            }
        }

        return logs;
    }

    /**
     * 根据名称找到日志文件。
     *
     * @param fileName
     *            日志文件名称
     * @return 日志文件
     */
    public static File getLogFile(String fileName) {
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Illegal fileName");
        }

        File log = new File(logDir + File.separator + fileName);
        return log.exists() ? log : null;
    }

    /**
     * 根据名称删除日志文件。
     *
     * @param fileName
     *            日志文件名称
     */
    public static void removeLogFile(String fileName) {
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Illegal fileName");
        }

        File log = new File(logDir + File.separator + fileName);
        if (log.exists()) {
            log.delete();
            logger.debug("Deleted log file " + log);
        }
    }

    /**
     * 取得日志文件根目录。
     *
     * @return 日志文件根目录
     */
    protected static String getLogDir() {
        return logDir;
    }

}
