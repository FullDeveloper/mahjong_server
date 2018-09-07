package com.chess.mahjong.gameserver.pojo;

/**
 * @author 周润斌
 * Date: 2018/9/7
 * Time: 13:46
 * Description:
 */
public class PlayRecordItemVO {
    /**
     * 玩家名
     */
    public String accountName;
    /**
     * 索引(游戏时对应的索引)
     */
    public int accountIndex;
    /**
     * 初始牌组成的字符串
     */
    public String cardList;
    /**
     * 头像
     */
    public String headIcon;
    /**
     * 分数
     */
    public int socre;
    /**
     * 性别
     */
    public int sex;
    /**
     * 当前局数
     */
    public int gameRound;
    /**
     * 用户uuid
     */
    public int uuid;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountIndex() {
        return accountIndex;
    }

    public void setAccountIndex(int accountIndex) {
        this.accountIndex = accountIndex;
    }

    public String getCardList() {
        return cardList;
    }

    public void setCardList(String cardList) {
        this.cardList = cardList;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public int getSocre() {
        return socre;
    }

    public void setSocre(int socre) {
        this.socre = socre;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getGameRound() {
        return gameRound;
    }

    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}
