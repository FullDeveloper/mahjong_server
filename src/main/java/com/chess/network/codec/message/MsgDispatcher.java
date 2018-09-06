package com.chess.network.codec.message;

import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.msg.processor.INotAuthProcessor;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessor;
import com.chess.mahjong.gameserver.msg.processor.MsgProcessorRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: zrb
 * Date: 2018/9/5
 * Time: 10:14
 * Description:  接收客户端请求 根据操作码转发对应的处理器
 */
public class MsgDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(MsgDispatcher.class);

    private Map<Integer, MsgProcessor> processorsMap = new HashMap<>();

    public MsgDispatcher() {
        for (MsgProcessorRegister register : MsgProcessorRegister.values()) {
            processorsMap.put(register.getMsgCode(), register.getMsgProcessor());
        }
        logger.info("初始化 消息处理器成功。。。");
    }


    /**
     * 通过协议号得到MsgProcessor
     *
     * @param msgCode
     * @return
     */
    public MsgProcessor getMsgProcessor(int msgCode) {
        return processorsMap.get(msgCode);
    }

    /**
     * 派发消息协议
     *
     * @param gameSession 游戏会话对象
     * @param clientRequest 客户端请求对象
     */
    public void dispatchMsg(GameSession gameSession, ClientRequest clientRequest) {
        int msgCode = clientRequest.getMsgCode();
        //客户端请求断开链接
        if (msgCode == 1000) {
            // TODO
        }
        MsgProcessor processor = getMsgProcessor(msgCode);
        if (gameSession.isLogin() || processor instanceof INotAuthProcessor) {
            processor.handle(gameSession, clientRequest);
        }
    }

}
