package com.chess.mahjong.gameserver.msg.processor.createroom;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.mahjong.gameserver.msg.response.createroom.CreateRoomResponse;
import com.chess.mahjong.gameserver.pojo.AvatarVO;
import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.network.codec.message.ClientRequest;
import com.chess.persist.util.JsonUtilTool;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 16:01
 * Description: 创建房间处理器
 */
public class CreateRoomMsgProcssor extends MsgProcessor {


    /**
     * 房间次数的倍数  8次
     */
    private static final Integer ROOM_COUNT = 8;

    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        String message = request.getString();
        RoomVO roomVO = JsonUtilTool.fromJson(message, RoomVO.class);
        // 判断当前用户是否登陆
        if (gameSession.isLogin()) {
            // 从当前会话中获取创建房间的用户信息
            Avatar avatar = gameSession.getRole(Avatar.class);
            AvatarVO avatarVo = avatar.avatarVO;
            // 检查用户房卡是否足以支付创建该房所需的房卡
            if (avatarVo.getAccount().getRoomCard() >= roomVO.getRoundNumber() / ROOM_COUNT) {
                // 判断用户当前是否有房间
                if (avatarVo.getRoomId() == null) {
                    // 执行创建房间操作
                    RoomManager.getInstance().createRoom(avatar, roomVO);
                    gameSession.sendMsg(new CreateRoomResponse(ErrorCode.SUCCESS_CODE, roomVO.getRoomId() + ""));
                } else {
                    // 已经有房间了 无法继续创建房间
                    gameSession.sendMsg(new CreateRoomResponse(ErrorCode.SUCCESS_CODE, avatarVo.getRoomId() + ""));
                }
            } else {
                // 房卡不足
                gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000014));
            }

        }


    }
}
