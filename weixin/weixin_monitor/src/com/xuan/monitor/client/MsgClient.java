package com.xuan.monitor.client;

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
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.winupon.base.wpcf.WpcfClient;
import com.winupon.base.wpcf.WpcfClientHandler;
import com.winupon.base.wpcf.WpcfException;
import com.winupon.base.wpcf.listener.DisconnectedListener;
import com.winupon.base.wpcf.util.DateUtils;
import com.winupon.base.wpcf.util.SecurityUtils;
import com.winupon.base.wpcf.util.UUIDUtils;
import com.xuan.weixinserver.message.common.AbstractMessage;
import com.xuan.weixinserver.message.common.SplitedMessage;
import com.xuan.weixinserver.wx.action.ActionContext;
import com.xuan.weixinserver.wx.action.ActionInvoker;

/**
 * 与服务器连接的客户端
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2012-9-19 下午01:47:24 $
 */
public class MsgClient {
	private final Logger log = LoggerFactory.getLogger(getClass());

    private static final MsgClient instance = new MsgClient();

    private volatile WpcfClient client;

    private volatile boolean isClosed = true;// 是否已经关闭

    private volatile int loginFailTimes = 0;// 连续的登录失败次数

    // 每条消息对应的分割消息集合
    private final ConcurrentMap<String, ConcurrentMap<Integer, SplitedMessage>> splitedMessageMap = new ConcurrentHashMap<String, ConcurrentMap<Integer, SplitedMessage>>();

    // 每次收到一个分割的消息，会更新改时间记录，因为如果一个消息6分钟还没收到分割消息，就会被清理掉
    private final ConcurrentMap<String, Date> splitedMessageHelpMap = new ConcurrentHashMap<String, Date>();

    // 存放响应消息的地方
    private final ConcurrentMap<String, AbstractMessage> messageId2RespMsgMap = new ConcurrentHashMap<String, AbstractMessage>();

    // 存放获取大图进度的地方
    private final ConcurrentMap<String, Integer> messageId2PercentMap = new ConcurrentHashMap<String, Integer>();

    // 清理splitedMessageMap的任务
    private Thread cleanSplitedMessageMapThread;

    private MsgClient() {
    }

    public static MsgClient getInstance() {
        return instance;
    }

    /**
     * 初始化客户端
     *
     * @param serverAddr
     *            服务器地址
     * @param serverPort
     *            服务器端口
     * @param accountId
     *            登陆人的账号
     * @param token
     *            票据（tocc有两种登陆方式：密码或票据）
     */
    public void init(String serverAddr, int serverPort, String loginId, String token) {
        if (!isClosed) {
            return;
        }

        isClosed = false;
        client = new WpcfClient("weixinclient", serverAddr, serverPort, loginId, "", new WeixinClientHandler(), 30, 20,null);// 设置，业务逻辑处理线程池数30，心跳时间间隔为20秒
        client.setToken(token);

        /* 设置断开了服务器连接的监听 */
        client.setDisconnectedListener(new MDisconnectedListener());

        /* 连接微信服务器，如果成功发送准备消息 */
        try {
        	if(client.connect()){
        		log.debug("客户端连上喽");
        	}else{
        		log.error("客户端没有连上，请保持服务器端正在运行");
        		return;
        	}
        }
        catch (WpcfException e) {
        	log.error("连接微信服务器异常，原因：" + e.getMessage(), e);
            return;
        }

        /* 开启分割消息清理线程 */
        if (null == cleanSplitedMessageMapThread) {
            cleanSplitedMessageMapThread = new CleanSplitedMessageMapThread();
            cleanSplitedMessageMapThread.start();
        }
    }

    /**
     * 断开连接，断开后可以重新连接
     */
    public void reconnect() {
    	log.error("连接微信服务器被断开，client被close，此client可以通过connect重新连接");
        if (null != client) {
            client.close();
        }
    }

    /**
     * 关闭连接，关闭后不能在重新连接，要重新初始化一个client出来
     */
    public void close() {
        if (isClosed) {
            return;
        }

        log.error("连接微信服务器被关闭，client被dispose，此client不能再被connect，需要重新初始化");
        isClosed = true;

        // 中断分割消息清理线程
        if (null != cleanSplitedMessageMapThread) {
            cleanSplitedMessageMapThread.interrupt();
            cleanSplitedMessageMapThread = null;
        }

        if (null != client) {
            client.dispose();
        }
    }

    /**
     * 阻塞以等待关闭完成，本身不执行关闭操作
     */
    public void waitClose() {
        if (null == client || isClosed) {
            return;
        }
    }

    /**
     * 判断是否是登陆状态
     *
     * @return
     */
    public boolean isLogined() {
        if (null == client) {
            return false;
        }

        return client.isLogined();
    }

    /**
     * 大消息，阻塞并返回响应消息
     *
     * @param messageId
     * @param message
     * @param timeOutMillis
     *            超时时间
     * @return
     * @throws TimeoutException
     */
    public AbstractMessage sendBigMessage2WaitResponse(String messageId, AbstractMessage message, long timeOutMillis)
            throws TimeoutException {
        if (isClosed) {
            return null;
        }

        long startTimeMillis = System.currentTimeMillis();
        // 生成消息的唯一标识
        if (null == messageId) {
            messageId = UUIDUtils.createId();
        }
        messageId = messageId.intern();

        // 用空消息占位
        messageId2RespMsgMap.put(messageId, NullMessage.NULL);

        // 发送消息
        AbstractMessage[] splitedMessages = AbstractMessage.splitBigMessage(message);
        for (AbstractMessage m : splitedMessages) {
            boolean success = sendMessage(messageId, m);
            if (!success || Thread.currentThread().isInterrupted()) {
                messageId2RespMsgMap.remove(messageId);
                return null;// 一个发送失败则不再发送
            }
        }

        // 等待返回的消息
        synchronized (messageId) {
            // 如果还没收到消息就等待，直到timeOutMillis时间过去后就抛出异常
            while (messageId2RespMsgMap.get(messageId) == NullMessage.NULL) {
                try {
                    messageId.wait(timeOutMillis);
                    if (System.currentTimeMillis() - startTimeMillis >= timeOutMillis) {
                        AbstractMessage resp = messageId2RespMsgMap.remove(messageId);
                        if (resp == NullMessage.NULL) {
                            throw new TimeoutException("timeout");
                        }
                        else {
                            return resp;
                        }
                    }
                }
                catch (InterruptedException e) {
                    messageId2RespMsgMap.remove(messageId);
                    throw new TimeoutException("timeout");
                }
            }
        }

        // 基本上很快收到消息才会执行到这
        return messageId2RespMsgMap.remove(messageId);
    }

    /**
     * 发消息，阻塞并返回响应消息
     *
     * @param messageId
     * @param message
     * @param timeOutMillis
     *            超时时间
     * @return
     * @throws TimeoutException
     */
    public AbstractMessage sendMessage2WaitResponse(String messageId, AbstractMessage message, long timeOutMillis)
            throws TimeoutException {
        if (isClosed) {
            return null;
        }

        long startTimeMillis = System.currentTimeMillis();

        // 生成消息的唯一标识
        if (null == messageId) {
            messageId = UUIDUtils.createId();
        }
        messageId = messageId.intern();

        // 用空消息占位
        messageId2RespMsgMap.put(messageId, NullMessage.NULL);

        // 发送消息
        client.sendMessage(messageId, message.getCommand(), message.getBytes(),null);

        // 等待返回的消息
        synchronized (messageId) {
            // 如果还没收到消息就等待timeOutMillis
            while (messageId2RespMsgMap.get(messageId) == NullMessage.NULL) {
                try {
                    messageId.wait(timeOutMillis);
                    if (System.currentTimeMillis() - startTimeMillis >= timeOutMillis) {
                        AbstractMessage resp = messageId2RespMsgMap.remove(messageId);
                        if (resp == NullMessage.NULL) {
                            throw new TimeoutException("timeout");
                        }
                        else {
                            return resp;
                        }
                    }
                }
                catch (InterruptedException e) {
                    messageId2RespMsgMap.remove(messageId);
                    throw new TimeoutException("timeout");
                }
            }
        }

        // 基本上很快收到消息才会执行到这
        return messageId2RespMsgMap.remove(messageId);
    }

    /**
     * 阻塞，发送消息
     *
     * @param messageId
     * @param message
     * @return
     */
    public boolean sendMessage(String messageId, AbstractMessage message) {
        if (isClosed) {
            return false;
        }

        // 创建消息唯一编号
        if (StringUtils.isEmpty(messageId)) {
            messageId = UUIDUtils.createId();
        }

        // 发送消息，阻塞直到返回结果
        WriteFuture future = client.sendMessage(messageId, message.getCommand(), message.getBytes(),null);

        try {
            future.await();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!future.isDone() || !future.isWritten()) {
            return false;
        }

        return true;
    }

    /**
     * 发送被分割的多个消息,任何一个发送失败则认为发送失败(阻塞)
     *
     * @param abstractMessages
     * @return
     */
    public boolean sendSplitedMessages(String messageId, AbstractMessage... splitedMessages) {
        if (isClosed) {
        	log.error("发送消息时，碰到通讯服务器关闭着");
            return false;
        }

        log.debug("分割消息的数量："+splitedMessages.length);

        if (null == messageId) {
            messageId = UUIDUtils.createId();
        }

        for (AbstractMessage m : splitedMessages) {
            boolean success = sendMessage(messageId, m);

            if (!success) {
                return false;// 一个发送失败则不再发送
            }

            if (Thread.currentThread().isInterrupted()) {
                return false;
            }
        }

        return true;
    }

    public boolean isClosed(){
    	return isClosed;
    }

    // 根据连接失败的次数返回重连的间隔秒数
    private int getWaitSeconds() {
        loginFailTimes++;

        if (loginFailTimes <= 5) {
            return 3;
        }
        else if (loginFailTimes <= 10) {
            return 10;
        }
        else if (loginFailTimes <= 20) {
            return 60;
        }
        else {
            return 180;
        }
    }

    /**
     * 客户端处理接受消息接口实现
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2012-10-19 下午2:30:06 $
     */
    private class WeixinClientHandler implements WpcfClientHandler {
        @Override
        public void kickedOutByServer() {
            // 被同名剔除
        	log.debug("同一个LoginId在别处登录，这里被同名剔除");
            client.dispose();
        }

        // 封装分割的消息
        private AbstractMessage filterSplitedMessageToRealMessage(String messageId, SplitedMessage msg) {
            messageId = messageId.intern();
            ConcurrentMap<Integer, SplitedMessage> map = splitedMessageMap.get(messageId);
            if (map == null) {
                map = new ConcurrentHashMap<Integer, SplitedMessage>();
                ConcurrentMap<Integer, SplitedMessage> temp = splitedMessageMap.putIfAbsent(messageId, map);
                if (temp != null) {
                    map = temp;
                }
            }
            splitedMessageHelpMap.put(messageId, new Date());
            map.put(msg.getSequence(), msg);
            int percent = (int) (map.size() * 100.0d / msg.getSplitedNum());
            percent = percent >= 100 ? 99 : percent;
            messageId2PercentMap.put(messageId, percent);
            synchronized (messageId) {
                messageId.notifyAll();
            }

            SplitedMessage firstMsg = map.get(0);
            if (firstMsg == null) {
                return null;
            }
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

                log.debug("bs的长度："+bs.length);
                return AbstractMessage.fromBytes(firstMsg.getOriginalCommand(), bs);
            }

            return null;
        }

        @Override
        public void receivedMessage(IoSession session, String loginId, String messageId, int command, byte[] messageBody) {
            AbstractMessage message = AbstractMessage.fromBytes(command, messageBody);
            if (null == message) {
            	log.error("消息未null错误，请在AbstractMessage中注册对应的消息，谢谢");
                return;
            }

            messageId = messageId.intern();

            // 分割的消息
            if (message instanceof SplitedMessage) {
                SplitedMessage sm = (SplitedMessage) message;
                log.debug("总共包：" + sm.getSplitedNum() + ",收到第几个包：" + sm.getSequence() + ",原始长度："
                        + sm.getOriginalLength() + ",校验值MD5：" + sm.getMd5());

                AbstractMessage temp = filterSplitedMessageToRealMessage(messageId, sm);

                if (null == temp) {// 如果返回null表示分割消息还不完整
                    return;
                }

                // 收集完成分割的消息
                message = temp;
            }

            // 执行策略
            new ActionInvoker(message, new ActionContext(loginId, messageId)).dealMessage();
        }

        @Override
        public void messageResponsed(String serverName, String messageId, int command, byte[] messageBody) {
            AbstractMessage message = AbstractMessage.fromBytes(command, messageBody);
            if (null == message) {
            	log.error("消息未null错误，请在AbstractMessage中注册对应的消息，谢谢");
                return;
            }

            messageId = messageId.intern();
            synchronized (messageId) {
                if (messageId2RespMsgMap.containsKey(messageId)) {
                    messageId2RespMsgMap.put(messageId, message);
                    messageId.notifyAll();
                }
            }
        }
    }

    /**
     * 分割消息清理线程类
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-7-16 下午6:25:50 $
     */
    private class CleanSplitedMessageMapThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(300);
                }
                catch (InterruptedException e) {
                	log.error("分割消息清理线程被中断，原因：" + e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }

                /* 中断返回 */
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                try {
                    for (Map.Entry<String, Date> entry : splitedMessageHelpMap.entrySet()) {
                        // 如果6分钟内没有新消息进入map，则删除之
                        if (DateUtils.addMinute(entry.getValue(), 6).before(new Date())) {
                            splitedMessageMap.remove(entry.getKey());
                            splitedMessageHelpMap.remove(entry.getKey());
                            messageId2PercentMap.remove(entry.getKey());
                        }
                    }
                }
                catch (Exception e) {
                	log.error("清理消息异常，原因：" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 断开服务连接处理
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-7-16 下午6:15:54 $
     */
    private class MDisconnectedListener implements DisconnectedListener {
        @Override
        public void onDisconnected() {
            /* 如果是由于同名被剔除的，这个监听也会被调用，所以不再发起重连 */
            if (client.isKickedOutByServer()) {
                return;
            }

            /* 如果客户端状态已近是关闭的，不再发起重连 */
            if (isClosed) {
                return;
            }

            int waitSeconds = getWaitSeconds();
            log.debug("断开连接，如果存在网络则" + waitSeconds + "S后重新连接");

            /* 等待网络的恢复 */
            try {
                Thread.sleep(waitSeconds * 1000);
            }
            catch (InterruptedException e) {
            	log.error("线程等待时被中断异常，原因：" + e.getMessage(), e);
                Thread.currentThread().interrupt();
            }

            /* 中断返回 */
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            /* 尝试重连服务器 */
            new Thread() {
                @Override
                public void run() {
                    try {
                        if (client.connect()) {
                            loginFailTimes = 0;
                        }
                    }
                    catch (WpcfException e) {
                    	log.error("重连微信服务器失败，原因：" + e.getMessage(), e);
                        return;
                    }
                }
            }.start();
        }
    }
}
