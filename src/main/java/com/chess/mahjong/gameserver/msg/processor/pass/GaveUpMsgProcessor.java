package com.chess.mahjong.gameserver.msg.processor.pass;

import com.chess.mahjong.gameserver.Avatar;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.logic.RoomLogic;
import com.chess.mahjong.gameserver.manager.RoomManager;
import com.chess.mahjong.gameserver.msg.processor.INotAuthProcessor;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.network.codec.message.ClientRequest;

/**
 * @author 周润斌
 * Date: 2018/9/10
 * Time: 10:34
 * Description: 用户放弃操作 吃、碰、杠、胡
 */
public class GaveUpMsgProcessor extends MsgProcessor implements INotAuthProcessor{
    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {
        RoomLogic roomLogic = RoomManager.getInstance().getRoom(gameSession.getRole(Avatar.class).getRoomVO().getRoomId());
        if(roomLogic != null){
            roomLogic.gaveUpAction(gameSession.getRole(Avatar.class));
        }
    }
}
