package com.chess.mahjong.gameserver.commons.initial;

import java.util.Properties;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 11:47
 * Description:
 */
public class Params {

    private static Properties properties = AppCf.getProperties();
    /**
     * 新玩家初始房卡数量
     */
    public static final  Integer initialRoomCard = Integer.valueOf(properties.get("initialRoomCard").toString());
    /**
     * 新玩家初始房卡数量
     */
    public static final  Integer initialPrizeCount = Integer.valueOf(properties.get("initialPrizeCount").toString());

}
