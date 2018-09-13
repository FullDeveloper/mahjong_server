package com.chess.mahjong.gameserver.msg.response.peng;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/13
 * Time: 14:30
 * Description:
 */
public class PengResponse extends ServerResponse {

    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param
     */
    public PengResponse(int status, int cardPoint,int AvatarId) {
        super(status, ConnectAPI.PENGPAI_RESPONSE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cardPoint",cardPoint);
            jsonObject.put("avatarId",AvatarId);
            output.writeUTF(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
