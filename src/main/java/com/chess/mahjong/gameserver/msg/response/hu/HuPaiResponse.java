package com.chess.mahjong.gameserver.msg.response.hu;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 14:39
 * Description:
 */
public class HuPaiResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param  str
     */
    public HuPaiResponse(int status, String str) {
        super(status, ConnectAPI.HUPAI_RESPONSE);
        if(status >0){
            try {
                //格式
                output.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        }
        //entireMsg();
    }
}
