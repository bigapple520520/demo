/*
 * @(#)AccountExtDao.java    Created on 2013-5-10
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.dao;

import java.util.Map;

import com.xuan.weixinserver.entity.AccountExt;

/**
 * 账号信息扩展
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午10:49:07 $
 */
public interface AccountExtDao {

    /**
     * 插入账号信息
     *
     * @param accountExt
     */
    void insertAccountExt(AccountExt accountExt);

    /**
     * 根据accountId查询
     *
     * @param accountId
     * @return
     */
    AccountExt findAccountExtByAccountId(String accountId);

    /**
     * 批量查询用户头像地址
     *
     * @param accountIds
     * @return
     */
    Map<String, AccountExt> findAccountId2AccountExtMapByAccountIds(String... accountIds);

    /**
     * 修改头像地址
     *
     * @param accountId
     * @param photoUrl
     */
    void updateAccountExtPhotoUrlByAccountId(String accountId, String photoUrl);

    /**
     * 加减余额
     *
     * @param amount
     * @param accountId
     */
    void update4AddBalanceByAccountId(long amount, String accountId);

    /**
     * 加减积分
     *
     * @param score
     * @param accountId
     */
    void update4AddScoreByAccountId(int score, String accountId);

    /**
     * 提问扣钱
     *
     * @param amount
     * @param accountId
     * @return
     */
    boolean updateBalance4Question(long amount, String accountId);

    /**
     * 提问扣积分
     *
     * @param score
     * @param accountId
     * @return
     */
    boolean updateScore4Question(int score, String accountId);

    /**
     * 更新答疑教师扩展中的星星评价
     *
     * @param accountId
     * @param starLevel
     */
    void updateStarLevelByAccountId(String accountId, int starLevel);

    /**
     * 更新提问的限额
     *
     * @param accountExt
     */
    void updateQuestionNumAndLastTimeByAccountId(AccountExt accountExt);

}
