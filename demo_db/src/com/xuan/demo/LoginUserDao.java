/* 
 * @(#)LoginUserAdapter.java    Created on 2013-5-3
 * Copyright (c) 2013 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.xuan.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;

import com.xuan.demo.db.BasicDao;
import com.xuan.demo.db.callback.MapRowMapper;
import com.xuan.demo.db.callback.MultiRowMapper;
import com.xuan.demo.db.callback.SingleRowMapper;
import com.xuan.demo.db.helper.SqlCreator;

/**
 * 登录用户的数据库操作
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-3 下午7:37:12 $
 */
public class LoginUserDao extends BasicDao {
    public LoginUserDao(Context context) {
        super(context);
    }

    private static final String INSERT_LOGINUSER = "INSERT INTO login_user(username, password, creation_time, modify_time) VALUES(?,?,?,?)";
    private static final String DELETE_LOGINUSER = "DELETE FROM login_user where username = ?";
    private static final String FIND_LOGINUSER = "SELECT username, password, creation_time, modify_time FROM login_user";
    private static final String FIND_LOGINUSER_BY_USERNAME_IN = "SELECT username, password, creation_time, modify_time FROM login_user WHERE username IN";

    /**
     * 插入数据
     * 
     * @param loginUser
     */
    public void insertLoginUser(LoginUser loginUser) {
        if (null == loginUser) {
            return;
        }

        update(INSERT_LOGINUSER, new Object[] { loginUser.getUsername(), loginUser.getPassword(),
                date2StringBySecond(loginUser.getCreationTime()), date2StringBySecond(loginUser.getModifyTime()) });
    }

    /**
     * 删除数据
     * 
     * @param username
     */
    public void deleteLoginUserByUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return;
        }

        update(DELETE_LOGINUSER, new Object[] { username });
    }

    /**
     * 单个查询
     * 
     * @param username
     * @return
     */
    public LoginUser findLoginUserListByUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            return null;
        }

        SqlCreator sqlCreator = new SqlCreator(FIND_LOGINUSER, false);
        sqlCreator.and("username = ?", username, true);
        return query(sqlCreator.getSQL(), sqlCreator.getArgs(), new SingleRowMapper<LoginUser>() {
            @Override
            public LoginUser mapRow(Cursor rs) throws SQLException {
                LoginUser loginUser = new LoginUser();
                loginUser.setUsername(rs.getString(rs.getColumnIndex("username")));
                loginUser.setPassword(rs.getString(rs.getColumnIndex("password")));
                loginUser.setCreationTime(string2DateTime(rs.getString(rs.getColumnIndex("creation_time"))));
                loginUser.setModifyTime(string2DateTime(rs.getString(rs.getColumnIndex("modify_time"))));
                return loginUser;
            }
        });
    }

    /**
     * 列表查询
     * 
     * @param username
     * @return
     */
    public List<LoginUser> findLoginUserList() {
        return query(FIND_LOGINUSER, null, new MultiRowMapper<LoginUser>() {
            @Override
            public LoginUser mapRow(Cursor rs, int arg1) throws SQLException {
                LoginUser loginUser = new LoginUser();
                loginUser.setUsername(rs.getString(rs.getColumnIndex("username")));
                loginUser.setPassword(rs.getString(rs.getColumnIndex("password")));
                loginUser.setCreationTime(string2DateTime(rs.getString(rs.getColumnIndex("creation_time"))));
                loginUser.setModifyTime(string2DateTime(rs.getString(rs.getColumnIndex("modify_time"))));
                return loginUser;
            }
        });
    }

    /**
     * 返回map的查询
     * 
     * @return
     */
    public Map<String, LoginUser> findUsername2UserMap() {
        return query(FIND_LOGINUSER, null, new MapRowMapper<String, LoginUser>() {
            @Override
            public String mapRowKey(Cursor rs, int n) throws java.sql.SQLException {
                return rs.getString(rs.getColumnIndex("username"));
            }

            @Override
            public LoginUser mapRowValue(Cursor rs, int n) throws java.sql.SQLException {
                LoginUser loginUser = new LoginUser();
                loginUser.setUsername(rs.getString(rs.getColumnIndex("username")));
                loginUser.setPassword(rs.getString(rs.getColumnIndex("password")));
                loginUser.setCreationTime(string2DateTime(rs.getString(rs.getColumnIndex("creation_time"))));
                loginUser.setModifyTime(string2DateTime(rs.getString(rs.getColumnIndex("modify_time"))));
                return loginUser;
            }
        });
    }

    /**
     * 用IN查询
     * 
     * @param username
     * @return
     */
    public List<LoginUser> findLoginUserByUsernames(String... username) {
        if (null == username) {
            return Collections.emptyList();
        }

        return queryForInSQL(FIND_LOGINUSER_BY_USERNAME_IN, null, username, null, new MultiRowMapper<LoginUser>() {
            @Override
            public LoginUser mapRow(Cursor rs, int arg1) throws java.sql.SQLException {
                LoginUser loginUser = new LoginUser();
                loginUser.setUsername(rs.getString(rs.getColumnIndex("username")));
                loginUser.setPassword(rs.getString(rs.getColumnIndex("password")));
                loginUser.setCreationTime(string2DateTime(rs.getString(rs.getColumnIndex("creation_time"))));
                loginUser.setModifyTime(string2DateTime(rs.getString(rs.getColumnIndex("modify_time"))));
                return loginUser;
            }
        });
    }

    private String date2StringBySecond(Date date) {
        if (date == null) {
            return null;
        }
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

    public Date string2DateTime(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(str);
        }
        catch (ParseException e) {
            // ignore
        }
        return date;
    }

}
