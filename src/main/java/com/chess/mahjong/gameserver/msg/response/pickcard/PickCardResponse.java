package com.chess.mahjong.gameserver.msg.response.pickcard;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import com.chess.mahjong.gameserver.pojo.CardVO;
import com.chess.persist.util.JsonUtilTool;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 14:46
 * Description:
 */
public class PickCardResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param ==     100 时表示是询问海底捞还是不捞
     */
    public PickCardResponse(int status, int cardPoint) {
        super(status, ConnectAPI.PICKCARD_RESPONSE);
        CardVO cardVO = new CardVO();
        cardVO.setCardPoint(cardPoint);
        try {
            output.writeUTF(JsonUtilTool.toJson(cardVO));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
