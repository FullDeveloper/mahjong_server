package com.chess.mybatis.daoImp;

import com.chess.mybatis.entity.RoomInfo;
import com.chess.mybatis.entity.RoomInfoExample;
import com.chess.mybatis.mapper.RoomInfoMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 17:38
 * Description:
 */
public class RoomInfoDaoImpl implements RoomInfoMapper {

    private SqlSessionFactory sqlSessionFactory;

    public RoomInfoDaoImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public int countByExample(RoomInfoExample example) {
        return 0;
    }

    @Override
    public int deleteByExample(RoomInfoExample example) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(RoomInfo record) {
        return 0;
    }

    @Override
    public int insertSelective(RoomInfo record) {
        int flag = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            RoomInfoMapper mapper = sqlSession.getMapper(RoomInfoMapper.class);
            flag = mapper.insertSelective(record);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        return flag;
    }

    @Override
    public List<RoomInfo> selectByExample(RoomInfoExample example) {
        return null;
    }

    @Override
    public RoomInfo selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public int updateByExampleSelective(RoomInfo record, RoomInfoExample example) {
        return 0;
    }

    @Override
    public int updateByExample(RoomInfo record, RoomInfoExample example) {
        return 0;
    }

    @Override
    public int updateByPrimaryKeySelective(RoomInfo record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(RoomInfo record) {
        return 0;
    }
}
