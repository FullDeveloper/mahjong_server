package com.chess.network.codec;

import com.chess.network.codec.message.ClientRequest;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: 周润斌
 * @Date: create in 下午 6:16 2018-03-01
 * @Description: 消息解码器。将连续的字节按照协议规范分割成完整的消息包，并包装成ClientRequest。
 */
public class GameMsgDecoder extends CumulativeProtocolDecoder {

    private static final Logger logger = LoggerFactory.getLogger(GameMsgDecoder.class);

    /**
     * flag(1 byte)+length(4 byte,后边内容的长度)+protocol code(4 byte)+content
     * length的长度包括  ：消息号+ 内容
     *
     * @param ioSession
     * @param ioBuffer
     * @param protocolDecoderOutput
     * @return
     * @throws Exception
     */
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        synchronized (ioBuffer) {

            // 定义解码协议
            if (ioBuffer.remaining() < (MsgProtocol.FLAG_SIZE + MsgProtocol.LENGTH_SIZE + MsgProtocol.MSG_CODE_SIZE)) {
                logger.info("数据包长度不足");
                return false;
            }
            System.out.println(ioBuffer.toString());
            //mark: 取当前的position的快照标记mark
            ioBuffer.mark();//0
            // flag 备用  读取1个字节 position从0开始 往后移2个 = 1
            byte flag = ioBuffer.get();
            if (flag == 1) {
                //读取长度字段 读取4个字节 此时position=5
                int length = ioBuffer.getInt();
                if (length <= 0 || length > MsgProtocol.MAX_PACKAGE_LENGTH) {
                    logger.info("数据包长度异常! 长度为:" + length);
                    return false;
                }
                //remaining()是返回limit-position的值
                //剩余 limit-position
                int surplus = ioBuffer.remaining();
                if (surplus >= length) {
                    //记录下当前的limit值
                    int preLimit = ioBuffer.limit();
                    /**
                     * 这行代码有一个bug，
                     * 读取协议内容时，如果第一个字节不是1，则越过此字节继续往后的读，直到读到1，
                     * 然而在设置limit时没有考虑到越过去的flag之前的字节，从而导致设置的limit比本应设置的位置小。
                     * 所以导致，iobuffer中当前position到设置的limit的长度小于我们要读取的length。
                     * 结果导致抛出BufferUnderflowException
                     */
                    // iobuffer.limit(MsgProtocol.flagSize+MsgProtocol.lengthSize+length);
                    // 如果position>limit, position = limit,如果mark>limit, 重置mark
                    // 数据总长度有25  经过position+length 变成只有23了
                    // 此操作为了避免EOFException异常
                    ioBuffer.limit(ioBuffer.position() + length);
                    byte[] body = new byte[length];
                    ioBuffer.get(body);
                    ioBuffer.limit(preLimit);
                    ClientRequest message = new ClientRequest(body);
                    protocolDecoderOutput.write(message);
                    return true;
                } else {
                    logger.info("数据包尚不完整！");
                    return false;
                }
            } else {
                logger.info("flag错误");
                return false;
            }
        }
    }

    private void printBuffer(IoBuffer ioBuffer) {

    }

    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }

    @Override
    public void dispose(IoSession session) throws Exception {
    }

}
