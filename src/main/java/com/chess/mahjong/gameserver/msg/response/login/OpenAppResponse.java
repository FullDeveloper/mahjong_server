package com.chess.mahjong.gameserver.msg.response.login;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * Author: zrb
 * Date: 2018/9/5
 * Time: 10:29
 * Description:
 */
public class OpenAppResponse extends ServerResponse {

    public OpenAppResponse(int status, String initWord) {
        super(status, ConnectAPI.OPENAPP_RESPONSE);
        try {
            output.writeUTF(initWord);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
    }

}
