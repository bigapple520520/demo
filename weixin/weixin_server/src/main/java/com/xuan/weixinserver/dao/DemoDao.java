/*
 * @(#)AccountExtDao.java    Created on 2013-5-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.dao;

import com.xuan.weixinserver.entity.AccountExt;

/**
 * 账号信息扩展
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午10:49:07 $
 */
public interface DemoDao {

    /**
     * 根据accountId查询
     *
     * @param accountId
     * @return
     */
    AccountExt findAccountExtByAccountId(String accountId);

}
