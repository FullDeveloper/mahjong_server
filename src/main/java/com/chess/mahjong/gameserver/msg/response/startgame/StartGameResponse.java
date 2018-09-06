package com.chess.mahjong.gameserver.msg.response.startgame;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 14:59
 * Description:
 */
public class StartGameResponse extends ServerResponse {
    /**
     * @param status
     * @param paiArray 自己的牌数组
     * @param bankerId 庄家ID
     */
    public StartGameResponse(int status, int[][] paiArray, int bankerId) {
        super(status, ConnectAPI.START_GAME_RESPONSE);
        try {
            JSONObject json = new JSONObject();
            json.put("paiArray", paiArray);
            json.put("bankerId", bankerId);
            output.writeUTF(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        entireMsg();
    }
}
