package com.chess.mahjong.gameserver.msg.response.createroom;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 17:45
 * Description:
 */
public class CreateRoomResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param msgCode
     */
    public CreateRoomResponse(int status, String message) throws IOException {
        super(status, ConnectAPI.CREATEROOM_RESPONSE);
        if (status > 0) {
            output.writeUTF(message);
            System.out.println("roomId:" + message);
            output.close();

        }
    }
}
