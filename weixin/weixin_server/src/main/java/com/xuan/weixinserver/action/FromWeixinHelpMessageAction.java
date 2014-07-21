/*
 * @(#)FromWeixinHelpMessageDeal.java    Created on 2014-4-15
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.action;

import java.io.File;
import java.util.Date;

import net.zdsoft.keel.util.SecurityUtils;
import net.zdsoft.keel.util.Validators;

import org.apache.commons.io.FileUtils;

import ch.qos.logback.classic.Level;

import com.winupon.base.wpcf.util.DateUtils;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.message.help.FromWeixinHelpMessage;
import com.xuan.weixinserver.message.help.FromWeixinHelpRespMessage;
import com.xuan.weixinserver.message.help.GetLogMessage;
import com.xuan.weixinserver.server.WeiXinServerSendHelper;
import com.xuan.weixinserver.util.LogbackUtils;
import com.xuan.weixinserver.wx.session.WxSessionManager;

/**
 * 系统消息处理
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-4-15 下午4:31:44 $
 */
public class FromWeixinHelpMessageAction extends BasicAction {

    @Override
    protected void doDealMessage(AbstractMessage abstractMessage) {
        FromWeixinHelpMessage message = (FromWeixinHelpMessage) abstractMessage;
        int type = message.getType();
        String md5 = message.getMd5();

        String fromAccountId = WxSessionManager.getInstance().getAccountIdByLoginId(getActionContext().getLoginId());
        if (!checkMd5(md5, fromAccountId)) {
            responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_MD5_ERROR, "MD5校验错误，请检查。"));
            return;
        }

        switch (type) {
        case FromWeixinHelpMessage.TYPE_CLEAR_CACHE:
            // 清空所有缓存
            break;
        case FromWeixinHelpMessage.TYPE_ONLINE_NUM:
            // 统计在线人数
            onlineNum();
            break;
        case FromWeixinHelpMessage.TYPE_MESSAGE_SENT_SIZE:
            // 当前发送的消息但还没收到响应的消息数
            break;
        case FromWeixinHelpMessage.TYPE_JVM_MEMORY:
            // 当前发送的消息但还没收到响应的消息数
            jvmMemory();
            break;
        case FromWeixinHelpMessage.TYPE_FRONCLIENTMSGMESSAGE_SUB_SIZE:
            // FromClientMsgMessage发送未响应数
            break;
        case FromWeixinHelpMessage.TYPE_WPDY_FRONCLIENTMSGMESSAGE_SUB_SIZE:
            // WpdyFromClientMsgMessage发送未响应数
            break;
        case FromWeixinHelpMessage.TYPE_GET_CURRENT_LOG:
            // 获取日志文件
            getCurrentLog();
            break;
        case FromWeixinHelpMessage.TYPE_SET_LOG_LEVEL:
            // 设置日志级别
            setLogLevel(message.getMessage());
            break;
        }
    }

    private void setLogLevel(String message) {
        if (Validators.isEmpty(message)) {
            responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS, "请指定要设置的类的日志级别。"));
            return;
        }

        String[] classAndLevel = message.split(":");
        if (null == classAndLevel || classAndLevel.length != 2) {
            responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS,
                    "设置级别参数不对，格式这样：net.zdsoft.weixinserver.Catalina:DEBUG"));
            return;
        }

        LogbackUtils.setLoggerLevel(classAndLevel[0], Level.toLevel(classAndLevel[1]));
        responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS, "设置成功，记得调试好后再设置回来。级别如下："
                + classAndLevel[0] + ":" + classAndLevel[1]));
    }

    private void getCurrentLog() {
        File currentFile = LogbackUtils.getLogFile("weixinserver.log");
        if (null == currentFile) {
            responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS, "日志文件不存在哦。"));
        }
        else {
            try {
                byte[] meesageBytes = FileUtils.readFileToByteArray(currentFile);
                AbstractMessage[] splitedMsgs = AbstractMessage.splitBigMessage(new GetLogMessage(meesageBytes));

                responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS, "正在传送日志，请等一下。"));
                WeiXinServerSendHelper.sendSplitedMessages(getActionContext().getLoginId(), getActionContext()
                        .getMessageId(), splitedMsgs);
            }
            catch (Exception e) {
                responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS, "读取日志文件异常。原因："
                        + e));
            }
        }
    }



    private void jvmMemory() {
        Runtime runtime = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();
        sb.append("试图使用最大内存[" + runtime.maxMemory() / 1024 / 1024 + "M]。");
        sb.append("虚拟机中内存总量[" + runtime.totalMemory() / 1024 / 1024 + "M]。");
        sb.append("虚拟机中空闲内存[" + runtime.freeMemory() / 1024 / 1024 + "M]。");
        sb.append("可用处理器数目[" + runtime.availableProcessors() + "个]。");
        responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS, sb.toString()));
    }



    private boolean checkMd5(String md5, String fromAccountId) {
        String dateStr = DateUtils.date2StringByDay(new Date());
        String md5Real = SecurityUtils.encodeByMD5(dateStr + "15858178400158581784001111111111");
        return md5Real.equals(md5);
    }

    private void onlineNum() {
        int sessionSize = WxSessionManager.getInstance().getSessionSize();
        responseMessage(new FromWeixinHelpRespMessage(FromWeixinHelpRespMessage.TYPE_SUCCESS, "accountId在线人数["
                + sessionSize + "]。"));
    }

}
