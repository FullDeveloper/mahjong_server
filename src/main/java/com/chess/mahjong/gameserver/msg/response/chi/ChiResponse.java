package com.chess.mahjong.gameserver.msg.response.chi;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/10
 * Time: 10:30
 * Description:
 */
public class ChiResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param str
     */
    public ChiResponse(int status, String str) {
        super(status, ConnectAPI.CHIPAI_RESPONSE);
        if (status > 0) {
            try {
                output.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }
    }
}
