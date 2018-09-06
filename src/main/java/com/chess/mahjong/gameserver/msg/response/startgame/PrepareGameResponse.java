package com.chess.mahjong.gameserver.msg.response.startgame;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 13:15
 * Description:
 */
public class PrepareGameResponse extends ServerResponse {
    /**
     *
     * @param status
     * @param  avatarIndex 准备人的索引
     */
    public PrepareGameResponse(int status,int avatarIndex) {
        super(status, ConnectAPI.PrepareGame_MSG_RESPONSE);
        try {
            JSONObject json = new JSONObject();
            json.put("avatarIndex", avatarIndex);
            output.writeUTF(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
        //entireMsg();
    }
}
