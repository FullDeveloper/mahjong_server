package com.chess.mahjong.gameserver.pojo;

import java.util.List;

/**
 * @author 周润斌
 * Date: 2018/9/5
 * Time: 16:02
 * Description:
 */
public class RoomVO {

    /**
     * 房间ID
     */
    private Integer roomId;
    /**
     * 数据库表ID
     */
    private Integer id;

    /**
     * 房间的使用总次数
     */
    private Integer roundNumber;

    /**
     * 房间当前轮数
     */
    private Integer currentRound = 0;

    /**
     * 房间模式，1-转转麻将。2-划水麻将。3-长沙麻将
     */
    private Integer roomType;

    /**
     * 房间名
     */
    public String name;

    /**
     *是否红中当赖子
     */
    private boolean hong;

    /**
     *七小对
     */
    private boolean sevenDouble;
    /**
     *抓码的个数
     */
    private int ma;

    /**
     *是否自摸胡，还是可以抢杠胡
     *0 可抢杠胡(默认)   1自摸胡
     */
    private int ziMo;

    /**
     * 下鱼(漂)(0--10)
     */
    private int xiaYu;
    /**
     * 是否要字牌
     */
    private boolean addWordCard;

    /**
     * 整个房间所有的玩家和对应的所有人的牌组
     */
    private List<AvatarVO> playerList;

    public List<AvatarVO> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<AvatarVO> playerList) {
        this.playerList = playerList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getHong() {
        return hong;
    }

    public void setHong(boolean hong) {
        this.hong = hong;
    }

    public boolean getSevenDouble() {
        return sevenDouble;
    }

    public void setSevenDouble(boolean sevenDouble) {
        this.sevenDouble = sevenDouble;
    }

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public int getZiMo() {
        return ziMo;
    }

    public void setZiMo(int ziMo) {
        this.ziMo = ziMo;
    }

    public int getXiaYu() {
        return xiaYu;
    }

    public void setXiaYu(int xiaYu) {
        this.xiaYu = xiaYu;
    }

    public boolean isAddWordCard() {
        return addWordCard;
    }

    public void setAddWordCard(boolean addWordCard) {
        this.addWordCard = addWordCard;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Integer getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Integer currentRound) {
        this.currentRound = currentRound;
    }

    public Integer getRoomType() {
        return roomType;
    }

    public void setRoomType(Integer roomType) {
        this.roomType = roomType;
    }
}
