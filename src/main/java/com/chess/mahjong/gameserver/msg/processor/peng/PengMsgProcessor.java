package com.chess.mahjong.gameserver.msg.processor.peng;

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
 * Date: 2018/9/13
 * Time: 14:06
 * Description:
 */
public class PengMsgProcessor extends MsgProcessor {

    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        RoomLogic roomLogic = RoomManager.getInstance().getRoom(gameSession.getRole(Avatar.class).getRoomVO().getRoomId());
        if(roomLogic != null){
            CardVO cardVO = JsonUtilTool.fromJson(request.getString(),CardVO.class);
            boolean isPeng =  roomLogic.pengCard(gameSession.getRole(Avatar.class),cardVO.getCardPoint());
            if(isPeng){

            }
            else{

            }
        }else{
            gameSession.sendMsg(new ErrorResponse(ErrorCode.Error_000005));
        }


    }

}
