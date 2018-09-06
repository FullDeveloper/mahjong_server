package com.chess.mybatis.service;

import com.chess.mybatis.daoImp.AccountCustomDaoImpl;
import com.chess.mybatis.daoImp.AccountDaoImpl;
import com.chess.mybatis.entity.Account;
import com.chess.mybatis.entity.AccountExample;
import com.chess.mybatis.mapper.AccountMapper;
import com.chess.mybatis.mapper.custom.AccountCustomMapper;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 11:22
 * Description:
 */
public class AccountService {

    private AccountMapper accMap;
    private AccountCustomMapper accountCustomMapper;

    private static AccountService accountService = new AccountService();

    public static AccountService getInstance() {
        return accountService;
    }

    public void initSetSession(SqlSessionFactory sqlSessionFactory) {
        accMap = new AccountDaoImpl(sqlSessionFactory);
        accountCustomMapper = new AccountCustomDaoImpl(sqlSessionFactory);
    }

    /**
     *
     * @param account
     * @throws SQLException
     */
    public int updateByPrimaryKeySelective(Account account){
        int index = 0;
        try{
            index = accMap.updateByPrimaryKeySelective(account);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return index;
    }

    /**
     * 创建新用户
     * @param account
     * @return 插入信息表中id
     * @throws SQLException
     */
    public int createAccount(Account account) throws SQLException{
        return accMap.insertSelective(account);
    }


    public Account selectAccount(String openId) {
        AccountExample example = new AccountExample();
        example.createCriteria().andOpenIdEqualTo(openId);
        List<Account> accountList = accMap.selectByExample(example);
        if (accountList != null && accountList.size() == 1) {
            return accountList.get(0);
        } else {
            return null;
        }
    }

    public Integer selectMaxId() {
        int index = accountCustomMapper.selectMaxId();
        System.out.println("-account selectMaxId index->>" + index);
        return index;
    }

    public Account selectByPrimaryKey(Long id) {
        return accMap.selectByPrimaryKey(id);
    }
}
