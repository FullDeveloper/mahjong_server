package com.chess.mahjong.gameserver.msg.response;

import com.chess.context.ConnectAPI;
import com.chess.mahjong.gameserver.commons.code.ErrorCode;

import java.io.IOException;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 13:11
 * Description:
 */
public class ErrorResponse extends ServerResponse {

    /**
     * 必须调用此方法设置消息号
     *
     *  @param message 错误消息
     */
    public ErrorResponse(String message) throws IOException {
        super(ErrorCode.SUCCESS_CODE, ConnectAPI.ERROR_RESPONSE);
        output.writeUTF(message);
        output.close();
    }
}
