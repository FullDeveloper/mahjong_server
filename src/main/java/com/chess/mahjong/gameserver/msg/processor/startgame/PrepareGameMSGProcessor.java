package com.chess.mahjong.gameserver.msg.processor.startgame;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.network.codec.message.ClientRequest;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 12:51
 * Description: 准备/开始游戏，当所有人都准备好后直接开始游戏
 */
public class PrepareGameMSGProcessor extends MsgProcessor {

    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        // 获取当前用户所属房间信息
        RoomVO roomVo = gameSession.getRole(Avatar.class).getRoomVO();
        if (roomVo != null) {
            // 获取该房间的逻辑处理对象
            RoomLogic roomLogic = RoomManager.getInstance().getRoom(roomVo.getRoomId());
            if (roomLogic != null) {
                Avatar avatar = gameSession.getRole(Avatar.class);
                // 执行准备游戏
                roomLogic.readyGame(avatar);
            } else {
                gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000005));
            }

        }


    }

}
