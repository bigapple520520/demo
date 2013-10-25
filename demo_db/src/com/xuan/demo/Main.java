package com.xuan.demo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Main extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        StringBuilder sb = new StringBuilder();

        LoginUserDao loginUserDao = new LoginUserDao(this);

        // 删除
        loginUserDao.deleteLoginUserByUsername("xuan");
        loginUserDao.deleteLoginUserByUsername("xuan2");

        // 添加查询
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("xuan");
        loginUser.setPassword("123456");
        loginUser.setCreationTime(new Date());
        loginUser.setModifyTime(new Date());
        loginUserDao.insertLoginUser(loginUser);

        LoginUser loginUser2 = new LoginUser();
        loginUser2.setUsername("xuan2");
        loginUser2.setPassword("56789");
        loginUser2.setCreationTime(new Date());
        loginUser2.setModifyTime(new Date());
        loginUserDao.insertLoginUser(loginUser2);

        // 批量查询查询
        List<LoginUser> list = loginUserDao.findLoginUserList();
        sb.append("批量查询查询:" + list + "\n\n");

        // 单个查询
        LoginUser object = loginUserDao.findLoginUserListByUsername("xuan");
        sb.append("单个查询xuan的记录:" + object.toString() + "\n\n");

        // 返回map查询
        Map<String, LoginUser> map = loginUserDao.findUsername2UserMap();
        sb.append("查询全部map:" + map + "\n\n");

        // IN条件查询
        List<LoginUser> list4In = loginUserDao.findLoginUserByUsernames("xuan2");
        sb.append("用IN条件查询xuan2的记录:" + list4In + "\n\n");

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(sb.toString());
    }

}
