package com.chess.network.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @Author: 周润斌
 * @Date: create in 下午 6:15 2018-03-01
 * @Description: 一个对象工厂，提供了获取编码器和解码器的函数接口。
 */
public class GameProtocolCodecFactory implements ProtocolCodecFactory {

    private final GameMsgEncoder gameMsgEncoder;
    private final GameMsgDecoder gameMsgDecoder;

    public GameProtocolCodecFactory() {
        gameMsgEncoder = new GameMsgEncoder();
        gameMsgDecoder = new GameMsgDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return gameMsgEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return gameMsgDecoder;
    }
}
