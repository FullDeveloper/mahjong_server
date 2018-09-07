package com.chess.mahjong.gameserver.msg.response.common;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.msg.response.ServerResponse;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 14:21
 * Description:
 */
public class ReturnInfoResponse extends ServerResponse {
    /**
     * 必须调用此方法设置消息号
     *
     * @param status
     * @param  str
     */
    public ReturnInfoResponse(int status, String str) {
        super(status, ConnectAPI.RETURN_INFO_RESPONSE);
        if(status >0){
            try {
                //格式
                output.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
