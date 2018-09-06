package com.chess.mahjong.gameserver.pojo;

/**
 * @author 周润斌
 * Date: 2018/9/6
 * Time: 16:26
 * Description:
 */
public class CardVO {

    /**
     * 牌的点数
     */
    private int cardPoint;

    /**
     * 两个吃牌之一
     */
    private int onePoint;

    /**
     * 两个吃牌之二
     */
    private int twoPoint;

    /**
     * 代表胡的类型
     */
    private String type;

    public int getCardPoint() {
        return cardPoint;
    }

    public void setCardPoint(int cardPoint) {
        this.cardPoint = cardPoint;
    }

    public int getOnePoint() {
        return onePoint;
    }

    public void setOnePoint(int onePoint) {
        this.onePoint = onePoint;
    }

    public int getTwoPoint() {
        return twoPoint;
    }

    public void setTwoPoint(int twoPoint) {
        this.twoPoint = twoPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
