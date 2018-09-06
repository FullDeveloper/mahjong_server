package com.chess.mahjong.gameserver.msg.response.login;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import com.chess.mahjong.gameserver.pojo.AvatarVO;
import com.chess.persist.util.JsonUtilTool;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 13:03
 * Description:
 */
public class LoginResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param avatarVO
     */
    public LoginResponse(int status, AvatarVO avatarVO) {
        super(status, ConnectAPI.LOGIN_RESPONSE);
        try {
            if (status > 0) {
                output.writeUTF(JsonUtilTool.toJson(avatarVO));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
    }
}
