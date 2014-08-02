/*
 * @(#)AccountExt.java    Created on 2013-5-9
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.weixinserver.entity;

import java.util.Date;

/**
 * 账号扩张信息号
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-9 下午7:57:20 $
 */
public class AccountExt {
    private String accountId;
    private String photoUrl;
    private Date creationTime;
    private int starLevel;
    private String remark;
    private double balance;
    private String drawPassword;
    private int role;// 0普通学生1认证老师

    private String code;// 用户的所属编号
    private int questionNum;// 学生每日提问的限制（宁波）
    private Date lastTime;// 每次提问的时间（宁波）

    private int score;// 积分

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(int starLevel) {
        this.starLevel = starLevel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getDrawPassword() {
        return drawPassword;
    }

    public void setDrawPassword(String drawPassword) {
        this.drawPassword = drawPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
