package com.chess.mahjong.gameserver.msg.processor.login;

import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.network.codec.message.ClientRequest;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 16:00
 * Description: 退出游戏处理器
 */
public class LogoutMsgProcessor extends MsgProcessor{


    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {

    }
}
