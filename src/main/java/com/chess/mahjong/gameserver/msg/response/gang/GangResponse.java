package com.chess.mahjong.gameserver.msg.response.gang;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import com.chess.mahjong.gameserver.pojo.GangBackVO;
import com.chess.persist.util.JsonUtilTool;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/10
 * Time: 11:46
 * Description:
 */
public class GangResponse extends ServerResponse {
    /**
     * @param status
     * @param fristPoint 第一张牌
     * @param nextPoint  第二张牌
     * @param type       明杠0 ， 暗杠 1
     */
    public GangResponse(int status, int fristPoint, int nextPoint, int type) {
        super(status, ConnectAPI.GANGPAI_RESPONSE);
        if (status > 0) {
            GangBackVO gangBackVO = new GangBackVO();
            gangBackVO.setType(type);
            try {
                output.writeUTF(JsonUtilTool.toJson(gangBackVO));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }
        // entireMsg();
    }
}
