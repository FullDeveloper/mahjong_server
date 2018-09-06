package com.chess.mahjong.gameserver.msg.response.login;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 13:16
 * Description:
 */
public class BreakLineResponse extends ServerResponse{

    public BreakLineResponse(int status) {
        super(status, ConnectAPI.BREAK_LINE_RESPONSE);
        try {
            if(status > 0) {
                output.writeUTF("1");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
    }

}
