package com.xuan.weixinserver.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import net.zdsoft.keel.jdbc.JdbcBasicDao;
import net.zdsoft.keel.jdbc.MapRowMapper;
import net.zdsoft.keel.jdbc.MultiRowMapper;
import net.zdsoft.keel.jdbc.SingleRowMapper;
import net.zdsoft.keel.util.Validators;

import org.springframework.stereotype.Repository;

import com.xuan.weixinserver.dao.AccountExtDao;
import com.xuan.weixinserver.entity.AccountExt;

/**
 *
 * 账号信息扩展
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-5-10 上午10:51:16 $
 */
@Repository
public class AccountExtDaoImpl extends JdbcBasicDao implements AccountExtDao {
    private static final String INSERT_ACCOUNTEXT = "INSERT INTO mp_account_ext(account_id, photo_url, creation_time,star_level,remark,balance) VALUES(?,?,?,?,?,?)";
    private static final String FIND_ACCOUNTEXT_BY_ACCOUNTID = "SELECT * FROM mp_account_ext WHERE account_id=?";
    private static final String UPDATE_PHOTOURL_BY_ACCOUNTID = "UPDATE mp_account_ext SET photo_url=? WHERE account_id=?";
    private static final String FIND_ACCOUNTEXT_BY_ACCOUNT_IN = "SELECT account_id, photo_url FROM mp_account_ext WHERE account_id IN";
    private static final String UPDATE_4_ADD_BALANCE_BY_ACCOUNTID = "UPDATE mp_account_ext SET balance = balance + ? WHERE account_id=?";
    private static final String UPDATE_4_ADD_SCORE_BY_ACCOUNTID = "UPDATE mp_account_ext SET score = score + ? WHERE account_id=?";
    private static final String UPDATE_BALANCE_4_QUESTION = "UPDATE mp_account_ext SET balance = balance - ? WHERE balance >=? AND account_id=?";
    private static final String UPDATE_SCORE_4_QUESTION = "UPDATE mp_account_ext SET score = score - ? WHERE score >=? AND account_id=?";
    private static final String UPDATE_STARLEVEL_BY_ACCOUNTID = "UPDATE mp_account_ext SET star_level=? WHERE account_id=?";
    private static final String UPDATE_QUESTIONNUM_AND_LASTTIME_BY_ACCOUNTID = "UPDATE mp_account_ext SET question_num=?,last_time=? WHERE account_id=?";

    @Override
    public void insertAccountExt(AccountExt accountExt) {
        update(INSERT_ACCOUNTEXT,
                new Object[] { accountExt.getAccountId(), accountExt.getPhotoUrl(), accountExt.getCreationTime(),
                        accountExt.getStarLevel(), accountExt.getRemark(), accountExt.getBalance() });
    }

    @Override
    public AccountExt findAccountExtByAccountId(String accountId) {
        if (Validators.isEmpty(accountId)) {
            return null;
        }

        return query(FIND_ACCOUNTEXT_BY_ACCOUNTID, new Object[] { accountId }, new MSingleRowMapper());
    }

    @Override
    public Map<String, AccountExt> findAccountId2AccountExtMapByAccountIds(String... accountIds) {
        if (Validators.isEmpty(accountIds)) {
            return Collections.emptyMap();
        }

        return queryForInSQL(FIND_ACCOUNTEXT_BY_ACCOUNT_IN, null, accountIds, new MapRowMapper<String, AccountExt>() {
            @Override
            public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("account_id");
            }

            @Override
            public AccountExt mapRowValue(ResultSet rs, int rowNum) throws SQLException {
                AccountExt accountExt = new AccountExt();
                accountExt.setAccountId(rs.getString("account_id"));
                accountExt.setPhotoUrl(rs.getString("photo_url"));
                return accountExt;
            }
        });
    }

    @Override
    public void updateAccountExtPhotoUrlByAccountId(String accountId, String photoUrl) {
        if (Validators.isEmpty(accountId)) {
            return;
        }

        update(UPDATE_PHOTOURL_BY_ACCOUNTID, new Object[] { photoUrl, accountId });
    }

    @Override
    public void update4AddBalanceByAccountId(long amount, String accountId) {
        if (Validators.isEmpty(accountId)) {
            return;
        }

        update(UPDATE_4_ADD_BALANCE_BY_ACCOUNTID, new Object[] { amount, accountId });
    }

    @Override
    public void update4AddScoreByAccountId(int score, String accountId) {
        if (Validators.isEmpty(accountId)) {
            return;
        }

        update(UPDATE_4_ADD_SCORE_BY_ACCOUNTID, new Object[] { score, accountId });
    }

    @Override
    public boolean updateBalance4Question(long amount, String accountId) {
        if (Validators.isEmpty(accountId) || amount < 0) {
            return false;
        }

        return update(UPDATE_BALANCE_4_QUESTION, new Object[] { amount, amount, accountId }) > 0;
    }

    @Override
    public boolean updateScore4Question(int score, String accountId) {
        if (Validators.isEmpty(accountId) || score < 0) {
            return false;
        }

        return update(UPDATE_SCORE_4_QUESTION, new Object[] { score, score, accountId }) > 0;
    }

    @Override
    public void updateStarLevelByAccountId(String accountId, int starLevel) {
        if (Validators.isEmpty(accountId)) {
            return;
        }

        update(UPDATE_STARLEVEL_BY_ACCOUNTID, new Object[] { starLevel, accountId });
    }

    @Override
    public void updateQuestionNumAndLastTimeByAccountId(AccountExt accountExt) {
        if (null == accountExt) {
            return;
        }

        update(UPDATE_QUESTIONNUM_AND_LASTTIME_BY_ACCOUNTID,
                new Object[] { accountExt.getQuestionNum(), accountExt.getLastTime(), accountExt.getAccountId() });
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
            accountExt.setPhotoUrl(rs.getString("photo_url"));
            accountExt.setCreationTime(rs.getTimestamp("creation_time"));
            accountExt.setStarLevel(rs.getInt("star_level"));
            accountExt.setRemark(rs.getString("remark"));
            accountExt.setDrawPassword(rs.getString("draw_password"));
            accountExt.setBalance(rs.getDouble("balance"));
            accountExt.setCode(rs.getString("code"));
            accountExt.setRole(rs.getInt("role"));
            accountExt.setQuestionNum(rs.getInt("question_num"));
            accountExt.setLastTime(rs.getTimestamp("last_time"));
            accountExt.setScore(rs.getInt("score"));
            return accountExt;
        }
    }

}
