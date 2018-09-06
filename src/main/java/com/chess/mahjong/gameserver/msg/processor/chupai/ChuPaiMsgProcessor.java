package com.chess.mahjong.gameserver.msg.processor.chupai;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.mahjong.gameserver.pojo.CardVO;
import com.chess.network.codec.message.ClientRequest;
import com.chess.persist.util.JsonUtilTool;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 16:13
 * Description: 玩家出牌处理器
 */
public class ChuPaiMsgProcessor extends MsgProcessor {

    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        // 拿到出牌对象
        CardVO cardVO = JsonUtilTool.fromJson(request.getString(),CardVO.class);
        // 拿到房间逻辑对象
        RoomLogic roomLogic = RoomManager.getInstance().getRoom(gameSession.getRole(Avatar.class).getRoomVO().getRoomId());
        if(roomLogic != null){
            // 检查牌的点数
            if(cardVO.getCardPoint() == -1){
                gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000009));
            }else{
                //出牌，发送消息在方法里面
                roomLogic.chuCard(gameSession.getRole(Avatar.class), cardVO.getCardPoint());
            }
        }else{
            gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000005));
        }
    }

}
