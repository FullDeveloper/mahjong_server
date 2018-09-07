package com.chess.mahjong.gameserver.msg.response.chupai;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 14:03
 * Description:
 */
public class ChuPaiResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status cardIndex牌点数   curAvatarIndex出牌人的索引
     * @param
     */
    public ChuPaiResponse(int status, int cardIndex, int curAvatarIndex) {
        super(status, ConnectAPI.CHUPAI_RESPONSE);
        JSONObject json = new JSONObject();
        json.put("cardIndex", cardIndex);
        json.put("curAvatarIndex", curAvatarIndex);
        if (status > 0) {
            try {
                output.writeUTF(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }
        // entireMsg();
    }
}
