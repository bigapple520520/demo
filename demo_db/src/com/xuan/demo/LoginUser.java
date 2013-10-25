/* 
 * @(#)LoginUser.java    Created on 2013-5-3
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登录用户信息
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-3 下午7:35:02 $
 */
public class LoginUser implements Serializable {
    private static final long serialVersionUID = -4224873711924646060L;

    private String username;
    private String password;
    private Date creationTime;
    private Date modifyTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("username[" + username + "]");
        sb.append("password[" + password + "]");
        sb.append("creationTime[" + date2StringBySecond(creationTime) + "]");
        sb.append("modifyTime[" + date2StringBySecond(modifyTime) + "]");

        return sb.toString();
    }

    private String date2StringBySecond(Date date) {
        if (date == null) {
            return null;
        }
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

}
