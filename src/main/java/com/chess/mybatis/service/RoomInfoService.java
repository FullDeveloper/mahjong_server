package com.chess.mybatis.service;

import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.mybatis.daoImp.NoticeTableDaoImpl;
import com.chess.mybatis.daoImp.RoomInfoDaoImpl;
import com.chess.mybatis.entity.RoomInfo;
import com.chess.mybatis.mapper.RoomInfoMapper;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Date;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 16:41
 * Description:
 */
public class RoomInfoService {

    private static RoomInfoService roomInfoService = new RoomInfoService();

    private RoomInfoMapper roomInfoMapper;

    public void initSetSession(SqlSessionFactory sqlSessionFactory) {
        roomInfoMapper = new RoomInfoDaoImpl(sqlSessionFactory);
    }

    public static RoomInfoService getInstance() {
        return roomInfoService;
    }

    public Integer createRoomInfo(RoomVO roomVO) {
        //创建信息的同事创建其关联表
        RoomInfo room = new RoomInfo();
        room.setHong(roomVO.getHong() ? "1" : "0");
        room.setGameType(roomVO.getRoomType() + "");
        room.setMa(roomVO.getMa());
        room.setRoomId(roomVO.getRoomId());
        room.setSevenDouble(roomVO.getSevenDouble() ? "1" : "0");
        room.setXiaYu(roomVO.getXiaYu());
        room.setZimo(roomVO.getZiMo() == 0 ? "0" : "1");
        room.setName(roomVO.getName());
        room.setAddWordCard(roomVO.isAddWordCard() ? "1" : "0");
        room.setCreateTime(new Date());
        room.setCardNumber(roomVO.getRoundNumber() / 8);
        //创建RoomInfo表
        int index = roomInfoMapper.insertSelective(room);
        roomVO.setId(room.getId().intValue());
        return index;

    }
}
