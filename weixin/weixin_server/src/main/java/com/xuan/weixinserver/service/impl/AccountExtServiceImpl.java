/*
 * @(#)AccountExtServiceImpl.java    Created on 2013-5-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuan.weixinserver.dao.AccountExtDao;
import com.xuan.weixinserver.entity.AccountExt;
import com.xuan.weixinserver.service.AccountExtService;

/**
 * 账号扩展信息
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午11:02:12 $
 */
@Service
public class AccountExtServiceImpl implements AccountExtService {
    @Resource
    private AccountExtDao accountExtDao;

    @Override
    public AccountExt getAccountExtByAccountId(String accountId) {
        return accountExtDao.findAccountExtByAccountId(accountId);
    }

}
