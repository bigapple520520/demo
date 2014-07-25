/*
 * @(#)WeiXinServer.java    Created on 2012-9-11
 * Copyright (c) 2012 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import net.zdsoft.keel.util.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winupon.base.wpcf.WpcfServer;
import com.winupon.base.wpcf.WpcfServerHandler;
import com.winupon.base.wpcf.util.SecurityUtils;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.message.common.SplitedMessage;
import com.xuan.weixinserver.service.DemoService;
import com.xuan.weixinserver.wx.action.ActionContext;
import com.xuan.weixinserver.wx.action.ActionInvoker;
import com.xuan.weixinserver.wx.session.WxSessionManager;

/**
 * 微信服务器服务端
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-2-28 上午10:27:00 $
 */
public class WeiXinServer {
    private static final Logger log = LoggerFactory.getLogger(WeiXinServer.class);

    @Resource
    private DemoService accountExtService;
    private static WpcfServer wpcfServer;

    // 缓存分割的消息
    private final ConcurrentMap<String, ConcurrentMap<Integer, SplitedMessage>> splitedMessageMap = new ConcurrentHashMap<String, ConcurrentMap<Integer, SplitedMessage>>();
    private final ConcurrentMap<String, Date> splitedMessageHelpMap = new ConcurrentHashMap<String, Date>();// 每进一次消息，就更新时间

    // 开启服务的入口处
    public void startServer() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e) {
            log.error("未知域名异常，原因：" + e.getMessage(), e);
        }

        // 运行服务端
        int port = 10000;
        int processThreadCount = 30;
        wpcfServer = new WpcfServer("weiXinServer:" + hostName, port, new WeiXinServerHandler(), processThreadCount,
                30, false);// 心跳设置30秒

        /* 启动清理失效的分割消息，任务每隔5分钟进行一次扫描清理，清理6分钟内没有被处理的分割消息 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MINUTES.sleep(5);
                    }
                    catch (InterruptedException e) {
                        log.error("处理分割消息的线程被中断，原因：" + e.getMessage(), e);
                    }

                    try {
                        for (Map.Entry<String, Date> entry : splitedMessageHelpMap.entrySet()) {
                            // 删除6分钟内没有新进消息片段的消息
                            if (DateUtils.addMinute(entry.getValue(), 6).before(new Date())) {
                                splitedMessageMap.remove(entry.getKey());
                                splitedMessageHelpMap.remove(entry.getKey());
                            }
                        }
                    }
                    catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }).start();

        /* 注册jvm关闭钩子，当jvm要关闭前先会做完这个任务才会关闭，在这我们关闭服务器 */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    wpcfServer.close();
                }
                catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    // 服务器端的处理逻辑，接口回调的形式传入
    private class WeiXinServerHandler implements WpcfServerHandler {
        @Override
        public void receivedMessage(IoSession session, String loginId, String messageId, int command, byte[] messageBody) {
            String cId = Integer.toHexString(command);
            log.debug("登录用户[{}]，收到消息，command={}", loginId, cId);

            AbstractMessage message = AbstractMessage.fromBytes(command, messageBody);
            if (null == message) {
                log.error("登录用户[{}]，消息command=[{}]是空错误。原因：检查是否在AbstractMessage注册了消息类。", loginId, cId);
                return;
            }

            if (message instanceof SplitedMessage) {
                // 分割消息处理
                AbstractMessage temp = filterSplitedMessageToRealMessage(messageId, (SplitedMessage) message);
                if (null == temp) {
                    return;
                }

                message = temp;
            }

            new ActionInvoker(message, new ActionContext(loginId, messageId)).dealMessage();
        }

        @Override
        public void messageResponsed(String loginId, String messageId, int command, byte[] messageBody) {
            log.debug("登录用户[{}]，收到响应command=[{}]消息。", loginId, Integer.toHexString(command));

            AbstractMessage message = AbstractMessage.fromBytes(command, messageBody);
            if (null == message) {
                log.error("登录用户[{}]，响应消息command=[{}]为空错误。原因：该响应消息没有在AbstractMessage中配置。", loginId,
                        Integer.toHexString(command));
                return;
            }

            new ActionInvoker(message, new ActionContext(loginId, messageId)).dealMessage();
        }

        @Override
        public boolean verifyToken(String loginId, String token, byte[] otherMsg) {
        	return StringUtils.equals(SecurityUtils.encodeByMD5(loginId), token);
        }

        @Override
        public void onUserSessionClosed(String loginId) {
            WxSessionManager.getInstance().removeSession(loginId);
            log.debug("用户退出：loginId={}", new String[] { loginId });
        }

        @Override
        public String getUserPassword(String userName, byte[] arg1) {
            log.debug("验证密码，用户名：{}", userName);
            return SecurityUtils.encodeByMD5(userName);
        }

        // 把消息片段组装成整体的一个消息，如果片段消息没有收集全就缓存起来
        private AbstractMessage filterSplitedMessageToRealMessage(String messageId, SplitedMessage msg) {
            ConcurrentMap<Integer, SplitedMessage> map = splitedMessageMap.get(messageId);

            /* 如果是null表示该消息片段第一次进来，就给该消息分配存储空间 */
            if (map == null) {
                map = new ConcurrentHashMap<Integer, SplitedMessage>();
                ConcurrentMap<Integer, SplitedMessage> temp = splitedMessageMap.putIfAbsent(messageId, map);
                if (temp != null) {
                    map = temp;
                }
            }

            splitedMessageHelpMap.put(messageId, new Date());
            map.put(msg.getSequence(), msg);
            log.debug("收到分割消息，分割总数={}, 已收到={},原始值={" + msg.getOriginalLength() + "},校验MD5={" + msg.getMd5() + "}",
                    msg.getSplitedNum(), map.size());

            SplitedMessage firstMsg = map.get(0);
            if (firstMsg == null) {
                return null;
            }

            /* 片段消息都接受完毕后，合并片段消息成整个消息 */
            if (firstMsg.getSplitedNum() == map.size()) {
                splitedMessageMap.remove(messageId);
                splitedMessageHelpMap.remove(messageId);
                List<SplitedMessage> list = new ArrayList<SplitedMessage>(map.values());

                Collections.sort(list, new Comparator<SplitedMessage>() {
                    @Override
                    public int compare(SplitedMessage o1, SplitedMessage o2) {
                        return o1.getSequence() - o2.getSequence();
                    }
                });

                ByteBuffer buf = ByteBuffer.allocate(firstMsg.getOriginalLength());

                for (SplitedMessage m : list) {
                    buf.put(m.getBody());
                }

                buf.flip();
                byte[] bs = new byte[buf.remaining()];
                buf.get(bs);

                if (bs.length != firstMsg.getOriginalLength()) {
                    return null;
                }

                String md5 = SecurityUtils.encodeByMD5(bs);
                if (!md5.equals(firstMsg.getMd5())) {
                    log.error("md5不一致");
                    return null;
                }

                return AbstractMessage.fromBytes(firstMsg.getOriginalCommand(), bs);
            }

            return null;
        }

    }

    /**
     * 获取socket的服务器端
     *
     * @return
     */
    public static WpcfServer getWpcfServer() {
        return wpcfServer;
    }

}
