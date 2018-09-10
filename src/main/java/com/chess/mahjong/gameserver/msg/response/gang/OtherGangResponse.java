package com.chess.mahjong.gameserver.msg.response.gang;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/10
 * Time: 11:45
 * Description:
 */
public class OtherGangResponse extends ServerResponse {
    /**
     * @param status
     * @param avatarId
     * @param cardPoint
     */
    public OtherGangResponse(int status, int cardPoint, int avatarId, int type) {
        super(status, ConnectAPI.OTHER_GANGPAI_NOICE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardPoint", cardPoint);
        jsonObject.put("avatarId", avatarId);
        jsonObject.put("type", type);
        try {
            output.writeUTF(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
        //entireMsg();
    }
}
