package com.chess.mahjong.gameserver.msg.response.login;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 9:59
 * Description:
 */
public class OtherBackLoginResponse extends ServerResponse {

    public OtherBackLoginResponse(int status, String uuid) {
        super(status, ConnectAPI.OTHER_BACK_LOGIN_RESPONSE);
        try {
            if (status > 0) {
                output.writeUTF(uuid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
    }

}
