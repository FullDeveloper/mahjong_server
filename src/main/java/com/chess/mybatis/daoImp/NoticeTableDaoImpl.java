package com.chess.mybatis.daoImp;

import com.chess.mybatis.entity.NoticeTable;
import com.chess.mybatis.entity.NoticeTableExample;
import com.chess.mybatis.mapper.NoticeTableMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 15:29
 * Description:
 */
public class NoticeTableDaoImpl implements NoticeTableMapper {

    private SqlSessionFactory sqlSessionFactory;

    public NoticeTableDaoImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public int countByExample(NoticeTableExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(NoticeTableExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(NoticeTable record) {
        return 0;
    }

    @Override
    public int insertSelective(NoticeTable record) {
        return 0;
    }

    @Override
    public List<NoticeTable> selectByExample(NoticeTableExample example) {
        return null;
    }

    @Override
    public NoticeTable selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(NoticeTable record, NoticeTableExample example) {
        return 0;
    }

    @Override
    public int updateByExample(NoticeTable record, NoticeTableExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(NoticeTable record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(NoticeTable record) {
        return 0;
    }

    @Override
    public NoticeTable selectRecentlyObject() {

        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession();
            NoticeTableMapper mapper = sqlSession.getMapper(NoticeTableMapper.class);
            return mapper.selectRecentlyObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }

        }

        return null;
    }
}
