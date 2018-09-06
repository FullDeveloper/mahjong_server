package com.chess.mahjong.gameserver.msg.response.joinroom;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import com.chess.mahjong.gameserver.pojo.AvatarVO;
import com.chess.persist.util.JsonUtilTool;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 10:30
 * Description:
 */
public class JoinRoomNotice extends ServerResponse {

    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param avatarVO
     */
    public JoinRoomNotice(int status, AvatarVO avatarVO) {
        super(status, ConnectAPI.JOIN_ROOM_NOTICE);
        if (status > 0) {
            try {
                output.writeUTF(JsonUtilTool.toJson(avatarVO));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }
    }

}
