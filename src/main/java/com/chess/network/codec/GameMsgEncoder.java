package com.chess.network.codec;

import com.chess.network.codec.message.ResponseMsg;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 周润斌
 * @Date: create in 下午 6:16 2018-03-01
 * @Description:
 */
public class GameMsgEncoder extends ProtocolEncoderAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GameMsgEncoder.class);

    /**
     * 此处实现对ResponseMsg的编码工作,并将它写入输出流中
     *
     * @param ioSession
     * @param message
     * @param out
     * @throws Exception
     */
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        ResponseMsg value = (ResponseMsg) message;
        IoBuffer io = value.entireMsg();
//	    System.out.println("io长度"+io.getLong()+"-----getHexDump"+io.getHexDump());
        out.write(value.entireMsg());
        out.flush();
        value.release();
    }
}
