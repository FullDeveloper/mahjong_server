package com.chess.mahjong.gameserver.msg.processor.login;

import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.msg.processor.INotAuthProcessor;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.response.login.OpenAppResponse;
import com.chess.network.codec.message.ClientRequest;

/**
 * Author: zrb
 * Date: 2018/9/5
 * Time: 10:24
 * Description: 用户打开应用时，做一些初始化操作
 */
public class OpenAppMsgProcessor extends MsgProcessor implements INotAuthProcessor {

    @Override
    public void process(GameSession gameSession, ClientRequest request) throws Exception {

        String requestMessage = request.getString();
        System.out.println("接收到客户端的请求消息====>" + requestMessage);

        gameSession.sendMsg(new OpenAppResponse(1, "welecome!"));
    }
}
