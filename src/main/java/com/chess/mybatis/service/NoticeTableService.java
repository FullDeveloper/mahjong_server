package com.chess.mybatis.service;

import com.chess.mybatis.daoImp.AccountCustomDaoImpl;
import com.chess.mybatis.daoImp.AccountDaoImpl;
import com.chess.mybatis.daoImp.NoticeTableDaoImpl;
import com.chess.mybatis.entity.NoticeTable;
import com.chess.mybatis.mapper.NoticeTableMapper;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 15:28
 * Description:
 */
public class NoticeTableService {

    private static NoticeTableService noticeTableService = new NoticeTableService();

    private NoticeTableMapper noticeTableMapper;

    public static NoticeTableService getInstance() {
        return noticeTableService;
    }

    public void initSetSession(SqlSessionFactory sqlSessionFactory) {
        noticeTableMapper = new NoticeTableDaoImpl(sqlSessionFactory);
    }

    public NoticeTable selectRecentlyObject() {
        NoticeTable noticeTable = null;
        try{
            noticeTable = noticeTableMapper.selectRecentlyObject();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return noticeTable;
    }
}
