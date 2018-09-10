package com.chess.mahjong.gameserver.msg.processor.chi;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;
import com.chess.mahjong.gameserver.msg.response.chi.ChiResponse;
import com.chess.mahjong.gameserver.pojo.CardVO;
import com.chess.network.codec.message.ClientRequest;
import com.chess.persist.util.JsonUtilTool;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 16:14
 * Description: 执行吃牌操作
 */
public class ChiMsgProcessor extends MsgProcessor {

    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        // 拿到当前用户所属的房间逻辑对象
        RoomLogic roomLogic = RoomManager.getInstance().getRoom(gameSession.getRole(Avatar.class).getRoomVO().getRoomId());
        if (roomLogic != null) {
            // 拿到吃的牌的点数
            CardVO cardVO = JsonUtilTool.fromJson(request.getString(), CardVO.class);
            //
            boolean isChi = roomLogic.chiCard(gameSession.getRole(Avatar.class), cardVO);
            if (isChi) {
                gameSession.sendMsg(new ChiResponse(ErrorCode.SUCCESS_CODE, "1"));
            } else {
                System.out.println("吃不起");
            }
        } else {
            gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000005));
        }


    }
}
