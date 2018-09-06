package com.chess.mybatis.daoImp;

import com.chess.mybatis.entity.Account;
import com.chess.mybatis.entity.AccountExample;
import com.chess.mybatis.mapper.AccountMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 11:38
 * Description:
 */
public class AccountDaoImpl implements AccountMapper {

    private SqlSessionFactory sqlSessionFactory;

    public AccountDaoImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public int countByExample(AccountExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(AccountExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(Account record) {
        int flag = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AccountMapper mapper = null;
        try {
            mapper = sqlSession.getMapper(AccountMapper.class);
            flag = mapper.insert(record);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                record.setNickName(filterNickName(record.getNickName()));
                flag = mapper.insert(record);
                sqlSession.commit();
            } catch (Exception e2) {
                e.printStackTrace();
                record.setNickName("???????");
                flag = mapper.insert(record);
                sqlSession.commit();
            }
        } finally {
            sqlSession.close();
        }
        return flag;
    }

    /**
     * @param nickname
     * @return String
     */
    public String filterNickName(String nickname) {
        String reg = "[^\u4e00-\u9fa5]";
        nickname = nickname.replaceAll(reg, "?");
        return nickname;
    }

    @Override
    public int insertSelective(Account record) {
        int flag = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AccountMapper mapper = null;
        try {
            mapper = sqlSession.getMapper(AccountMapper.class);
            flag = mapper.insertSelective(record);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //昵称出问题
            try {
                record.setNickName(filterNickName(record.getNickName()));
                flag = mapper.insertSelective(record);
                sqlSession.commit();
            } catch (Exception e2) {
                e.printStackTrace();
                record.setNickName("???????");
                flag = mapper.insertSelective(record);
                sqlSession.commit();
            }
        }finally {
            sqlSession.close();
        }
        return flag;
    }

    @Override
    public List<Account> selectByExample(AccountExample example) {
        List<Account> result = null;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
            result = mapper.selectByExample(example);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return result;
    }

    @Override
    public Account selectByPrimaryKey(Long id) {
        Account result = null;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            AccountMapper mapper = sqlSession.getMapper(AccountMapper.class);
            result = mapper.selectByPrimaryKey(id);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return result;
    }

    @Override
    public int updateByExampleSelective(Account record, AccountExample example) {
        return 0;
    }

    @Override
    public int updateByExample(Account record, AccountExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(Account record) {
        int flag = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        AccountMapper mapper = null;
        try{
            mapper = sqlSession.getMapper(AccountMapper.class);
            flag = mapper.updateByPrimaryKeySelective(record);
            sqlSession.commit();
        }catch (Exception e) {
            e.printStackTrace();
            //昵称出问题
            try {
                record.setNickName(filterNickName(record.getNickName()));
                flag = mapper.updateByPrimaryKeySelective(record);
                sqlSession.commit();
            } catch (Exception e2) {
                e.printStackTrace();
                record.setNickName("???????");
                flag = mapper.updateByPrimaryKeySelective(record);
                sqlSession.commit();
            }
        }finally {
            sqlSession.close();
        }
        return flag;
    }

    @Override
    public int updateByPrimaryKey(Account record) {
        return 0;
    }
}
