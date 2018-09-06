package com.chess.network.codec.message;

import java.io.IOException;

/**
 * @Author: 周润斌
 * @Date: create in 下午 6:36 2018-03-01
 * @Description: 使用byte数组作为缓冲区，使用{@link MsgBodyWrap}将byte数组转成合适的数据。
 */
public class ClientRequest {

    int msgCode;

    MsgBodyWrap msgBodyWrap = null;

    public ClientRequest(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("消息缓冲区对象为null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("消息缓冲区对象大小为0");
        }
        msgBodyWrap = MsgBodyWrap.newInstance4In(array);
        try {
            msgCode = msgBodyWrap.readInt();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public int getMsgCode() {
        return msgCode;
    }

    public byte getByte() throws IOException {
        return msgBodyWrap.readByte();
    }

    public short getShort() throws IOException {
        return msgBodyWrap.readShort();
    }

    public int getInt() throws IOException {
        return msgBodyWrap.readInt();
    }

    public long getLong() throws IOException {
        return msgBodyWrap.readLong();
    }

    public float getFloat() throws IOException {
        return msgBodyWrap.readFloat();
    }

    public double getDouble() throws IOException {
        return msgBodyWrap.readDouble();
    }

    public String getString() throws IOException {
        return msgBodyWrap.readUTF();
    }

    public void relese() {
        if (msgBodyWrap != null) {
            msgBodyWrap.close();
        }
        msgBodyWrap = null;
    }


}
