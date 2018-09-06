package com.chess.persist.util;

import com.chess.mahjong.gameserver.commons.code.ErrorCode;
import com.chess.mahjong.gameserver.commons.session.GameSession;
import com.chess.mahjong.gameserver.msg.response.ErrorResponse;

import java.io.IOException;

/**
 * Created by kevin on 2016/6/23.
 */
public class GlobalUtil {

    public static boolean checkIsLogin(GameSession session) {
        if (!session.isLogin()) {
            System.out.println("账户未登录或已经掉线!");
            try {
                session.sendMsg(new ErrorResponse(ErrorCode.Error_000002));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public static int getRandomUUid() {
        double d = Math.random();
        String subStr = String.valueOf(d).substring(7, 13);
        return Integer.parseInt(subStr);
    }

    public static int[] CloneIntList(int[] List) {
        int[] result = new int[List.length];
        for (int i = 0; i < List.length; i++) {
            result[i] = List[i];
        }
        return result;
    }
}
