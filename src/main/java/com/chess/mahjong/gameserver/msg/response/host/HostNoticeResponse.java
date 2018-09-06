package com.chess.mahjong.gameserver.msg.response.host;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 15:33
 * Description:
 */
public class HostNoticeResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param message
     */
    public HostNoticeResponse(int status, String message) {
        super(status, ConnectAPI.HOST_SEND_RESPONSE);
        if (status > 0) {
            try {
                output.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }
    }
}
