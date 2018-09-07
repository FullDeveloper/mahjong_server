package com.chess.mahjong.gameserver.msg.response.pickcard;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;
import net.sf.json.JSONObject;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 14:48
 * Description:
 */
public class OtherPickCardResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param avatarIndex
     */
    public OtherPickCardResponse(int status, int avatarIndex) {
        super(status, ConnectAPI.OTHER_PICKCARD_RESPONSE);
        JSONObject json = new JSONObject();
        json.put("avatarIndex",avatarIndex);
        if(status >0){
            try {
                //System.out.println("发送摸牌信息给其他玩家-----摸牌人索引："+avatarIndex);
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
