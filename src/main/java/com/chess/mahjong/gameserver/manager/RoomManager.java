package com.chess.mahjong.gameserver.manager;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.mybatis.service.RoomInfoService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 16:11
 * Description:
 */
public class RoomManager {

    private static RoomManager roomManager;

    /**
     * 房间号： 房间
     */
    private Map<Integer, RoomLogic> roomList;

    /**
     * 创建/加入房间之后存 玩家uuid和房间id
     */
    private Map<String, Integer> uuidAndRoomId;


    /**
     * 房间数量
     */
    private int roomCount = 0;

    public RoomManager() {
        roomList = new HashMap<>();
        uuidAndRoomId = new HashMap<>();
    }

    public static RoomManager getInstance() {
        if (roomManager == null) {
            roomManager = new RoomManager();
        }
        return roomManager;
    }

    /**
     * 创建房间
     * 生成房间号
     * 将玩家加入到房间中
     *
     * @param avatar
     */
    public void createRoom(Avatar avatar, RoomVO roomVO) {
        int roomId = randRoomId();
        roomVO.setRoomId(roomId);
        RoomLogic roomLogic = new RoomLogic(roomVO);
        // 创建房间内的玩家
        roomLogic.CreateRoom(avatar);
        //表中录入房间信息
        RoomInfoService.getInstance().createRoomInfo(roomVO);
        roomList.put(roomId, roomLogic);
        roomCount++;
        System.out.println("(创建新房间成功)当前在线房间数量：" + roomCount + ":" + roomList.size());
        addUuidAndRoomId(avatar.avatarVO.getAccount().getUuid(), roomVO.getRoomId());
    }


    /**
     * 存玩家id和房间roomid
     *
     * @param uuid   玩家uuid
     * @param roomid 房间id
     */
    public void addUuidAndRoomId(String uuid, Integer roomid) {
        uuidAndRoomId.put(uuid, roomid);
    }

    /**
     * 随机获取房间ID
     *
     * @return
     */
    private int randRoomId() {
        int roomId = (int) (899999 * Math.random());
        if (roomId < 100000) {
            roomId += 300000;
        }
        RoomLogic temp = getRoom(roomId);
        if (temp == null) {
            return roomId;
        } else {
            roomId = randRoomId();
        }
        return roomId;
    }

    /**
     * 获取room
     *
     * @param roomId 房间号
     * @return
     */
    public RoomLogic getRoom(int roomId) {
        return roomList.get(roomId);
    }

}
