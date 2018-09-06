package com.chess.mahjong.gameserver.msg.response.offline;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 11:48
 * Description:
 */
public class OffLineResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param msgCode
     */
    public OffLineResponse(int status, String msgCode) {
        super(status, ConnectAPI.OFF_LINE_RESPONSE);
        if(status>0){
            try {
                output.writeUTF(msgCode);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }
    }
}
