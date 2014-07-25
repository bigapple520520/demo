/*
 * @(#)AccountExtService.java    Created on 2013-5-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.service;

import com.xuan.weixinserver.entity.AccountExt;

/**
 * 账号扩展信息
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午11:00:50 $
 */
public interface DemoService {


    /**
     * 根据accountId查询
     *
     * @param accountId
     * @return
     */
    AccountExt getAccountExtByAccountId(String accountId);
}

