package com.chess.mahjong.gameserver.pojo;

import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 13:43
 * Description:
 */
public class PlayBehaviedVO {
    /**
     * 1出牌，2摸牌，3吃，4碰，5杠，6胡(自摸/点炮),7抢胡,8抓码.....
     */
    public int type;
    /**
     * 进行type操作时的牌(当为吃的时候存cardIndex1,cardIndex2,cardIndex3)
     */
    public String cardIndex;
    /**
     * 索引
     */
    public int accountIndexId;
    /**
     * 记录序号
     */
    public int recordIndex;
    /**
     * 杠的类型，1-别人点杠，2-自己暗杠，3-自己摸起来杠   4:-抢杠
     */
    public int gangType;

    /**
     * 抓的码   格式(1:2:3:5:6:8)
     */
    public String ma;
    /**
     * 有效码
     */
    public List<Integer> valideMa;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(String cardIndex) {
        this.cardIndex = cardIndex;
    }

    public int getAccountIndexId() {
        return accountIndexId;
    }

    public void setAccountIndexId(int accountIndexId) {
        this.accountIndexId = accountIndexId;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    public void setRecordIndex(int recordIndex) {
        this.recordIndex = recordIndex;
    }

    public int getGangType() {
        return gangType;
    }

    public void setGangType(int gangType) {
        this.gangType = gangType;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public List<Integer> getValideMa() {
        return valideMa;
    }

    public void setValideMa(List<Integer> valideMa) {
        this.valideMa = valideMa;
    }
}
