package com.xuan.weixinserver.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.zdsoft.keel.jdbc.JdbcBasicDao;
import net.zdsoft.keel.jdbc.MultiRowMapper;
import net.zdsoft.keel.jdbc.SingleRowMapper;
import net.zdsoft.keel.util.Validators;

import org.springframework.stereotype.Repository;

import com.xuan.weixinserver.dao.DemoDao;
import com.xuan.weixinserver.entity.AccountExt;

/**
 *
 * 账号信息扩展
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午10:51:16 $
 */
@Repository
public class DemotDaoImpl extends JdbcBasicDao implements DemoDao {
    private static final String FIND_ACCOUNTEXT_BY_ACCOUNTID = "SELECT * FROM mp_account_ext WHERE account_id=?";

    @Override
    public AccountExt findAccountExtByAccountId(String accountId) {
        if (Validators.isEmpty(accountId)) {
            return null;
        }

        return query(FIND_ACCOUNTEXT_BY_ACCOUNTID, new Object[] { accountId }, new MSingleRowMapper());
    }

    /**
     * 单条结果集
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-6-26 下午4:50:56 $
     */
    private class MSingleRowMapper implements SingleRowMapper<AccountExt> {
        @Override
        public AccountExt mapRow(ResultSet rs) throws SQLException {
            return new MMultiRowMapper().mapRow(rs, 0);
        }
    }

    /**
     * 多条结果集
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-5-26 下午1:07:21 $
     */
    private class MMultiRowMapper implements MultiRowMapper<AccountExt> {
        @Override
        public AccountExt mapRow(ResultSet rs, int rowNum) throws SQLException {
            AccountExt accountExt = new AccountExt();
            accountExt.setAccountId(rs.getString("account_id"));
            return accountExt;
        }
    }

}
