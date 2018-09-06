package com.chess.network;

import com.chess.mahjong.GameServerBootstrap;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.network.codec.message.ClientRequest;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 周润斌
 * @Date: create in 下午 7:04 2018-03-01
 * @Description:
 */
public class MinaMsgHandler extends IoHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MinaMsgHandler.class);

    /**
     * 会话打开时执行
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.info("a session create from ip {}", session.getRemoteAddress());
        // 初始化用户会话信息：保存用户的　IP地址，会话对象到session
        new GameSession(session);
    }

    /**
     * 收到消息时执行
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        ClientRequest clientRequest = (ClientRequest) message;
        GameSession gameSession = GameSession.getInstance(session);
        if (gameSession == null) {
            logger.info("gameSession is null, Please check Error!");
            return;
        }
        GameServerBootstrap.msgDispatcher.dispatchMsg(gameSession,clientRequest);
    }

    /**
     * 会话关闭时执行
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("a session closed ip:{}", session.getRemoteAddress());
        super.sessionClosed(session);
    }

    /**
     * 出错时执行
     *
     * @param session
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }


}
