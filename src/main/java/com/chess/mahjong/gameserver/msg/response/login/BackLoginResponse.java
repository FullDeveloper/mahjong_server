package com.chess.mahjong.gameserver.msg.response.login;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import com.chess.mahjong.gameserver.pojo.RoomVO;
import com.chess.persist.util.JsonUtilTool;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 10:04
 * Description:
 */
public class BackLoginResponse extends ServerResponse {

    public BackLoginResponse(int status, RoomVO roomVO) {
        super(status, ConnectAPI.BACK_LOGIN_RESPONSE);
        try {
            if (status > 0) {
                output.writeUTF(JsonUtilTool.toJson(roomVO));
            } else {
                output.writeUTF(roomVO.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
    }
}
