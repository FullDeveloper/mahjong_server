package com.chess.mahjong.gameserver.commons.constant;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 13:08
 * Description:
 */
public class GameConstants {

    /**
     * 保存登陆用户key的前缀
     */
    public static final String SESSION_PREFIX = "uuid_";

    /**
     * 出牌操作
     */
    public static final Integer CHUPAi_OPTION = 1;
    /**
     * 摸牌操作
     */
    public static final Integer MOPAI_OPTION = 2;

    /**
     * 吃牌操作
     */
    public static final Integer CHIPAI_OPTION = 4;


    /**
     * 碰牌
     */
    public static final Integer PENG_PAI = 1;
    /**
     * 杠牌
     */
    public static final Integer GANG_PAI = 2;
    /**
     * 胡牌
     */
    public static final Integer HU_PAI = 3;
    /**
     * 吃牌
     */
    public static final Integer CHI_PAI = 4;
    /**
     * 抢胡
     */
    public static final Integer QIANG_HU = 5;
    /**
     * 明杠
     */
    public static final String MING_GANG = "4";
    /**
     * 暗杠
     */
    public static final String AN_GANG = "5";

}
