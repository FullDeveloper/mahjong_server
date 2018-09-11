package com.chess.mahjong.gameserver.msg.response;

import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.network.codec.MsgProtocol;
import com.chess.network.codec.message.MsgBodyWrap;
import com.chess.network.codec.message.ResponseMsg;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * Author: zrb
 * Date: 2018/9/5
 * Time: 10:27
 * Description: 服务端发给客户端的消息。 所有返回给客户端的消息都最好继承于它.<br>
 * 这里封装了基本的输出字节操作。
 */
public class ServerResponse implements ResponseMsg {

    protected MsgBodyWrap output = MsgBodyWrap.newInstance4Out();
    private int msgCode;
    /**
     * 0: 失败， 1： 成功
     */
    private int status;

    /**
     * 必须调用此方法设置消息号
     */
    public ServerResponse(int status, int msgCode) {
        setStatus(status);
        setMsgCode(msgCode);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void setMsgCode(int code) {
        msgCode = code;
    }

    @Override
    public IoBuffer entireMsg() {
        byte[] body = output.toByteArray();
        /* 标志 byte 长度short */
        // 协议标记的长度 1 + 数据长度的字节长度 4 + 操作码的字节长度 4 + 内容的长度  + 4(可以当做是状态的字节长度)
        int length = MsgProtocol.FLAG_SIZE + MsgProtocol.LENGTH_SIZE + MsgProtocol.MSG_CODE_SIZE + body.length + 4;
        IoBuffer buf = IoBuffer.allocate(length);
        //flag
        buf.put(MsgProtocol.DEFALUT_FLAG);
        //数据长度
        buf.putInt(length);
        // 响应码
        buf.putInt(msgCode);
        // 状态 1：成功 -1：失败
        buf.putInt(status);

        buf.put(body);
        buf.flip();
        return buf;
    }

    @Override
    public void release() {
        if (output != null) {
            output.close();
            output = null;
        }
        output = null;
    }

    public void setOutput(MsgBodyWrap output) {
        this.output = output;
    }
}
