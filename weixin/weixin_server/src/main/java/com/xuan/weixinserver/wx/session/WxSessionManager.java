/*
 * @(#)WxSessionManager.java    Created on 2014-6-22
 * Copyright (c) 2014 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.wx.session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.zdsoft.keel.util.Validators;

/**
 * 登录用户连接管理
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-6-22 下午1:56:51 $
 */
public class WxSessionManager {
    private static final WxSessionManager instance = new WxSessionManager();

    private WxSessionManager() {
    }

    public static WxSessionManager getInstance() {
        return instance;
    }

    /**
     * 其中key表示用户的登录标识，而value表示用户的账号accountId<br>
     * 如果需要考虑保存登录用户的更多信息，可以定义一个Session类做value值，当前业务没有涉及就暂存放accountId<br>
     * 注意当前业务，登录标识就是用了accountId，所以这两个值是一样的
     */
    private final ConcurrentHashMap<String, WxSession> loginId2SessionMap = new ConcurrentHashMap<String, WxSession>();

    /**
     * 存入session
     *
     * @param loginedId
     * @param accountId
     */
    public void putSession(String loginId, WxSession session) {
        loginId2SessionMap.putIfAbsent(loginId, session);
    }

    /**
     * 判断是否有session
     *
     * @param loginedId
     * @return
     */
    public boolean hasSession(String loginId) {
    	if(Validators.isEmpty(loginId)){
    		return false;
    	}

        return loginId2SessionMap.containsKey(loginId);
    }

    /**
     * 获取session
     *
     * @param loginId
     * @return
     */
    public WxSession getSession(String loginId) {
        return loginId2SessionMap.get(loginId);
    }

    /**
     * 删除session
     *
     * @param loginId
     * @return
     */
    public WxSession removeSession(String loginId) {
        return loginId2SessionMap.remove(loginId);
    }

    /**
     * 获取session数
     *
     * @return
     */
    public int getSessionSize() {
        return loginId2SessionMap.size();
    }

    // ----------------------------------------accountId相关
    /**
     * 根据AccountId判断session是否存在，<br>
     * 注意：当前因为loginId=accountId。所以方法同hasSession
     *
     * @param accountId
     * @return
     */
    public boolean hasSessionByAccountId(String accountId) {
        return hasSession(accountId);
    }

    /**
     * 根据accountId获取loginId<br>
     * 注意：当前因为loginId=accountId。所以直接返回accountId
     *
     * @param accountId
     * @return
     */
    public String getLoginIdByAccountId(String accountId) {
        return accountId;
    }

    /**
     * 获取所有在线的accountId<br>
     * 注意：当前因为loginId=accountId。所以直接返回loginId的集合
     *
     * @param accountId
     * @return
     */
    public Set<String> getAccountIdSet() {
        return loginId2SessionMap.keySet();
    }

    /**
     * 根据登录loginId或者这个人的accountId。<br>
     * 注意：当前因为loginId=accountId。所以直接返回loginId
     *
     * @param loginId
     * @return
     */
    public String getAccountIdByLoginId(String loginId) {
        return loginId;
    }

}
