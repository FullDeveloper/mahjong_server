package com.chess.mahjong.gameserver.msg.processor;

import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.network.codec.message.ClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: zrb
 * Date: 2018/9/5
 * Time: 10:18
 * Description:  父类  抽象处理消息方法
 */
public abstract class MsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MsgProcessor.class);

    public void handle(GameSession gameSession, ClientRequest request) {
        try {
            process(gameSession, request);
        } catch (Exception e) {
            logger.error("消息处理出错，msg code:" + request.getMsgCode());
            e.printStackTrace();
        }
    }

    /**
     *  处理客户端的请求
     * @param gameSession 游戏会话对象
     * @param request   客户端请求类
     * @throws Exception  处理中的异常信息
     */
    public abstract void process(GameSession gameSession, ClientRequest request) throws Exception;

}
