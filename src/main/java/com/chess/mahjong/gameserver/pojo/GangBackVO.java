package com.chess.mahjong.gameserver.pojo;

import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/10
 * Time: 11:46
 * Description:
 */
public class GangBackVO {

    private List<Integer> cardList;

    /**
     * 明杠 0    暗杠 1
     */
    private int type;

    public List<Integer> getCardList() {
        return cardList;
    }

    public void setCardList(List<Integer> cardList) {
        this.cardList = cardList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
