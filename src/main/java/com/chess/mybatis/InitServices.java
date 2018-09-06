package com.chess.mybatis;

import com.chess.mybatis.entity.RoomInfo;
import com.chess.mybatis.service.AccountService;
import com.chess.mybatis.service.NoticeTableService;
import com.chess.mybatis.service.RoomInfoService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * Author: zrb
 * Date: 2018/9/4
 * Time: 14:48
 * Description:
 */
public class InitServices {

    private static InitServices initServers = new InitServices();

    public static InitServices getInstance() {
        return initServers;
    }


    public void initServiceFun() throws IOException {
        Reader reader = Resources.getResourceAsReader("myBatisConfig.xml");
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        AccountService.getInstance().initSetSession(sessionFactory);
        NoticeTableService.getInstance().initSetSession(sessionFactory);
        RoomInfoService.getInstance().initSetSession(sessionFactory);

    }

}
