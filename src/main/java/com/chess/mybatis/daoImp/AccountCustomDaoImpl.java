package com.chess.mybatis.daoImp;

import com.chess.mybatis.entity.Account;
import com.chess.mybatis.mapper.custom.AccountCustomMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 11:53
 * Description:
 */
public class AccountCustomDaoImpl implements AccountCustomMapper {

    private SqlSessionFactory sqlSessionFactory;

    public AccountCustomDaoImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Integer selectMaxId() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AccountCustomMapper customMapper = sqlSession.getMapper(AccountCustomMapper.class);
        return customMapper.selectMaxId();
    }

}
